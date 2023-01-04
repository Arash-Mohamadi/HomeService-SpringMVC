package com.example.homeservicespringmvc.repository;


import com.example.homeservicespringmvc.entity.users.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;


@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long> {

    Optional<Customer> findCustomerByUsername(String username);
    Optional<Customer> findCustomerByEmail(String email);


}
