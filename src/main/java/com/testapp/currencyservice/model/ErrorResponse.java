package com.testapp.currencyservice.model;

import lombok.Data;

@Data
public class ErrorResponse {
    public ErrorResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    private String errorMessage;
}
