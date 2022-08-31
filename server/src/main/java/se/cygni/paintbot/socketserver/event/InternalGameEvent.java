package se.cygni.paintbot.socketserver.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import se.cygni.paintbot.api.GameMessage;
import se.cygni.paintbot.socketserver.mapper.GameMessageMapper;

import java.time.Clock;
import java.time.Instant;

@Getter
public class InternalGameEvent extends ApplicationEvent {
    private final Instant timeStamp;
    private GameMessage gameMessage;

    public InternalGameEvent(Object source, Clock clock) {
        super(source, clock);
        this.timeStamp = clock.instant();
    }

    public InternalGameEvent(Object source, Clock clock, GameMessage gameMessage) {
        super(source);
        this.timeStamp = clock.instant();
    }

    public void onGameAborted(String gameId) {
        this.gameMessage = GameMessageMapper.onGameAborted(gameId);
    }

    public void onGameChanged(String gameId) {
        this.gameMessage = GameMessageMapper.onGameChanged(gameId);
    }
}
