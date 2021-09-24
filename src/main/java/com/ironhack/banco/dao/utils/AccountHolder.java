package com.ironhack.banco.dao.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ironhack.banco.dao.accounts.Account;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@PrimaryKeyJoinColumn(name = "id")
public class AccountHolder extends User{

    private String name;
    private Date dateOfBirth;

    @AttributeOverrides({
            @AttributeOverride(name="houseNumber",column=@Column(name="current_house_number")),
            @AttributeOverride(name="streetName",column=@Column(name="current_street_name")),
            @AttributeOverride(name="postcode",column=@Column(name="current_postcode")),
            @AttributeOverride(name="city",column=@Column(name="current_city")),
            @AttributeOverride(name="country",column=@Column(name="current_country"))
    })

    @Embedded
    @Valid
    private Address primaryAddress;

    @AttributeOverrides({
            @AttributeOverride(name="houseNumber",column=@Column(name="mail_house_number")),
            @AttributeOverride(name="streetName",column=@Column(name="mail_street_name")),
            @AttributeOverride(name="postcode",column=@Column(name="mail_postcode")),
            @AttributeOverride(name="city",column=@Column(name="mail_city")),
            @AttributeOverride(name="country",column=@Column(name="mail_country"))
    })

    @Embedded
    @Valid
    private Address mailingAddress;


    public Optional getMailingAddress() {
        return Optional.ofNullable(this.mailingAddress);
    }

    public void setMailingAddress(final Address mailingAddress) {
        this.mailingAddress = mailingAddress;
    }


    @OneToMany(mappedBy = "primaryOwner", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Account> accountList;

    @OneToMany(mappedBy = "secondaryOwner", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Account> accountListTwo;

    public AccountHolder(String name, Date dateOfBirth, Address primaryAddress) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.primaryAddress = primaryAddress;
    }

}
