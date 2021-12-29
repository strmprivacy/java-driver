package io.strmprivacy.driver.client;

class AuthProvider {
    private String idToken;
    private String refreshToken;
    private long expiresAt;

    // for jackson
    public AuthProvider() {

    }
    public AuthProvider(String idToken, String refreshToken, long expiresAt) {
        this.idToken = idToken;
        this.refreshToken = refreshToken;
        this.expiresAt = expiresAt;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
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
