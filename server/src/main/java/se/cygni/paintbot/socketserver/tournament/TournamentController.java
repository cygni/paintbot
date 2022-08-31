package se.cygni.paintbot.socketserver.tournament;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.cygni.paintbot.eventapi.model.TournamentInfo;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class TournamentController {
    private final TournamentManager tournamentManager;

    @GetMapping("/tournament/active")
    public ResponseEntity<TournamentInfo> getActiveTournament() {
        if (tournamentManager.isTournamentActive()) {
            TournamentInfo tournamentInfo = tournamentManager.getTournamentInfo();
            return ResponseEntity.ok(tournamentInfo);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
