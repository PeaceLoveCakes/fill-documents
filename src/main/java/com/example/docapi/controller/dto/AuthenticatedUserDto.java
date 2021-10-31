package com.example.docapi.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticatedUserDto {

    private AccountDto user;
    private String token;

}
