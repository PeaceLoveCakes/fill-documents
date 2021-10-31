package com.example.docapi.config.security.jwt;

import com.example.docapi.db.entity.Account;
import com.example.docapi.db.entity.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

public final class JwtUserFactory {

    public JwtUserFactory() {
    }

    public static JwtUser create(Account account) {
        return new JwtUser(
                account.getId(),
                account.getUsername(),
                account.getPassword(),
                mapToGrantedAuthorities(account.getRoles()));
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(List<Role> userRoles) {
        return userRoles.stream()
                .map(role ->
                        new SimpleGrantedAuthority(role.getName())
                ).collect(Collectors.toList());
    }
}
