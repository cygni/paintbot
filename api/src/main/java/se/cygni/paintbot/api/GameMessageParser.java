package se.cygni.paintbot.api;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Slf4j
public class GameMessageParser {

    private final static ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(new JavaTimeModule());
    }

    private GameMessageParser() {
    }

    public static GameMessage decodeMessage(final String msg)
            throws IOException {
        try {
            return mapper
                    .readValue(msg, GameMessage.class);
        } catch (final IllegalStateException e) {
            throw e;
        }
    }

    public static String encodeMessage(final GameMessage message)
            throws IOException {

        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        mapper.writeValue(out, message);
        return out.toString();
    }
}
