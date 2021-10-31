package com.example.docapi.model;

import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class DpsDocTemplate {

    private File sourceFile;
    private String name;
    private Map<String, String> tags_values;
    private Map<String, DpsTemplate> templates;

    public DpsDocTemplate(String templateName) {
        this.name = templateName;
        tags_values = new HashMap<>();
        templates = new HashMap<>();
    }

}
