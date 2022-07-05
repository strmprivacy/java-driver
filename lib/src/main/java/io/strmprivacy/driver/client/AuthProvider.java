package io.strmprivacy.driver.client;

class AuthProvider {
    private String access_token;
    private String refreshToken;
    private long expiresAt;

    // for jackson
    public AuthProvider() {

    }

    public AuthProvider(String access_token, String refreshToken, long expiresAt) {
        this.access_token = access_token;
        this.refreshToken = refreshToken;
        this.expiresAt = expiresAt;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public long getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(long expiresAt) {
        this.expiresAt = expiresAt;
    }
}
