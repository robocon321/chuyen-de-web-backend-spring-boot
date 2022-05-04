package com.robocon321.demo.domain;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class LoginRequestDomain {
    @NotBlank
    private String username;

    @NotBlank
    private String password;
}