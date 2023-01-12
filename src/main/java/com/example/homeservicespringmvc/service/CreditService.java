package com.example.homeservicespringmvc.service;

import com.example.homeservicespringmvc.entity.capability.Credit;

public interface CreditService  {

    void createCredit(Credit credit);

    void deposit(Long creditId,double amount);
    void withdraw(Long creditId,double amount);
    void transfer(Long sourceId, Long destinationId, Long amount,Long orderId);
    void online(Long destinationId, Long amount,Long orderId);
}
