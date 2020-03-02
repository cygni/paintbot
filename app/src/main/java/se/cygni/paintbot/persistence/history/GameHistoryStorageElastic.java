package se.cygni.paintbot.persistence.history;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import se.cygni.paintbot.api.GameMessage;
import se.cygni.paintbot.api.GameMessageParser;
import se.cygni.paintbot.eventapi.ApiMessageParser;
import se.cygni.paintbot.eventapi.history.GameHistory;
import se.cygni.paintbot.eventapi.history.GameHistorySearchItem;
import se.cygni.paintbot.eventapi.history.GameHistorySearchResult;
import se.cygni.paintbot.persistence.history.domain.GameHistoryPersisted;

import java.io.IOException;
import java.util.*;

@Profile({"production"})
@Component
public class GameHistoryStorageElastic implements GameHistoryStorage {

    private static Logger log = LoggerFactory
            .getLogger(GameHistoryStorageElastic.class);

    private final static int MAX_SEARCH_RESULT = 20;

    @Value("${paintbot.elastic.gamehistory.index}")
    private String gameHistoryIndex;

    @Value("${paintbot.elastic.gamehistory.type}")
    private String gameHistoryType;

    @Value("${paintbot.elastic.gameevent.index}")
    private String gameEventIndex;

    @Value("${paintbot.elastic.gameevent.type}")
    private String gameEventType;

    private final EventBus eventBus;
    private final RestHighLevelClient elasticClient;

    @Autowired
    public GameHistoryStorageElastic(EventBus eventBus, RestHighLevelClient elasticClient) {
        log.debug("GameHistoryStorageElastic started");

        this.eventBus = eventBus;
        this.eventBus.register(this);
        this.elasticClient = elasticClient;
    }

    @Override
    @Subscribe
    public void addGameHistory(GameHistory gameHistory) {
        try {
            gameHistory.getMessages().forEach(gameMessage -> {

                String eventId = UUID.randomUUID().toString();
                try {
                    IndexRequest indexRequest = new IndexRequest(gameEventIndex, gameEventType, eventId);
                    String msg = GameMessageParser.encodeMessage(gameMessage);
                    indexRequest.source(msg, XContentType.JSON);
                    elasticClient.index(indexRequest);
                } catch (Exception e) {
                    log.error("Failed to store GameEvent", e);
                }
            });

            GameHistoryPersisted ghp = new GameHistoryPersisted(
                    gameHistory.getGameId(),
                    gameHistory.getPlayerNames(),
                    gameHistory.getGameDate()
            );

            IndexRequest indexRequest = new IndexRequest(gameHistoryIndex, gameHistoryType, gameHistory.getGameId());
            String msg = ApiMessageParser.encodeMessage(ghp);
            indexRequest.source(msg, XContentType.JSON);
            elasticClient.index(indexRequest);
        } catch (Exception e) {
            log.error("Failed to store a GameHistory", e);
        }
    }

    @Override
    public Optional<GameHistory> getGameHistory(String gameId) {
        SearchRequest searchRequest = new SearchRequest(gameHistoryIndex);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
                .query(QueryBuilders.idsQuery(gameHistoryType).addIds(gameId));
        searchRequest.source(searchSourceBuilder);

        try {
            SearchResponse esResponse = elasticClient.search(searchRequest);
            if (esResponse.getHits().totalHits > 0) {
                GameHistoryPersisted ghp = (GameHistoryPersisted) ApiMessageParser.decodeMessage(esResponse.getHits().getAt(0).getSourceAsString());
                List<GameMessage> gameMessages = getGameEventsForGame(gameId);

                GameHistory gameHistory = new GameHistory(
                        ghp.getGameId(),
                        ghp.getPlayerNames(),
                        ghp.getGameDate(),
                        gameMessages
                );

                return Optional.of(gameHistory);
            }
        } catch (Exception e) {
            log.error("Failed to deserialize stored GameHistory", e);
        }
        return Optional.empty();
    }

    private List<GameMessage> getGameEventsForGame(String gameId) {
        List<GameMessage> messages = new ArrayList<>();

        QueryBuilder qb = QueryBuilders.termQuery("gameId.keyword", gameId);

        SearchRequest searchRequest = new SearchRequest(gameEventIndex)
                .scroll(new TimeValue(60000));
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
                .query(qb)
                .size(200);
        searchRequest.source(searchSourceBuilder);

        SearchResponse scrollResp;
        try {
            scrollResp = elasticClient.search(searchRequest);
        } catch (IOException e) {
            log.error("Failed to search ElasticSearch");
            return messages;
        }


        //Scroll until no hits are returned
        while (true) {

            for (SearchHit hit : scrollResp.getHits().getHits()) {
                try {
                    GameMessage gameMessage = GameMessageParser.decodeMessage(hit.getSourceAsString());
                    messages.add(gameMessage);
                } catch (Exception e) {
                    log.error("Failed to decode GameMessage", e);
                }
            }

            try {
                scrollResp = elasticClient.searchScroll(new SearchScrollRequest(scrollResp.getScrollId()).scroll(new TimeValue(60000)));
            } catch (IOException e) {
                log.error("Failed to search ElasticSearch when scrolling");
                return messages;
            }
            //Break condition: No hits are returned
            if (scrollResp.getHits().getHits().length == 0) {
                break;
            }
        }

        messages.sort(Comparator.comparingLong(GameMessage::getTimestamp));

        return messages;
    }

    @Override
    public GameHistorySearchResult listGamesWithPlayer(String playerName) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
                .query(QueryBuilders.matchQuery("playerNames", playerName))
                .size(MAX_SEARCH_RESULT);

        return getGameHistorySearchResult(searchSourceBuilder);
    }

    @Override
    public GameHistorySearchResult getHistoricGames() {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
                .query(QueryBuilders.existsQuery("playerNames"))
                .size(MAX_SEARCH_RESULT);

        return getGameHistorySearchResult(searchSourceBuilder);
    }

    private GameHistorySearchResult getGameHistorySearchResult(SearchSourceBuilder searchSourceBuilder) {
        SearchRequest searchRequest = new SearchRequest(gameHistoryIndex);
        searchRequest.source(searchSourceBuilder);

        List<GameHistorySearchItem> items = new ArrayList<>();
        try {
            SearchResponse esResponse = elasticClient.search(searchRequest);

            Iterator<SearchHit> searchHitIterator = esResponse.getHits().iterator();
            int counter = 0;
            while (searchHitIterator.hasNext() && counter < MAX_SEARCH_RESULT) {
                GameHistoryPersisted gh = (GameHistoryPersisted) ApiMessageParser.decodeMessage(searchHitIterator.next().getSourceAsString());
                items.add(new GameHistorySearchItem(gh.getGameId(), gh.getPlayerNames(), gh.getGameDate()));
                counter++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        items.sort((o1, o2) -> o2.getGameDate().compareTo(o1.getGameDate()));
        return new GameHistorySearchResult(items);
    }


}
