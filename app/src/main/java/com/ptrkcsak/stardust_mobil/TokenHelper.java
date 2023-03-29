package com.ptrkcsak.stardust_mobil;

public class TokenHelper {
    String access_token;
    public TokenHelper(String token) {
        this.access_token = token;
    }
    public String getToken() {
        return access_token;
    }
    public void setToken(String token) {
        this.access_token = token;
    }
}
