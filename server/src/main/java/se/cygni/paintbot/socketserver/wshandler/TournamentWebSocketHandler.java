package se.cygni.paintbot.socketserver.wshandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import se.cygni.paintbot.socketserver.tournament.TournamentManager;

@Slf4j
@Service
@RequiredArgsConstructor
public class TournamentWebSocketHandler extends BaseGameSocketHandler {

    private final TournamentManager tournamentManager;

//    @Autowired
//    public TournamentWebSocketHandler(TournamentManager tournamentManager) {
//        this.tournamentManager = tournamentManager;
//
//        log.info("Started tournament web socket handler");
//
//        // Get an eventbus and register this handler
////        this.setOutgoingEventBus(tournamentManager.getOutgoingEventBus());
////        this.setIncomingEventBus(tournamentManager.getIncomingEventBus());
//    }

    @Override
    protected void playerLostConnection() {
        tournamentManager.playerLostConnection(getPlayerId());
    }
}
