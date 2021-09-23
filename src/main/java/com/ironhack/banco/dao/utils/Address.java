package com.ironhack.banco.dao.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
@Embeddable
public class Address {

    @NotNull(message = "House number is required")
    private int houseNumber;

    @NotBlank(message = "Street name is required")
    private String streetName;

    @NotBlank(message = "Postcode is required")
    private String postcode;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "Country is required")
    private String country;

    public Address(int houseNumber, String streetName, String postcode, String city, String country) {
        this.houseNumber = houseNumber;
        this.streetName = streetName;
        this.postcode = postcode;
        this.city = city;
        this.country = country;
    }
}
