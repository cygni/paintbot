package se.cygni.paintbot.eventapi.exception;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import se.cygni.paintbot.eventapi.ApiMessage;
import se.cygni.paintbot.eventapi.type.ApiMessageType;

@ApiMessageType
public class ApiMessageException extends ApiMessage {

    private final String errorMessage;

    @JsonCreator
    public ApiMessageException(
            @JsonProperty("errorMessage") String errorMessage) {

        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

}
