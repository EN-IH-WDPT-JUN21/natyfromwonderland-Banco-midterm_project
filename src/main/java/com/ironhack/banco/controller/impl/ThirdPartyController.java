package com.ironhack.banco.controller.impl;

import com.ironhack.banco.controller.interfaces.IThirdPartyController;
import com.ironhack.banco.dao.ThirdParty;
import com.ironhack.banco.dao.Transaction;
import com.ironhack.banco.dto.TransactionDTO;
import com.ironhack.banco.repository.ThirdPartyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class ThirdPartyController implements IThirdPartyController {

    @Autowired
    private ThirdPartyRepository thirdPartyRepository;

    //Route to create a third party
    @PostMapping("/thirdparty")
    @ResponseStatus(HttpStatus.CREATED)
    public ThirdParty addParty(@RequestBody @Valid ThirdParty thirdParty){
        return thirdPartyRepository.save(thirdParty);
    }

    @PostMapping("/thirdparty/sendmoney")
    public void sendMoney(@RequestParam String hashedKey, @RequestBody TransactionDTO transactionDTO){
        if(thirdPartyRepository.findByHashedKey(hashedKey).isPresent()){

        }
    }
}
