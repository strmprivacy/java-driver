package io.strmprivacy.driver.client;

import io.strmprivacy.driver.domain.Config;

public class ClientBuilder {
    private String billingId;
    private String clientId;
    private String clientSecret;
    private Config config;

    public ClientBuilder setBillingId(String billingId) {
        this.billingId = billingId;
        return this;
    }

    public ClientBuilder setClientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public ClientBuilder setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
        return this;
    }

    public ClientBuilder setConfig(Config config) {
        this.config = config;
        return this;
    }

    public StrmPrivacyClient createStrmPrivacyClient() {
        return new StrmPrivacyClient(billingId, clientId, clientSecret, config);
    }
}
