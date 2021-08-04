package io.streammachine.driver.serializer;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.avro.Schema;

import java.util.HashMap;
import java.util.Map;

public class SerializerProvider {
    private static final Map<String, EventSerializer> serializers = new HashMap<>();

    private SerializerProvider() {
    }

    // FIXME ensure that the caller gets the correct serializer type, regardless of the provided schemaId
    // Now, it's possible for a caller to get an AvroSerializer, while expecting a JsonSerializer, since the schemaId
    // can be the same by coincident
    public static EventSerializer getSerializer(String schemaId, Object schema) {
        final EventSerializer existingSerializer = serializers.get(schemaId);
        if (existingSerializer == null) {
            if (schema instanceof Schema) {
                final AvroSerializer serializer = new AvroSerializer((Schema) schema);
                serializers.put(schemaId, serializer);
                return serializer;
            } else if (schema instanceof JsonNode) {
                final JsonSerializer serializer = new JsonSerializer((JsonNode) schema);
                serializers.put(schemaId, serializer);
                return serializer;
            } else {
                throw new UnsupportedSerializationTypeException("Provided serialization type is not supported");
            }
        }

        return existingSerializer;
    }
}
