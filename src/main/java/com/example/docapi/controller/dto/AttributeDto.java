package com.example.docapi.controller.dto;

import com.example.docapi.db.entity.Attribute;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class AttributeDto {

    String name;
    String value;
    List<AttributeDto> childes;

    public static AttributeDto from(Attribute attribute) {
        return new AttributeDto()
                .setName(attribute.getName())
                .setChildes(attribute.getChildes()
                        .stream().map(AttributeDto::from)
                        .collect(Collectors.toList()));
    }
}
