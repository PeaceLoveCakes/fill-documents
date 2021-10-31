package com.example.docapi.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(of = {"code"})
public class DpsAttribute {

    private String code;
    private String value;

}
