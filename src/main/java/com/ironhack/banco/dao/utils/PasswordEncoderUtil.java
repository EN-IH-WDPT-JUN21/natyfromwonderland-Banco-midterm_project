package com.ironhack.banco.dao.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordEncoderUtil {
    public static void main(String[] args) throws JsonProcessingException {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


        System.out.println(passwordEncoder.encode("123456"));
        System.out.println(passwordEncoder.encode("c@tch22"));
        System.out.println(passwordEncoder.encode("test@torsilens21"));


    }
}
