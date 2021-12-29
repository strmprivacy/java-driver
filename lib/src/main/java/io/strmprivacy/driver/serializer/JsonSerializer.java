package io.strmprivacy.driver.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonSerializer implements EventSerializer {

    private static final Logger log = LoggerFactory.getLogger(EventSerializer.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final JsonNode schema;

    public JsonSerializer(JsonNode schema) {
        this.schema = schema;
    }

    public byte[] serialize(Object event, SerializationType serializationType) {
        try {
            return MAPPER.writeValueAsBytes(event);
        } catch (JsonProcessingException e) {
            log.error("Json processing error {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
