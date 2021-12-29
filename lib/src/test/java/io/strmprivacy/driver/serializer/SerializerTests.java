package io.strmprivacy.driver.serializer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.strmprivacy.schemas.StrmPrivacyEvent;
import io.strmprivacy.schemas.demo.v1.DemoEvent;
import io.strmprivacy.schemas.demo.v1.StrmMeta;
import org.apache.avro.Schema;
import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SerializerTests {
    //    private static final Logger log = LoggerFactory.getLogger(SerializerTests.class);
    static ObjectMapper mapper = new ObjectMapper();

    @Test
    void serializeAvroTest() throws IOException {
        DemoEvent event = avroDemoEvent();
        EventSerializer serializer = SerializerProvider.getSerializer(event.getSchemaRef(), event.getSchema());
        DemoEvent readBack = parseBytes(event.getSchema(),
                serializer.serialize(event, SerializationType.AVRO_BINARY));
        assertEquals(event, readBack);
    }

    private DemoEvent parseBytes(Schema schema, byte[] bytes) throws IOException {
        SpecificDatumReader<DemoEvent> reader = new SpecificDatumReader<>(schema);
        DataInputStream din = new DataInputStream(new ByteArrayInputStream(bytes, 0, bytes.length));
        BinaryDecoder binaryDecoder = DecoderFactory.get().binaryDecoder(din, null);
        return reader.read(null, binaryDecoder);
    }

    @Test
    void serializeJsonSchemaTest() throws IOException {
        JsonSchemaDemoEvent event = jsonSchemaDemoEvent();
        EventSerializer serializer = SerializerProvider.getSerializer(event.getSchemaRef(), event.getSchema());
        byte[] bytes = serializer.serialize(event, SerializationType.JSON);
        JsonSchemaDemoEvent readback = mapper.readValue(bytes, JsonSchemaDemoEvent.class);
        assertEquals(event, readback);
        assert (bytes.length > 0);
    }

    public static JsonSchemaDemoEvent jsonSchemaDemoEvent() throws JsonProcessingException {
        return mapper.readValue("{\"field\": 123}", JsonSchemaDemoEvent.class);
    }

    public static DemoEvent avroDemoEvent() {
        return DemoEvent.newBuilder()
                .setStrmMeta(StrmMeta.newBuilder()
                        .setEventContractRef("strmprivacy/example/1.2.3")
                        .setConsentLevels(singletonList(1))
                        .build())
                .setUniqueIdentifier(UUID.randomUUID().toString())
                .setSomeSensitiveValue("A value that should be encrypted")
                .setConsistentValue("a-user-session")
                .setNotSensitiveValue("Hello from Java")
                .build();
    }

    @JsonIgnoreProperties({"schemaRef", "schema", "strmSchema"})
    public static class JsonSchemaDemoEvent implements StrmPrivacyEvent {

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            JsonSchemaDemoEvent that = (JsonSchemaDemoEvent) o;
            return Objects.equals(field, that.field);
        }

        @Override
        public int hashCode() {
            return Objects.hash(field);
        }

        public Integer field;

        @Override
        public String getSchemaRef() {
            return "does-not-matter";
        }

        @Override
        public Object getSchema() {
            return mapper.createObjectNode();
        }
    }
}
