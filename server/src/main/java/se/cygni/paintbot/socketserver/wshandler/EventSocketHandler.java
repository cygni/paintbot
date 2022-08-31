package se.cygni.paintbot.socketserver.wshandler;

//import com.google.common.eventbus.EventBus;
//import com.google.common.eventbus.Subscribe;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import se.cygni.paintbot.api.GameMessage;
import se.cygni.paintbot.api.GameMessageParser;
import se.cygni.paintbot.api.event.*;
import se.cygni.paintbot.api.request.HeartBeatRequest;
import se.cygni.paintbot.api.response.HeartBeatResponse;
import se.cygni.paintbot.eventapi.ApiMessage;
import se.cygni.paintbot.eventapi.ApiMessageParser;
import se.cygni.paintbot.eventapi.exception.ApiMessageException;
import se.cygni.paintbot.eventapi.model.ActiveGame;
import se.cygni.paintbot.eventapi.model.ActiveGamePlayer;
import se.cygni.paintbot.eventapi.model.TournamentGame;
import se.cygni.paintbot.eventapi.model.TournamentGamePlan;
import se.cygni.paintbot.eventapi.request.*;
import se.cygni.paintbot.eventapi.response.ActiveGamesList;
import se.cygni.paintbot.eventapi.response.NoActiveTournamentEvent;
import se.cygni.paintbot.eventapi.response.TournamentCreated;
import se.cygni.paintbot.eventapi.response.TournamentKilled;
import se.cygni.paintbot.socketserver.event.InternalGameEvent;
import se.cygni.paintbot.socketserver.game.Game;
import se.cygni.paintbot.socketserver.game.GameManager;
import se.cygni.paintbot.socketserver.mapper.GameSettingsMapper;
import se.cygni.paintbot.socketserver.tournament.TournamentManager;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This is a per-connection websocket. That means a new instance will
 * be created for each connecting client.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EventSocketHandler extends TextWebSocketHandler {
    private WebSocketSession session;
    private String[] filterGameIds = new String[0];
    //    private EventBus globalEventBus;
    private final ObjectMapper objectMapper;
    private final GameManager gameManager;
    private final TournamentManager tournamentManager;

//    @Autowired
//    public EventSocketHandler(
////            EventBus globalEventBus,
//            GameManager gameManager,
//            TournamentManager tournamentManager,
//            TokenService tokenService) {
//
////        this.globalEventBus = globalEventBus;
//        this.gameManager = gameManager;
//        this.tournamentManager = tournamentManager;
//        this.tokenService = tokenService;
//        log.info("EventSocketHandler started!");
//    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        log.info("Opened new event session for " + session.getId());
        this.session = session;
//        globalEventBus.register(this);
        sendListOfActiveGames();
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
//        globalEventBus.unregister(this);
        log.info("Removed session: {}", session.getId());
    }

    private boolean tryToHandleApiMessage(String message) {
        try {
            ApiMessage apiMessage = ApiMessageParser.decodeMessage(message);

            // TODO: this is performed by spring security
//            if (!verifyTokenSendErrorIfUnauthorized(apiMessage)) {
//                return true;
//            }

            if (apiMessage instanceof ListActiveGames) {
                sendListOfActiveGames();
            } else if (apiMessage instanceof SetGameFilter) {
                setActiveGameFilter((SetGameFilter) apiMessage);
            } else if (apiMessage instanceof StartGame) {
                startGame((StartGame) apiMessage);
            } else if (apiMessage instanceof GetActiveTournament) {
                if (tournamentManager.isTournamentActive()) {
                    sendApiMessage(tournamentManager.getTournamentInfo());
                } else {
                    sendApiMessage(new NoActiveTournamentEvent());
                }
            } else if (apiMessage instanceof KillTournament) {
                // ToDo: Do we really need the current tournamentId?
                tournamentManager.killTournament();
                sendApiMessage(new TournamentKilled());

            } else if (apiMessage instanceof CreateTournament) {
                CreateTournament createTournament = (CreateTournament) apiMessage;


                // ToDo: Handle case that a tournament is already started

                if (tournamentManager.isTournamentActive()) {
                    tournamentManager.killTournament();
                }
                tournamentManager.createTournament(createTournament.getTournamentName());
                sendApiMessage(new TournamentCreated(
                        tournamentManager.getTournamentId(),
                        tournamentManager.getTournamentName(),
                        GameSettingsMapper.INSTANCE.gameFeaturesToGameSettings(tournamentManager.getGameFeatures())
                ));
            } else if (apiMessage instanceof UpdateTournamentSettings) {
                UpdateTournamentSettings updateTournamentSettings = (UpdateTournamentSettings) apiMessage;

                // ToDo: Handle case that a tournament is already started
                tournamentManager.setGameFeatures(
                        GameSettingsMapper.INSTANCE.gameSettingsToGameFeatures(
                                updateTournamentSettings.getGameSettings()
                        )
                );

            } else if (apiMessage instanceof StartTournament) {

                tournamentManager.startTournament();

            } else if (apiMessage instanceof StartTournamentGame) {
                StartTournamentGame startGame = (StartTournamentGame) apiMessage;
                tournamentManager.startGame(startGame.getGameId());
            }
        } catch (Exception e) {
            log.debug("Got exception when handling API message", e);
            sendApiMessage(new ApiMessageException(e.getMessage()));
            return false;
        }
        return true;
    }

    private boolean tryToHandleGameMessage(String message) {
        try {
            GameMessage gameMessage = GameMessageParser.decodeMessage(message);
            if (gameMessage instanceof HeartBeatRequest) {
                sendHeartbeat();
            } else {
                log.info("Got GameMessage that I'm not prepared to handle: {}", message);
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message)
            throws Exception {
        String msg = message.getPayload();
        log.debug(msg);

        if (!tryToHandleGameMessage(msg)) {
            if (!tryToHandleApiMessage(msg)) {
                log.error("Got message which I could not understand: {}", msg);
            }
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception)
            throws Exception {
        session.close(CloseStatus.SERVER_ERROR);
//        globalEventBus.unregister(this);
        log.info("Transport error, removed session: {}", session.getId());
    }

    //    @Subscribe
    public void onInternalGameEvent(InternalGameEvent event) {
        GameMessage gameMessage = event.getGameMessage();

        if (gameMessage instanceof GameCreatedEvent ||
                gameMessage instanceof GameChangedEvent ||
                gameMessage instanceof GameAbortedEvent) {
            sendListOfActiveGames();
            return;
        }

        if (gameMessage instanceof TournamentEndedEvent) {
            sendGameMessage(gameMessage);
            return;
        }

        if (gameMessage instanceof GameEndedEvent) {
            sendListOfActiveGames();
        }

        sendGameEvent(event.getGameMessage());
    }

    //    @Subscribe
    public void onTournamentGamePlan(TournamentGamePlan tgp) {
        sendApiMessage(tgp);
    }

    private void sendGameEvent(GameMessage message) {
        if (!session.isOpen())
            return;

        String gameId = extractGameId(message);
        if (ArrayUtils.contains(filterGameIds, gameId)) {
            sendGameMessage(message);
        }
    }

    private void sendHeartbeat() {
        HeartBeatResponse heartBeatResponse = new HeartBeatResponse();
        sendGameMessage(heartBeatResponse);
    }

    private void sendListOfActiveGames() {
        log.debug("Sending updated list of games");
        List<Game> games = gameManager.listAllGames();

        List<ActiveGame> activeGames = games.stream().map(game -> {
            List<ActiveGamePlayer> players = game.getPlayerManager().toSet().stream().map(player -> {
                return new ActiveGamePlayer(player.getName(), player.getPlayerId(), player.getTotalPoints());
            }).collect(Collectors.toList());

            return new ActiveGame(
                    game.getGameId(),
                    ArrayUtils.contains(filterGameIds, game.getGameId()),
                    GameSettingsMapper.INSTANCE.gameFeaturesToGameSettings(game.getGameFeatures()),
                    players);

        }).collect(Collectors.toList());

        ActiveGamesList gamesList = new ActiveGamesList(activeGames);
        sendApiMessage(gamesList);
    }

    private void setActiveGameFilter(SetGameFilter gameFilter) {
        this.filterGameIds = gameFilter.getIncludedGameIds();
    }

    private void startGame(StartGame apiMessage) {
        Game game = gameManager.getGame(apiMessage.getGameId());
        log.info(apiMessage.getGameId());
        if (game != null) {
            log.info("Starting game: {}", game.getGameId());
            log.info("Active remote players: {}", game.getPlayerManager().getLiveAndRemotePlayers().size());
            game.startGame();
        }
    }

//    private boolean verifyTokenSendErrorIfUnauthorized(ApiMessage apiMessage) {
//        try {
//            Game g = objectMapper.readValue(apiMessage.toString(), Game.class);
//            String token = "pepe-token";//BeanUtils.getProperty(apiMessage, "token");
//            if (!tokenService.isTokenValid(token)) {
//                String msg = String.format("Operation %s requires valid token. Specified token: %s is invalid",
//                        apiMessage.getClass().getSimpleName(),
//                        token);
//                Unauthorized unauthorized = new Unauthorized(msg);
//                sendApiMessage(unauthorized);
//                return false;
//            }
//        } catch (Exception e) {
//            // Happens if the ApiMessage doesn't have a token property
//            // in which case no authorization is needed.
//        }
//        return true;
//    }

    private String extractGameId(GameMessage gameMessage) {
        try {
            TournamentGame game = objectMapper.readValue(gameMessage.toString(), TournamentGame.class);
            return game.getGameId();
            //return BeanUtils.getProperty(gameMessage, "gameId");
        } catch (Exception e) {
            return ":";
        }
    }

    private void sendGameMessage(GameMessage gameMessage) {
        try {
            if (session.isOpen()) {
                session.sendMessage(new TextMessage(GameMessageParser.encodeMessage(gameMessage)));
            }
        } catch (IOException e) {
            log.error("Failed to send GameMessage over eventsocket", e);
        }
    }

    private void sendApiMessage(ApiMessage apiMessage) {
        try {
            if (session.isOpen()) {
                session.sendMessage(new TextMessage(ApiMessageParser.encodeMessage(apiMessage)));
            }
        } catch (IOException e) {
            log.error("Failed to send GameMessage over eventsocket", e);
        }
    }
}
