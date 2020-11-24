package io.streammachine.driver.serializer;

import java.io.IOException;

public interface EventSerializer {
    byte[] serialize(Object event, SerializationType serializationType) throws IOException;
}
