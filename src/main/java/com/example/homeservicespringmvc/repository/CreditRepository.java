package com.example.homeservicespringmvc.repository;

import com.example.homeservicespringmvc.entity.capability.Credit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditRepository extends JpaRepository<Credit,Long> {
}
