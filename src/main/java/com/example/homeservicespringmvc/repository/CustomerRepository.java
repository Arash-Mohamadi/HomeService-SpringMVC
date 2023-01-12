package com.example.homeservicespringmvc.repository;


import com.example.homeservicespringmvc.entity.capability.Credit;
import com.example.homeservicespringmvc.entity.capability.Order;
import com.example.homeservicespringmvc.entity.users.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>
        , CustomerCustomRepository
        , JpaSpecificationExecutor<Customer> {

    Optional<Customer> findCustomerByUsername(String username);

    Optional<Customer> findCustomerByEmail(String email);

    @Query("""
            select c.credit
            from Customer c 
            where c.id=:customerId
                        """)
    Credit viewBalance(Long customerId);


    @Query("""
                        select o
                        from Order o
                        join Customer c
                        on o.customer.id = c.id
            where c.username =:username
                         """)
    List<Order> viewOrders(String username);


}
