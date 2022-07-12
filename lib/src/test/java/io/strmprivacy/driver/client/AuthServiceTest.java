package io.strmprivacy.driver.client;

import io.strmprivacy.driver.domain.Config;
import io.strmprivacy.driver.serializer.EventSerializer;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertNotNull;


class AuthServiceTest {
    private static final Logger log = LoggerFactory.getLogger(EventSerializer.class);

    @Test
    @Disabled
    void tokenTest () {
        Config config = getTestConfig();
        AuthService authService = new AuthService("clientId", "clientSecret", config);
        String accessToken = authService.getAccessToken();
        assertNotNull(accessToken);
    }

    private Config getTestConfig() {
        return Config.builder()
                .gatewayHost("events.dev.strmprivacy.io")
                .gatewayPort(443)
                .gatewayScheme("http")
                .authScheme("https")
                .authHost("accounts.dev.strmprivacy.io")
                .authEndpoint("/auth/realms/streams/protocol/openid-connect/token")
                .build();
    }
}
