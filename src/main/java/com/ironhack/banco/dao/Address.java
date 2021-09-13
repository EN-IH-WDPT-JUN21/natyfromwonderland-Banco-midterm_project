package com.ironhack.banco.dao;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Embeddable
public class Address {

    @NotBlank(message = "House number is required")
    private int houseNumber;

    @NotBlank(message = "Street name is required")
    @Pattern(regexp = "\\w+\\.?", message = "The street name is invalid.")
    private String streetName;

    @NotBlank(message = "Postcode is required")
    @Pattern(regexp = "[a-zA-Z0-9]", message = "The postcode name is invalid.")
    private String postcode;

    @NotBlank(message = "City is required")
    @Pattern(regexp = "\\w+\\.?", message = "The city name is invalid.")
    private String city;

    @NotBlank(message = "Country is required")
    @Pattern(regexp = "\\w+\\.?", message = "The country name is invalid.")
    private String country;

}
