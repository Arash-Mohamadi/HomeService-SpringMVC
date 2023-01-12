package com.example.homeservicespringmvc.util;

import com.example.homeservicespringmvc.entity.capability.Token;
import com.example.homeservicespringmvc.entity.users.Customer;
import com.example.homeservicespringmvc.entity.users.Manager;
import com.example.homeservicespringmvc.entity.users.Specialist;
import com.example.homeservicespringmvc.exception.CustomizedEmailException;
import com.example.homeservicespringmvc.repository.TokenRepository;
import com.example.homeservicespringmvc.service.TokenService;
import com.example.homeservicespringmvc.service.impl.TokenServiceImpl;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.UUID;


@Configuration

public class TokenUtil {

    private final TokenService tokenService;

    public TokenUtil(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    public static TokenUtil getTokenUtil(TokenService tokenService){
        return new TokenUtil(tokenService);
    }

    public String generate(Customer customer, Specialist specialist, Manager manager) {
        String tokenValue = UUID.randomUUID().toString();
        Token token = new Token(
                tokenValue,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                customer,
                specialist,
                manager);
        tokenService.saveToken(token);
        return tokenValue;
    }

    public void setConfirmationAtToken(Token token) {
        if (token.getConfirmationAt() != null) {
            throw new CustomizedEmailException("email already confirmed");
        }
        if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new CustomizedEmailException("token expired");
        }
        tokenService.setConfirmationAtToken(token);
    }


}
