package com.example.docapi.service;

import com.example.docapi.config.exception.BadRequestException;
import com.example.docapi.config.security.jwt.JwtTokenProvider;
import com.example.docapi.controller.dto.CredentialsDto;
import com.example.docapi.db.entity.Account;
import com.example.docapi.db.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;


    public Account register(CredentialsDto credentialsDto) {
        return register(new Account()
                .setUsername(credentialsDto.getUsername())
                .setPassword(credentialsDto.getPassword())
                .setNickname(credentialsDto.getFullName()));
    }

    public Account register(Account account) {
        if(accountRepository.existsByUsername(account.getUsername())) {
            throw new BadRequestException("Username is already taken");
        }
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        return accountRepository.save(account);
    }

    public Account findByUsername(String username) {
        return accountRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    public Optional<Account> findById(Long id) {
        return Optional.of(accountRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found")));
    }

    public List<Account> getAllUsers() {
        return accountRepository.findAll();
    }

    public void delete(Long id) {
        accountRepository.deleteById(id);
    }


    public String authenticate(CredentialsDto credentialsDto){
        try {
            String username = credentialsDto.getUsername();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, credentialsDto.getPassword()));
            Account user = findByUsername(username);

            return jwtTokenProvider.createToken(username, user.getRoles());

        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

}
