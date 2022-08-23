package br.com.coelho.helper;

import org.springframework.http.HttpHeaders;

public class AuthHelper {
    public static HttpHeaders getHeaderAuth() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-API-KEY", System.getenv("X_API_KEY_BACK"));
        return headers;
    }
}
