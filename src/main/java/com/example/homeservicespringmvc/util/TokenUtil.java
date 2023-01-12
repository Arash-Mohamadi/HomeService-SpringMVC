package com.example.homeservicespringmvc.util;

import com.example.homeservicespringmvc.entity.capability.Token;
import com.example.homeservicespringmvc.entity.users.Customer;
import com.example.homeservicespringmvc.entity.users.Manager;
import com.example.homeservicespringmvc.entity.users.Specialist;
import com.example.homeservicespringmvc.exception.CustomizedEmailException;
import com.example.homeservicespringmvc.service.TokenService;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
public class TokenUtil {
    private static  TokenService tokenService;

    public static String generate(Customer customer, Specialist specialist, Manager manager){
        String tokenValue = UUID.randomUUID().toString();
        Token token = new Token(
                tokenValue,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(5),
                customer,
                specialist,
                manager);
        tokenService.saveToken(token);
        return tokenValue ;
    }

    public static void setConfirmationAtToken(Token token) {
        if (token.getConfirmationAt() != null) {
            throw new CustomizedEmailException("email already confirmed");
        }
        if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new CustomizedEmailException("token expired");
        }
        tokenService.setConfirmationAtToken(token);
    }


}
