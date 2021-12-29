package io.strmprivacy.driver.domain;


public class Config {
    private String gatewayScheme;
    private String gatewayHost;
    private String gatewayEndpoint;
    private int gatewayPort;

    private String egressScheme;
    private String egressWsScheme;
    private String egressHost;
    private String egressWsEndpoint;
    private String egressHealthEndpoint;

    private String stsScheme;
    private String stsHost;
    private String stsAuthEndpoint;
    private String stsRefreshEndpoint;

    private Config(Builder builder) {
        setGatewayScheme(builder.gatewayScheme);
        setGatewayHost(builder.gatewayHost);
        setGatewayEndpoint(builder.gatewayEndpoint);
        setGatewayPort(builder.gatewayPort);
        setEgressScheme(builder.egressScheme);
        setEgressWsScheme(builder.egressWsScheme);
        setEgressHost(builder.egressHost);
        setEgressWsEndpoint(builder.egressWsEndpoint);
        setEgressHealthEndpoint(builder.egressHealthEndpoint);
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

    public String getEgressScheme() {
        return egressScheme;
    }

    public void setEgressScheme(String egressScheme) {
        this.egressScheme = egressScheme;
    }

    public String getEgressWsScheme() {
        return egressWsScheme;
    }

    public void setEgressWsScheme(String egressWsScheme) {
        this.egressWsScheme = egressWsScheme;
    }

    public String getEgressHost() {
        return egressHost;
    }

    public void setEgressHost(String egressHost) {
        this.egressHost = egressHost;
    }

    public String getEgressWsEndpoint() {
        return egressWsEndpoint;
    }

    public void setEgressWsEndpoint(String egressWsEndpoint) {
        this.egressWsEndpoint = egressWsEndpoint;
    }

    public String getEgressHealthEndpoint() {
        return egressHealthEndpoint;
    }

    public void setEgressHealthEndpoint(String egressHealthEndpoint) {
        this.egressHealthEndpoint = egressHealthEndpoint;
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

        private String egressScheme = "https";
        private String egressWsScheme = "wss";
        private String egressHost = "websocket.strmprivacy.io";
        private String egressWsEndpoint = "/ws";
        private String egressHealthEndpoint = "/is-alive";

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

        public Builder egressScheme(String val) {
            egressScheme = val;
            return this;
        }

        public Builder egressWsScheme(String val) {
            egressWsScheme = val;
            return this;
        }

        public Builder egressHost(String val) {
            egressHost = val;
            return this;
        }

        public Builder egressWsEndpoint(String val) {
            egressWsEndpoint = val;
            return this;
        }

        public Builder egressHealthEndpoint(String val) {
            egressHealthEndpoint = val;
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
