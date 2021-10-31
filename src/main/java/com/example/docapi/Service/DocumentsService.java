package com.example.docapi.Service;

import com.example.documentsparser.Documents.DocService;
import com.example.documentsparser.Documents.model.DocTemplateForDocParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentsService {

    private final DocService docService;

    public final List<String> documentsList(){
        File templatesDir = docService.getTEMPLATES_FOLDER();
        return Arrays.asList(templatesDir.list());
    }

    public DocTemplateForDocParser getTemplate(String templateName){
        return docService.getTemplate(templateName);
    }

    public void parseDoc(DocTemplateForDocParser docTemplate, String resultName){
        docService.parseDocument(docTemplate, resultName);
    }

}
