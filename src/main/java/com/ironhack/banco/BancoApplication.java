package com.ironhack.banco;

import com.ironhack.banco.dao.accounts.BusinessLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BancoApplication {

	@Autowired
	BusinessLogic businessLogic;

	public static void main(String[] args) {
		System.setProperty("java.awt.headless", "false");
		SpringApplication.run(BancoApplication.class, args);
	}

}
