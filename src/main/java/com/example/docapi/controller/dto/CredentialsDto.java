package com.example.docapi.controller.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CredentialsDto {

    @NotNull
    private String username;
    @NotNull
    private String password;
    private String fullName;

}
