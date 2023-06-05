package com.example.docapi.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticatedUserDto {

    private AccountDto account;
    private String token;

}
