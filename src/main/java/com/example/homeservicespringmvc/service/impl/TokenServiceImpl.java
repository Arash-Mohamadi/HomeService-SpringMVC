package com.example.homeservicespringmvc.service.impl;

import com.example.homeservicespringmvc.entity.capability.Token;
import com.example.homeservicespringmvc.exception.CustomizedNotFoundException;
import com.example.homeservicespringmvc.repository.TokenRepository;
import com.example.homeservicespringmvc.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final TokenRepository tokenRepository;
    @Override
    @Transactional
    public void saveToken(Token token) {
        tokenRepository.save(token);

    }

    @Override
    @Transactional
    public Token getToken(String tokenValue) {
        return tokenRepository.findByTokenValue(tokenValue)
                .orElseThrow(()-> new CustomizedNotFoundException("token not found"));
    }

    @Override
    @Transactional
    public void setConfirmationAtToken(Token token) {
        token.setConfirmationAt(LocalDateTime.now());
        tokenRepository.save(token);
    }
}
