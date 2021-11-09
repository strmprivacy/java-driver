package io.strmprivacy.driver.common;

import org.eclipse.jetty.websocket.api.WebSocketAdapter;

import java.util.concurrent.CountDownLatch;

public class WebSocketConsumer extends WebSocketAdapter {
    private final CountDownLatch closureLatch = new CountDownLatch(1);

    public void awaitClosure() throws InterruptedException {
        closureLatch.await();
    }
}
