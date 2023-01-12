package com.example.homeservicespringmvc.service.impl;

import com.example.homeservicespringmvc.entity.capability.Transaction;
import com.example.homeservicespringmvc.repository.TransactionRepository;
import com.example.homeservicespringmvc.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;


    @Override
    @Transactional
    public void createTransaction(Transaction transaction) {
        transactionRepository.save(transaction);

    }
}
