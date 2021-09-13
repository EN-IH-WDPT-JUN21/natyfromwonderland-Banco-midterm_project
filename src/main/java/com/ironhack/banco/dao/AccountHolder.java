package com.ironhack.banco.dao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.Optional;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class AccountHolder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotBlank(message = "Name cannot be blank")
    private Date dateOfBirth;

    @AttributeOverrides({
            @AttributeOverride(name="houseNumber",column=@Column(name="current_house_number")),
            @AttributeOverride(name="streetName",column=@Column(name="current_street_name")),
            @AttributeOverride(name="postcode",column=@Column(name="current_postcode")),
            @AttributeOverride(name="city",column=@Column(name="current_city")),
            @AttributeOverride(name="country",column=@Column(name="current_country"))
    })

    @Valid
    @Embedded
    private Address primaryAddress;

    @AttributeOverrides({
            @AttributeOverride(name="houseNumber",column=@Column(name="mail_house_number")),
            @AttributeOverride(name="streetName",column=@Column(name="mail_street_name")),
            @AttributeOverride(name="postcode",column=@Column(name="mail_postcode")),
            @AttributeOverride(name="city",column=@Column(name="mail_city")),
            @AttributeOverride(name="country",column=@Column(name="mail_country"))
    })

    @Valid
    @Embedded
    private Optional<Address> mailingAddress;


}
