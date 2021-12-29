package io.strmprivacy.driver.serializer;

import java.io.IOException;

public interface EventSerializer {
    byte[] serialize(Object event) throws IOException;
}
