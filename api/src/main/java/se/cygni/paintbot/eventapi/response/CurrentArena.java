package se.cygni.paintbot.eventapi.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import se.cygni.paintbot.api.model.GameSettings;
import se.cygni.paintbot.eventapi.ApiMessage;
import se.cygni.paintbot.eventapi.type.ApiMessageType;

@ApiMessageType
public class CurrentArena extends ApiMessage {

    private final String currentArena;

    @JsonCreator
    public CurrentArena(
            @JsonProperty("currentArena") String currentArena) {

        this.currentArena = currentArena;
    }

    public String getCurrentArena() {
        return currentArena;
    }

}
