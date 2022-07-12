package io.strmprivacy.driver.domain;


public class Config {
    private String gatewayScheme;
    private String gatewayHost;
    private String gatewayEndpoint;
    private int gatewayPort;
    private String authScheme;
    private String authHost;
    private String authEndpoint;

    private Config(Builder builder) {
        setGatewayScheme(builder.gatewayScheme);
        setGatewayHost(builder.gatewayHost);
        setGatewayEndpoint(builder.gatewayEndpoint);
        setGatewayPort(builder.gatewayPort);
        setAuthScheme(builder.authScheme);
        setAuthHost(builder.authHost);
        setAuthEndpoint(builder.authEndpoint);
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

    public void setAuthScheme(String authScheme) {
        this.authScheme = authScheme;
    }

    public String getAuthScheme() {
        return authScheme;
    }

    public void setAuthHost(String authHost) {
        this.authHost = authHost;
    }

    public String getAuthHost() {
        return authHost;
    }

    public void setAuthEndpoint(String authEndpoint) {
        this.authEndpoint = authEndpoint;
    }

    public String getAuthEndpoint() {
        return authEndpoint;
    }


    public static final class Builder {
        private String gatewayScheme = "https";
        private String gatewayHost = "events.strmprivacy.io";
        private String gatewayEndpoint = "/event";
        private int gatewayPort = 443;
        private String authScheme = "https";
        private String authHost = "accounts.strmprivacy.io";
        private String authEndpoint = "/auth/realms/streams/protocol/openid-connect/token";

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


        public Builder authScheme(String val) {
            authScheme = val;
            return this;
        }

        public Builder authHost(String val) {
            authHost = val;
            return this;
        }

        public Builder authEndpoint(String val) {
            authEndpoint = val;
            return this;
        }

        public Config build() {
            return new Config(this);
        }
    }
}
