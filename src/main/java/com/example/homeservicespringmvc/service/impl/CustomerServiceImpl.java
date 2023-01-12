package com.example.homeservicespringmvc.service.impl;

import com.example.homeservicespringmvc.entity.capability.*;
import com.example.homeservicespringmvc.entity.enums.OrderStatus;
import com.example.homeservicespringmvc.entity.enums.UserRole;
import com.example.homeservicespringmvc.entity.users.Customer;
import com.example.homeservicespringmvc.exception.CustomizedDuplicatedItemException;
import com.example.homeservicespringmvc.exception.CustomizedEmailException;
import com.example.homeservicespringmvc.exception.CustomizedInvalidStatusException;
import com.example.homeservicespringmvc.exception.CustomizedNotFoundException;
import com.example.homeservicespringmvc.repository.CustomerRepository;
import com.example.homeservicespringmvc.security.email.EmailSender;
import com.example.homeservicespringmvc.security.email.EmailSenderImpl;
import com.example.homeservicespringmvc.service.*;

import static com.example.homeservicespringmvc.specification.CustomerSpecification.*;

import com.example.homeservicespringmvc.util.TokenUtil;
import com.example.homeservicespringmvc.validation.HibernateValidatorProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final OrderService orderService;
    private final OpinionService opinionService;
    private final SuggestionService suggestionService;
    private final SpecialistService specialistService;

    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    private final EmailSender emailSender;


    @Override
    @Transactional
    public void signup(Customer customer) {
        HibernateValidatorProvider.checkEntity(customer);
        if (!isExistCustomer(customer)) {
            customer.setPassword(passwordEncoder.encode(customer.getPassword()));
            Credit credit = new Credit();
            customer.setUserType(UserRole.CUSTOMER);
            customer.setCredit(credit);
            customerRepository.save(customer);
            String tokenValue = TokenUtil.getTokenUtil(tokenService).generate(customer, null, null);
            String link = "http://localhost:8080/api/v1/registration/confirm?token=" + tokenValue;
            emailSender.send(customer.getEmail(),
                    EmailSenderImpl.buildEmail(customer.getFirstname(),link ));
        }
    }

    @Override
    @Transactional
    public String confirmTokenByCustomer(String tokenValue) {
        Token tokenFound = tokenService.getToken(tokenValue);
        TokenUtil.getTokenUtil(tokenService).setConfirmationAtToken(tokenFound);
        enableCustomer(tokenFound.getCustomer().getId());
        return "email confirmed,thank you";
    }

    @Override
    @Transactional
    public void editPassword(String username, String password) {
        Customer customer;
        customer = customerRepository.findCustomerByUsername(username)
                .orElseThrow(() -> new CustomizedNotFoundException("desired customer not found ."));

        customer.setPassword(password);
        HibernateValidatorProvider.checkEntity(customer);
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        customerRepository.save(customer); // update
    }

    @Override
    @Transactional
    public void registerOrder(Order order, String subServiceName, String customerUsername) {
        Customer customer = fetchCustomer(customerUsername);
        orderService.registerOrder(order, subServiceName, customer);
    }

    @Override
    @Transactional
    public void registerOpinion(Opinion opinion, Long orderId) {
        Order order = fetchOrder(orderId);
        if (order.getStatus() != OrderStatus.PAID) {
            throw new CustomizedInvalidStatusException(" order should 'PAID' ");
        } else {
            setOrderWithOpinion(order, opinion); // blow page
        }
    }

    @Override
    @Transactional
    public void selectSpecialist(Long suggestionId, Long orderId) {
        specialistService.selectSpecialist(suggestionId, orderId);
    }

    @Override
    @Transactional
    public List<Suggestion> showAllSuggestionWithPrice(Long orderId) {
        return suggestionService.findSuggestionWithPrice(orderId);
    }

    @Override
    @Transactional
    public List<Suggestion> showAllSuggestionWithScore(Long orderId) {
        return suggestionService.findSuggestionWithSpecialistScore(orderId);
    }

    @Override
    @Transactional
    public void changeStatusOrderToStarted(Long orderId) {
        orderService.changeStatusOrderToStarted(orderId);
    }

    @Override
    @Transactional
    public void changeStatusOrderToDone(Long orderId) {
        orderService.changeStatusOrderToDone(orderId);
    }


    @Override
    @Transactional
    public List<Customer> loadAll() {
        return customerRepository.findAll();
    }

    @Override
    @Transactional
    public List<Customer> searchCustomerByPersonalInfo(Map<String, String> info) {

        return customerRepository.findAll(searchCustomer(info));
    }

    @Override
    @Transactional
    public List<Customer> searchCustomerByType(String type) {
        return customerRepository.findAll(hasRole(type));
    }

    @Override
    @Transactional
    public Credit showBalance(Long customerId) {
        return customerRepository.viewBalance(customerId);
    }

    @Override
    @Transactional
    public List<Order> showOrders(String username) {
        return customerRepository.viewOrders(username);
    }

    @Override
    @Transactional
    public List<Customer> showAllCustomerByDateOfSignup(LocalDateTime dateOfSignup) {

        return customerRepository.findAll(dateOfSignupReport(dateOfSignup));
    }

    @Override
    @Transactional
    public List<Customer> greaterThanOrEqualOrders(Long count) {
        return customerRepository.findAll(greaterThanOrEqualOrder(count));
    }




    private boolean isExistCustomer(Customer customer) {
        if (customerRepository.findCustomerByUsername(customer.getUsername()).isPresent()) {
            throw new CustomizedDuplicatedItemException("username is duplicate");
        }
        if (customerRepository.findCustomerByEmail(customer.getEmail()).isPresent()) {
            throw new CustomizedDuplicatedItemException("email is duplicate");
        }
        return false;
    }

    private void setOrderWithOpinion(Order order, Opinion opinion) {
        if (order.getOpinion() == null) {
            order.setOpinion(opinion);
            opinion.setSpecialist(order.getSpecialist());
            opinion.setCustomer(order.getCustomer());
            order.getSpecialist().setAvg(opinion.getScore() + order.getSpecialist().getAvg());
            opinionService.createOpinion(opinion); // save opinion
        } else
            throw new RuntimeException("opinion registered");

    }

    private Customer fetchCustomer(String customerUsername) {
        return customerRepository.findCustomerByUsername(customerUsername).orElseThrow(
                () -> new CustomizedNotFoundException(" customer not found ."));
    }

    private Order fetchOrder(Long orderId) {
        return orderService.fetchOrderById(orderId).orElseThrow(
                () -> new CustomizedNotFoundException(" desired order not found ."));
    }

    private void enableCustomer(Long id) {
        Customer customer = customerRepository.getReferenceById(id);
//                .orElseThrow(() -> new CustomizedEmailException("email not found"));
        customer.setEnabled(true);
        customerRepository.save(customer);

    }


}
