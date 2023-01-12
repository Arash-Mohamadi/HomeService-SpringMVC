package com.example.homeservicespringmvc.service.impl;

import com.example.homeservicespringmvc.entity.capability.Credit;
import com.example.homeservicespringmvc.entity.capability.Order;
import com.example.homeservicespringmvc.entity.capability.Transaction;
import com.example.homeservicespringmvc.entity.enums.OrderStatus;
import com.example.homeservicespringmvc.entity.enums.TransactionStatus;
import com.example.homeservicespringmvc.entity.enums.TransactionType;
import com.example.homeservicespringmvc.exception.CustomizedNotFoundException;
import com.example.homeservicespringmvc.repository.CreditRepository;
import com.example.homeservicespringmvc.service.CreditService;
import com.example.homeservicespringmvc.service.OrderService;
import com.example.homeservicespringmvc.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreditServiceImpl implements CreditService {

    private final CreditRepository creditRepository;
    private final TransactionService transactionService;

    private final OrderService orderService;

    @Override
    @Transactional
    public void createCredit(Credit credit) {
        creditRepository.save(credit);
    }

    @Override
    @Transactional
    public void deposit(Long creditId, double amount) {
        checkAmount(amount);
        Credit creditFound = checkCredit(creditId);
        creditFound.setBalance(creditFound.getBalance() + amount);
        creditRepository.save(creditFound);
        transactionService.createTransaction(
                Transaction.builder()
                        .amount(amount)
                        .type(TransactionType.DEPOSIT)
                        .status(TransactionStatus.ACCEPTED)
                        .toCredit(creditFound)
                        .fromCredit(null)
                        .order(null)
                        .build());
    }

    @Override
    @Transactional
    public void withdraw(Long creditId, double amount) {
        checkAmount(amount);
        Credit creditFound = checkCredit(creditId);
        if (creditFound.getBalance() >= amount) {
            creditFound.setBalance(creditFound.getBalance() - amount);
        }
        creditRepository.save(creditFound);
        transactionService.createTransaction(
                Transaction.builder()
                        .amount(amount)
                        .type(TransactionType.WITHDRAW)
                        .status(TransactionStatus.ACCEPTED)
                        .toCredit(null)
                        .fromCredit(creditFound)
                        .order(null)
                        .build());

    }

    @Override
    @Transactional
    public void transfer(Long sourceId, Long destinationId, Long amount, Long orderId) {
        checkAmount(amount);
        Order order = fetchOrder(orderId);
        if (order.getStatus() == OrderStatus.DONE) {
            Credit source = withdrawFromCredit(sourceId, amount);
            Credit destination = depositToCredit(destinationId, amount);
            transactionService.createTransaction(
                    Transaction.builder()
                            .amount(amount)
                            .type(TransactionType.TRANSFER)
                            .status(TransactionStatus.ACCEPTED)
                            .toCredit(destination)
                            .fromCredit(source)
                            .order(order)
                            .build()
            );
        }
        order.setStatus(OrderStatus.PAID);
        orderService.createOrder(order);// update
    }

    @Override
    @Transactional
    public void online(Long destinationId, Long amount, Long orderId) {
        checkAmount(amount);
        Order order = fetchOrder(orderId);
        if (order.getStatus() == OrderStatus.DONE) {
            Credit destination = depositToCredit(destinationId, amount);
            transactionService.createTransaction(
                    Transaction.builder()
                            .amount(amount)
                            .type(TransactionType.ONLINE)
                            .status(TransactionStatus.ACCEPTED)
                            .toCredit(destination)
                            .fromCredit(null)
                            .order(order)
                            .build()
            );
        }
        order.setStatus(OrderStatus.PAID);
        orderService.createOrder(order);// update
    }


    private void checkAmount(double amount) {
        if (amount <= 0)
            throw new IllegalArgumentException("Amount should not be less than zero.");
    }

    private Credit checkCredit(Long creditId) {
        return creditRepository.findById(creditId)
                        .orElseThrow(() -> new CustomizedNotFoundException("credit not found"));
    }

    private Credit withdrawFromCredit(Long sourceId, double amount) {
        Credit source = creditRepository.findById(sourceId)
                .orElseThrow(() -> new CustomizedNotFoundException("sourceId not found"));
        if (source.getBalance() <= amount) {
            throw new RuntimeException("insufficient balance");
        }
        source.setBalance(source.getBalance() - amount);
        creditRepository.save(source);
        return source;
    }

    private Credit depositToCredit(Long destinationId, double amount) {
        Credit destination = creditRepository.findById(destinationId)
                .orElseThrow(() -> new CustomizedNotFoundException("destinationId not found"));
        destination.setBalance(destination.getBalance() + (amount * 0.7)); // 70 %
        creditRepository.save(destination);
        return destination;
    }

    private Order fetchOrder(Long orderId){
        return orderService.fetchOrderById(orderId).orElseThrow(
                () -> new CustomizedNotFoundException("order not found"));
    }


}
