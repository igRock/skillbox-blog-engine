package ru.skillbox.blog_engine.dto;

public class AuthResponse {
    private boolean result;
    private UserAdditionalInfoDto user;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public UserAdditionalInfoDto getUser() {
        return user;
    }

    public void setUser(UserAdditionalInfoDto user) {
        this.user = user;
    }
}
