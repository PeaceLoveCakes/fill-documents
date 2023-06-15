package com.example.docapi.controller;

import com.example.docapi.config.exception.BadRequestException;
import com.example.docapi.controller.dto.AttributeDto;
import com.example.docapi.controller.dto.TemplateDto;
import com.example.docapi.db.entity.Template;
import com.example.docapi.db.repository.AccountRepository;
import com.example.docapi.db.repository.AttributeRepository;
import com.example.docapi.db.repository.TemplateRepository;
import com.example.docapi.service.DocumentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/template")
public class MainController {

    private final DocumentsService documentsService;
    private final TemplateRepository templateRepository;
    private final AccountRepository accountRepository;
    private final AttributeRepository attributeRepository;

    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Long uploadTemplate(@RequestPart MultipartFile file,
                               @RequestParam(required = false) Long accountId) throws IOException {
        Template template = documentsService.toTemplate(file);
        if (accountId != null) {
            template.setAuthor(accountRepository.findById(accountId).orElse(null));
        }
        return templateRepository.save(template).getId();
    }

    @GetMapping("/list")
    public List<TemplateDto> documentsList() {
        return templateRepository.findAll().stream().map(TemplateDto::from).collect(Collectors.toList());
    }

    @GetMapping
    public TemplateDto getTemplate(@RequestParam Long templateId) {
        TemplateDto templateDto = TemplateDto.from(templateRepository.findById(templateId)
                .orElseThrow(() -> new BadRequestException("Template not found")));
        templateDto.setAttributes(
                attributeRepository.findAllByTemplateIdAndParentIdIsNull(templateId)
                        .stream().map(AttributeDto::from)
                        .collect(Collectors.toList()));
        return templateDto;
    }

    @PostMapping("/replaceContent")
    public ResponseEntity<?> replaceContent(@RequestBody TemplateDto templateDto) throws IOException {
        byte[] data = documentsService.fillTemplate(templateDto);
        ByteArrayResource resource = new ByteArrayResource(data);
        String contentType = "application/octet-stream";
        String headerValue = "attachment; filename=\"" + templateDto.getName() + "\"";
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                .body(resource);
    }
}
