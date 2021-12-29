package io.strmprivacy.driver.serializer;

import io.strmprivacy.schemas.demo.v1.DemoEvent;
import io.strmprivacy.schemas.demo.v1.StrmMeta;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.UUID;

import static java.util.Collections.singletonList;

public class AvroSerializerTest {
    private static final Logger log = LoggerFactory.getLogger(AvroSerializerTest.class);

    @Test
    void serialize() throws IOException {

        DemoEvent event = demoEvent();
        EventSerializer serializer = SerializerProvider.getSerializer(event.getSchemaRef(), event.getSchema());
        byte[] bytes = serializer.serialize(event, SerializationType.AVRO_BINARY);
        log.debug("bytes={}", bytes);
        assert(bytes.length>0);
    }

    public static DemoEvent demoEvent() {
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
}
