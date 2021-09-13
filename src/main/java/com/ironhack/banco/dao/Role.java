package com.ironhack.banco.dao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Role {

    @Enumerated
    private Role name;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}