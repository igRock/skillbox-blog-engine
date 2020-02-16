package ru.skillbox.blog_engine.dto;

public class ResultResponse {
    private boolean result;
    private Error errors;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public Error getErrors() {
        return errors;
    }

    public void setErrors(Error errors) {
        this.errors = errors;
    }
}
