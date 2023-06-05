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
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DocumentsService {

    private final File ROOT_DIR = new File(System.getProperty("user.dir") + "/documents");
    private final File TMP_DIR = new File(ROOT_DIR + "/templates");
    private final TemplateRepository templateRepository;

    @Autowired
    DocumentsService(TemplateRepository templateRepository) {
        this.templateRepository = templateRepository;
        ROOT_DIR.mkdir();
        TMP_DIR.mkdir();
    }

    public Template toTemplate(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        File tmp = TempFile.createFromInputStream(file::getInputStream, fileName);

        Template template = new Template()
                .setName(fileName)
                .setType(file.getContentType())
                .setData(file.getBytes());
        Set<AttributeDto> attributes = ParsingLogic.findAttributes(tmp);
        template.setAttributes(new HashSet<>());
        template.getAttributes().addAll(attributes.stream()
                .map(attribute -> attributeFromDto(attribute, template)
                ).collect(Collectors.toSet()));
        return template;
    }

    private Attribute attributeFromDto(AttributeDto attributeDto, Template template) {
        Attribute attribute = new Attribute()
                .setName(attributeDto.getName())
                .setTemplate(template);
        if (attributeDto.getChildes() == null || attributeDto.getChildes().isEmpty()) {
            return attribute;
        }
        for (AttributeDto childDto : attributeDto.getChildes()) {
            Attribute child = attributeFromDto(childDto, template);
            template.getAttributes().add(child);
            child.setParent(attribute);
        }
        return attribute;
    }

    public byte[] fillTemplate(TemplateDto templateDto) throws IOException {
        Template template = templateRepository.getById(templateDto.getId());
        byte[] content = templateRepository.getDataById(template.getId());
        File input = TempFile.createFromInputStream(() -> new ByteArrayInputStream(content),
                templateDto.getName().substring(templateDto.getName().lastIndexOf('.')));
        File output = TempFile.createFromString("", templateDto.getName().substring(templateDto.getName().lastIndexOf('.')));
        Map<String, String> replaces = ParsingLogic.attributeDtoToMap(templateDto.getAttributes());
        ParsingLogic.replaceContent(input, output, replaces);
        return Files.readAllBytes(output.toPath());
    }

//    public File getOutputFile(String fileName){
//        return new File(docService.getOUTPUT_FOLDER() + "/" + fileName);
//    }
//
//    public final List<String> templatesList(){
//        File templatesDir = docService.getTEMPLATES_FOLDER();
//        return Arrays.asList(templatesDir.list());
//    }
//
//    public final List<String> outputList(){
//        File output = docService.getOUTPUT_FOLDER();
//        return Arrays.asList(output.list());
//    }
//
//    public DpsDocTemplate getTemplate(String templateName){
//        return docService.getTemplate(templateName);
//    }
//
//    public void parseDoc(DpsDocTemplate docTemplate, String resultName){
//        docService.parseDocument(docTemplate, resultName);
//    }

//    @Getter private final File DOCS_DIR;
//    @Getter private final File TEMPLATES_FOLDER;
//    @Getter private final File OUTPUT_FOLDER;
//
//    public DocService(String rootDir) {
//        DOCS_DIR = new File(rootDir);
//        TEMPLATES_FOLDER = new File(DOCS_DIR + "/templates");
//        OUTPUT_FOLDER = new File(DOCS_DIR + "/output");
//        DOCS_DIR.mkdir();
//        TEMPLATES_FOLDER.mkdir();
//        OUTPUT_FOLDER.mkdir();
//    }
//
//    public DpsDocTemplate getTemplate(String templateName) {
//        DpsDocTemplate dpsDocTemplate = new DpsDocTemplate(templateName);
//        dpsDocTemplate.setSourceFile(getTemplateFile(templateName));
//        readTemplate(dpsDocTemplate);
//        return dpsDocTemplate;
//    }
//
//    public void parseDocument(DpsDocTemplate dpsDocTemplate, String resultName) {
//        dpsDocTemplate.setSourceFile(getTemplateFile(dpsDocTemplate.getName()));
//        DocParser parser = getFileParser(dpsDocTemplate.getSourceFile());
//
//        parser.parseDocument(dpsDocTemplate,
//                createOutputFile(resultName + getFileType(dpsDocTemplate.getSourceFile())));
//    }
//
//    private void readTemplate(DpsDocTemplate dpsDocTemplate) {
//        DocParser parser = getFileParser(dpsDocTemplate.getSourceFile());
//        if (parser != null) parser.initTemplate(dpsDocTemplate);
//    }
//
//    private File getTemplateFile(String templateName) {
//        return new File(TEMPLATES_FOLDER + "/" + templateName);
//    }
//
//    private File createOutputFile(String fileName) {
//        File output = new File(OUTPUT_FOLDER + "/" + fileName);
//        try {
//            output.createNewFile();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return output;
//    }
//
//    private String getFileType(File file) {
//        if (file.isDirectory()) return "directory";
//
//        if (file.getName().contains(".xlsx")) {
//            return ".xlsx";
//        } else if (file.getName().contains(".txt")) {
//            return ".txt";
//        } else if (file.getName().contains(".docx")) {
//            return ".docx";
//        }
//
//        return "";
//    }
//
//    private DocParser getFileParser(File file) {
//        if (file.isDirectory()) return null;
//
//        if (file.getName().contains(".xlsx")) {
//            return xlsxParser;
//        } else if (file.getName().contains(".txt")) {
//            return txtParser;
//        } else if (file.getName().contains(".docx")) {
//            return docxParser;
//        }
//
//        return null;
//    }

}
