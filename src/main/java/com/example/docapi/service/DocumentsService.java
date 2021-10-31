package com.example.docapi.service;

import com.example.documentsparser.DocService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DocumentsService {

    private final DocService docService = new DocService();

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

}
