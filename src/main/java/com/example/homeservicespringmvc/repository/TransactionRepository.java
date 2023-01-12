package com.example.homeservicespringmvc.repository;

import com.example.homeservicespringmvc.entity.capability.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Long> {
}
