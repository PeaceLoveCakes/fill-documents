package com.example.docapi.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class DpsTemplate {

    private String name;
    private String description;
    private List<DpsAttribute> attributesForDocParser = new ArrayList<>();

    public DpsTemplate addAttribute(DpsAttribute dpsAttribute) {
        attributesForDocParser.add(dpsAttribute);
        return this;
    }
}
