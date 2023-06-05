package com.example.docapi.controller.dto;

import com.example.docapi.db.entity.Account;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class AccountDto {

    private Long id;
    private String username;
    private String nickname;
    private List<RoleDto> roles;

    public static AccountDto from(Account account){
        return new AccountDto()
                .setId(account.getId())
                .setUsername(account.getUsername())
                .setNickname(account.getNickname())
                .setRoles(account.getRoles() != null
                        ? account.getRoles().stream().map(RoleDto::from).collect(Collectors.toList())
                        : null);
    }

}
