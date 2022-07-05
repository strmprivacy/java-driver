package io.strmprivacy.driver.domain;


public class Config {
    private String gatewayScheme;
    private String gatewayHost;
    private String gatewayEndpoint;
    private int gatewayPort;
    private String keycloakScheme;
    private String keycloakHost;
    private String keycloakEndpoint;

    private Config(Builder builder) {
        setGatewayScheme(builder.gatewayScheme);
        setGatewayHost(builder.gatewayHost);
        setGatewayEndpoint(builder.gatewayEndpoint);
        setGatewayPort(builder.gatewayPort);
        setKeycloakScheme(builder.keycloakScheme);
        setKeycloakHost(builder.keycloakHost);
        setKeycloakEndpoint(builder.keycloakEndpoint);
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getImplementationVersion() {
        String version = this.getClass().getPackage().getImplementationVersion();

        return version != null ? version : "development";
    }

    public String getGatewayScheme() {
        return gatewayScheme;
    }

    public void setGatewayScheme(String gatewayScheme) {
        this.gatewayScheme = gatewayScheme;
    }

    public String getGatewayHost() {
        return gatewayHost;
    }

    public void setGatewayHost(String gatewayHost) {
        this.gatewayHost = gatewayHost;
    }

    public String getGatewayEndpoint() {
        return gatewayEndpoint;
    }

    public void setGatewayEndpoint(String gatewayEndpoint) {
        this.gatewayEndpoint = gatewayEndpoint;
    }

    public int getGatewayPort() {
        return gatewayPort;
    }

    public void setGatewayPort(int gatewayPort) {
        this.gatewayPort = gatewayPort;
    }

    public void setKeycloakScheme(String keycloakScheme) {
        this.keycloakScheme = keycloakScheme;
    }

    public String getKeycloakScheme() {
        return keycloakScheme;
    }

    public void setKeycloakHost(String keycloakHost) {
        this.keycloakHost = keycloakHost;
    }

    public String getKeycloakHost() {
        return keycloakHost;
    }

    public void setKeycloakEndpoint(String keycloakEndpoint) {
        this.keycloakEndpoint = keycloakEndpoint;
    }

    public String getKeycloakEndpoint() {
        return keycloakEndpoint;
    }


    public static final class Builder {
        private String gatewayScheme = "https";
        private String gatewayHost = "events.strmprivacy.io";
        private String gatewayEndpoint = "/event";
        private int gatewayPort = 443;
        private String keycloakScheme = "https";
        private String keycloakHost = "accounts.strmprivacy.io";
        private String keycloakEndpoint = "/auth/realms/streams/protocol/openid-connect/token";

        private Builder() {
        }

        public Builder gatewayScheme(String val) {
            gatewayScheme = val;
            return this;
        }

        public Builder gatewayHost(String val) {
            gatewayHost = val;
            return this;
        }

        public Builder gatewayPort(int val) {
            gatewayPort = val;
            return this;
        }


        public Builder keycloakScheme(String val) {
            keycloakScheme = val;
            return this;
        }

        public Builder keycloakHost(String val) {
            keycloakHost = val;
            return this;
        }

        public Builder keycloakEndpoint(String val) {
            keycloakEndpoint = val;
            return this;
        }

        public Config build() {
            return new Config(this);
        }
    }
}
