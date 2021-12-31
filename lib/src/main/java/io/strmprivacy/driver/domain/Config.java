package io.strmprivacy.driver.domain;


public class Config {
    private String gatewayScheme;
    private String gatewayHost;
    private String gatewayEndpoint;
    private int gatewayPort;

    private String stsScheme;
    private String stsHost;
    private String stsAuthEndpoint;
    private String stsRefreshEndpoint;

    private Config(Builder builder) {
        setGatewayScheme(builder.gatewayScheme);
        setGatewayHost(builder.gatewayHost);
        setGatewayEndpoint(builder.gatewayEndpoint);
        setGatewayPort(builder.gatewayPort);
        setStsScheme(builder.stsScheme);
        setStsHost(builder.stsHost);
        setStsAuthEndpoint(builder.stsAuthEndpoint);
        setStsRefreshEndpoint(builder.stsRefreshEndpoint);
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

    public String getStsScheme() {
        return stsScheme;
    }

    public void setStsScheme(String stsScheme) {
        this.stsScheme = stsScheme;
    }

    public String getStsHost() {
        return stsHost;
    }

    public void setStsHost(String stsHost) {
        this.stsHost = stsHost;
    }

    public String getStsAuthEndpoint() {
        return stsAuthEndpoint;
    }

    public void setStsAuthEndpoint(String stsAuthEndpoint) {
        this.stsAuthEndpoint = stsAuthEndpoint;
    }

    public String getStsRefreshEndpoint() {
        return stsRefreshEndpoint;
    }

    public void setStsRefreshEndpoint(String stsRefreshEndpoint) {
        this.stsRefreshEndpoint = stsRefreshEndpoint;
    }

    public static final class Builder {
        private String gatewayScheme = "https";
        private String gatewayHost = "events.strmprivacy.io";
        private String gatewayEndpoint = "/event";
        private int gatewayPort = 443;

        private String stsScheme = "https";
        private String stsHost = "sts.strmprivacy.io";
        private String stsAuthEndpoint = "/auth";
        private String stsRefreshEndpoint = "/refresh";

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

        public Builder gatewayEndpoint(String val) {
            gatewayEndpoint = val;
            return this;
        }

        public Builder gatewayPort(int val) {
            gatewayPort = val;
            return this;
        }

        public Builder stsScheme(String val) {
            stsScheme = val;
            return this;
        }

        public Builder stsHost(String val) {
            stsHost = val;
            return this;
        }

        public Builder stsAuthEndpoint(String val) {
            stsAuthEndpoint = val;
            return this;
        }

        public Builder stsRefreshEndpoint(String val) {
            stsRefreshEndpoint = val;
            return this;
        }

        public Config build() {
            return new Config(this);
        }
    }
}
