package com.intercorp.msone.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class RootDTO implements Serializable {

    private PersonDTO person;
    private int random;
    private float random_float;
    private boolean bool;
    private String date;
    private String regEx;
    private String enumValue;
    private String elt;
    private String last_update;
    private String last_modified;

}
