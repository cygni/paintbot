package se.cygni.snake.tournament;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import se.cygni.snake.eventapi.model.TournamentInfo;

@RestController
public class TournamentController {

    private final TournamentManager tournamentManager;

    @Autowired
    public TournamentController(TournamentManager tournamentManager) {
        this.tournamentManager = tournamentManager;
    }

    @GetMapping("/tournament/active")
    public ResponseEntity<TournamentInfo> getActiveTournament() {
        if (tournamentManager.isTournamentActive()) {

            return new ResponseEntity<TournamentInfo>(tournamentManager.getTournamentInfo(),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }
}
