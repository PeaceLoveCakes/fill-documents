package com.example.docapi.controller;

import com.example.docapi.controller.dto.AccountDto;
import com.example.docapi.controller.dto.AuthenticatedUserDto;
import com.example.docapi.controller.dto.CredentialsDto;
import com.example.docapi.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/auth")
public class AuthenticationController {

    private final AccountService accountService;

    @PostMapping("/login")
    public AuthenticatedUserDto login(@RequestBody CredentialsDto credentialsDto) {
        return new AuthenticatedUserDto()
                .setAccount(AccountDto.from(accountService.findByUsername(credentialsDto.getUsername())))
                .setToken(accountService.authenticate(credentialsDto));
    }

    @PostMapping("/registration")
    public AccountDto registration(@RequestBody CredentialsDto credentialsDto){
        return AccountDto.from(accountService.register(credentialsDto));
    }
}
