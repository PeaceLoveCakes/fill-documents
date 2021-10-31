package com.example.docapi.controller.dto;

import com.example.docapi.db.entity.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleDto {

    private Long id;
    private String name;

    public static RoleDto from(Role role) {
        return new RoleDto()
                .setId(role.getId())
                .setName(role.getName());
    }
}
