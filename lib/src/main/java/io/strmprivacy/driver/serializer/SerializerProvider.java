package io.strmprivacy.driver.serializer;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.avro.Schema;

import java.util.HashMap;
import java.util.Map;

public class SerializerProvider {
    private static final Map<String, EventSerializer> serializers = new HashMap<>();

    private SerializerProvider() {
    }

    public static EventSerializer getSerializer(String schemaId_, Object schema) {
        return serializers.computeIfAbsent(schemaId_, schemaId -> {
            if (schema instanceof Schema) {
                return new AvroSerializer((Schema) schema);
            } else if (schema instanceof JsonNode) {
                return new JsonSerializer((JsonNode) schema);
            } else {
                throw new UnsupportedSerializationTypeException("Provided serialization type is not supported");
            }
        });
    }
}
