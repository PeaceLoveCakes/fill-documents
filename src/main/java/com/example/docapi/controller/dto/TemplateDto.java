package com.example.docapi.controller.dto;

import com.example.docapi.db.entity.Template;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TemplateDto {

    private Long id;
    private Long authorId;
    private String name;
    private List<AttributeDto> attributes;

    public static TemplateDto from(Template template) {
        return new TemplateDto().setId(template.getId())
                .setName(template.getName())
                .setAuthorId(template.getAuthor() == null ? null : template.getAuthor().getId());
    }
}
