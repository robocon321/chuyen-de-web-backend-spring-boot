package com.robocon321.demo.domain;

import lombok.Data;

@Data
public class LoginResponseDomain {
    private String accessToken;
    private String tokenType = "Bearer";

    public LoginResponseDomain(String accessToken) {
        this.accessToken = accessToken;
    }
}