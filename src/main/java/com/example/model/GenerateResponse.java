package com.example.webhooksolver.model;

import lombok.Data;

@Data
public class GenerateResponse {
    private String webhook;
    private String accessToken;
    private DataWrapper data;
}
