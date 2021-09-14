package com.ironhack.banco.dao;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@PrimaryKeyJoinColumn(name = "id")
public class AccountHolder extends User{

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotBlank(message = "Date of birth cannot be blank")
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
    private Address mailingAddress;

    public Optional getMailingAddress() {
        return Optional.ofNullable(this.mailingAddress);
    }

    public void setMailingAddress(final Address mailingAddress) {
        this.mailingAddress = mailingAddress;
    }


   @JsonManagedReference
    @OneToMany(mappedBy = "primaryOwner")
    private List<Account> accountList;

    @JsonManagedReference
    @OneToMany(mappedBy = "secondaryOwner")
    private List<Account> accountListTwo;

}
