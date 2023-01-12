package com.example.homeservicespringmvc.repository;

import com.example.homeservicespringmvc.entity.users.Customer;
import com.example.homeservicespringmvc.entity.users.Person;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


public interface CustomerCustomRepository {

    List<Customer> findCustomerByFilter(Map<String,String> filter);
}
