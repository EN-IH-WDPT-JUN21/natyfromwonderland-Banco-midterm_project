package com.ironhack.banco.controller.impl;

import com.ironhack.banco.controller.interfaces.IThirdPartyController;
import com.ironhack.banco.dao.ThirdParty;
import com.ironhack.banco.repository.ThirdPartyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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
}
