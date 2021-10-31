package com.example.docapi;

import com.example.documentsparser.Documents.DocService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    private final String DOCS_DIR = System.getProperty("user.dir") + "/Documents";

    @Bean
    public DocService docService(){
        return new DocService(DOCS_DIR);
    }

}
