package com.example.homeservicespringmvc.service;


import com.example.homeservicespringmvc.entity.capability.Credit;
import com.example.homeservicespringmvc.entity.capability.Opinion;
import com.example.homeservicespringmvc.entity.capability.Order;
import com.example.homeservicespringmvc.entity.capability.Suggestion;
import com.example.homeservicespringmvc.entity.users.Customer;
import com.example.homeservicespringmvc.entity.users.Specialist;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


public interface CustomerService {
    void signup(Customer customer);
    void editPassword(String username ,String password);
    void registerOrder(Order order, String subServiceName, String customerUsername);

    void registerOpinion(Opinion opinion, Long orderId);

    void selectSpecialist(Long suggestionId,Long orderId);

    List<Suggestion> showAllSuggestionWithPrice(Long orderId);
    List<Suggestion> showAllSuggestionWithScore(Long orderId);

    void changeStatusOrderToStarted(Long orderId);
    void changeStatusOrderToDone(Long orderId);

    List<Customer> loadAll();

    List<Customer> searchCustomerByPersonalInfo(Map<String, String> info);
    List<Customer> searchCustomerByType(String type);
    Credit showBalance(Long customerId);
    List<Order> showOrders(String username);

    List<Customer> showAllCustomerByDateOfSignup(LocalDateTime dateOfSignup);

    List<Customer> greaterThanOrEqualOrders(Long count);

    String confirmTokenByCustomer(String tokenValue);



}
