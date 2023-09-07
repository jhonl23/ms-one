package com.intercorp.msone.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class PersonDTO implements Serializable {

    private String firstname;
    private String lastname;
    private String city;
    private String country;
    private String firstname2;
    private String lastname2;
    private String email;

}


