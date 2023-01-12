package com.example.homeservicespringmvc.service;


import com.example.homeservicespringmvc.entity.capability.Token;

public interface TokenService {

    void saveToken(Token token);

    Token getToken(String tokenValue);
    void setConfirmationAtToken(Token token);

}

