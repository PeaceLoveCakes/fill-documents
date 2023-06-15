package com.example.docapi.service;

import com.antkorwin.ioutils.TempFile;
import com.example.docapi.controller.dto.AttributeDto;
import com.example.docapi.controller.dto.TemplateDto;
import com.example.docapi.db.entity.Attribute;
import com.example.docapi.db.entity.Template;
import com.example.docapi.db.repository.TemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class DocumentsService {

    private final TemplateRepository templateRepository;

    @Autowired
    DocumentsService(TemplateRepository templateRepository) {
        this.templateRepository = templateRepository;
    }

    public Template toTemplate(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        File tmp = TempFile.createFromInputStream(file::getInputStream, fileName);

        Template template = new Template()
                .setName(fileName)
                .setType(file.getContentType())
                .setData(file.getBytes());
        List<AttributeDto> attributes = ParsingLogic.findAttributes(tmp);
        template.setAttributes(new HashSet<>());
        template.getAttributes().addAll(attributes.stream()
                .map(attribute -> attributeFromDto(attribute, template)
                ).collect(Collectors.toSet()));
        return template;
    }

    private Attribute attributeFromDto(AttributeDto attributeDto, Template template) {
        Attribute attribute = new Attribute()
                .setName(attributeDto.getName())
                .setTemplate(template)
                .setChildes(new ArrayList<>());
        if (attributeDto.getChildes() == null || attributeDto.getChildes().isEmpty()) {
            return attribute;
        }
        for (AttributeDto childDto : attributeDto.getChildes()) {
            Attribute child = attributeFromDto(childDto, template);
            child.setTemplate(template);
            attribute.getChildes().add(child);
        }
        return attribute;
    }

    private File getAsFile(TemplateDto templateDto){
        Template template = templateRepository.getById(templateDto.getId());
        byte[] content = templateRepository.getDataById(template.getId());
        return TempFile.createFromInputStream(() -> new ByteArrayInputStream(content),
                templateDto.getName().substring(templateDto.getName().lastIndexOf('.')));
    }

    public byte[] fillTemplate(TemplateDto templateDto) throws IOException {
        File input = getAsFile(templateDto);
        File output = TempFile.createFromString("", templateDto.getName().substring(templateDto.getName().lastIndexOf('.')));
        Map<String, String> replaces = ParsingLogic.attributeDtoToMap(templateDto.getAttributes());
        ParsingLogic.replaceContent(input, output, replaces);
        return Files.readAllBytes(output.toPath());
    }

}
