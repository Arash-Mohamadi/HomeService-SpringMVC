package com.example.homeservicespringmvc.service.impl;

import com.example.homeservicespringmvc.entity.capability.*;
import com.example.homeservicespringmvc.entity.enums.UserRole;
import com.example.homeservicespringmvc.entity.users.Customer;
import com.example.homeservicespringmvc.entity.users.Manager;
import com.example.homeservicespringmvc.entity.users.Person;
import com.example.homeservicespringmvc.entity.users.Specialist;
import com.example.homeservicespringmvc.exception.CustomizedDuplicatedItemException;
import com.example.homeservicespringmvc.exception.CustomizedEmailException;
import com.example.homeservicespringmvc.exception.CustomizedNotFoundException;
import com.example.homeservicespringmvc.repository.ManagerRepository;
import com.example.homeservicespringmvc.security.email.EmailSender;
import com.example.homeservicespringmvc.security.email.EmailSenderImpl;
import com.example.homeservicespringmvc.service.*;
import com.example.homeservicespringmvc.util.TokenUtil;
import com.example.homeservicespringmvc.validation.HibernateValidatorProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static com.example.homeservicespringmvc.util.TokenUtil.setConfirmationAtToken;

@Service
@RequiredArgsConstructor
public class ManagerServiceImpl implements ManagerService {

    private final MainServicesService mainServicesService;
    private final SubServicesService subServicesService;
    private final SpecialistService specialistService;

    private final PasswordEncoder passwordEncoder;
    private final CustomerService customerService;
    private final OrderService orderService;

    private final ManagerRepository managerRepository;

    private final TokenService tokenService;
    private final EmailSender emailSender;

    @Override
    @Transactional
    public void addService(MainServices services) {
        mainServicesService.addService(services);
    }

    @Override
    @Transactional
    public void addSubServicesToServices(String serviceName, SubServices subServices) {
        mainServicesService.addSubServicesToServices(serviceName, subServices);
    }

    @Override
    @Transactional
    public void removeSubServicesOfServices(String subServiceName) {
        mainServicesService.removeSubServiceByManager(subServiceName);
    }

    @Override
    @Transactional
    public void addSpecialistToSubServices(String subServiceName, String specialistUsername) {
        specialistService.addSpecialistToSubService(specialistUsername, subServiceName);
    }

    @Override
    @Transactional
    public void removeSpecialistOfSubServices(String subServiceName, String specialistUsername) {
        specialistService.removeSpecialistOfSubServices(subServiceName, specialistUsername);
    }

    @Override
    @Transactional
    public void confirmSpecialist(String specialistUsername) {
        specialistService.confirmSpecialist(specialistUsername);
    }


    @Override
    @Transactional
    public List<MainServices> showAllService() {
        return mainServicesService.showAllService();
    }

    @Override
    @Transactional
    public List<Customer> searchCustomerByPersonalInfo(Map<String, String> info) {
        return customerService.searchCustomerByPersonalInfo(info);
    }


    @Override
    @Transactional
    public List<Specialist> searchUserBySpecialistType(String type) {
        String userType = type.toUpperCase();
        if (userType.equals("SPECIALIST")) {
            return specialistService.searchSpecialistByType(userType);
        }
        throw new CustomizedNotFoundException("desired role not found");
    }

    @Override
    @Transactional
    public List<Customer> searchUserByCustomerType(String type) {
        String userType = type.toUpperCase();
        if (userType.equals("CUSTOMER")) {
            return customerService.searchCustomerByType(userType);
        } else
            throw new CustomizedNotFoundException("desired role not found");

    }

    @Override
    @Transactional
    public List<Specialist> searchSpecialistByScore(String score) {
        return specialistService.searchSpecialistByScore(score);
    }

    @Override
    @Transactional
    public List<Specialist> searchSpecialistBySubService(String name) {
        SubServices subServices = subServicesService.fetchSubServiceWithName(name).orElseThrow(
                () -> new CustomizedNotFoundException("subService not found"));
        return specialistService.searchSpecialistBySubService(subServices);
    }

    @Override
    @Transactional
    public List<Specialist> searchSpecialistByPersonalInfo(Map<String, String> info) {
        return specialistService.searchSpecialistByPersonalInfo(info);
    }

    @Override
    @Transactional
    public List<Order> viewAllOrdersCustomer(String username) {
        return customerService.showOrders(username);
    }

    @Override
    @Transactional
    public List<Order> viewAllOrdersSpecialist(String username) {
        return specialistService.showOrders(username);
    }

    @Override
    @Transactional
    public List<Order> viewOrdersByDate(LocalDateTime start, LocalDateTime end) {
        return orderService.viewOrderByTime(start, end);
    }

    @Override
    @Transactional
    public List<Order> viewOrdersByStatus(String status) {
        return orderService.viewOrderByStatus(status);
    }

    @Override
    @Transactional
    public List<Order> viewOrdersBySubService(String name) {
        return orderService.viewOrderBySubService(name);
    }

    @Override
    @Transactional
    public List<Order> viewOrdersByService(String name) {
        return orderService.viewOrderByService(name);
    }

    @Override
    @Transactional
    public List<Customer> showAllCustomerByDateOfSignup(LocalDateTime dateOfSignup) {
        return customerService.showAllCustomerByDateOfSignup(dateOfSignup);
    }

    @Override
    @Transactional
    public List<Specialist> showAllSpecialistByDateOfSignup(LocalDateTime dateOfSignup) {
        return specialistService.showAllSpecialistByDateOfSignup(dateOfSignup);
    }

    @Override
    @Transactional
    public List<Customer> showAllCustomerByCountOrder(Long count) {
        return customerService.greaterThanOrEqualOrders(count);
    }

    @Override
    @Transactional
    public List<Specialist> showAllSpecialistByCountOrder(Long count) {
        return specialistService.greaterThanOrEqualOrders(count);
    }

    @Override
    @Transactional
    public void signup(Manager manager) {
        HibernateValidatorProvider.checkEntity(manager);
        if (!isExistManager(manager)) {
            manager.setPassword(passwordEncoder.encode(manager.getPassword()));
            manager.setUserType(UserRole.MANAGER);
            managerRepository.save(manager);
            String tokenValue = TokenUtil.generate(null, null, manager);
            String link = "http://localhost:8080/api/v1/registration/confirm?token=" + tokenValue;
            emailSender.send(manager.getEmail(),
                    EmailSenderImpl.buildEmail(manager.getFirstname(),link ));
        }
    }

    @Override
    @Transactional
    public String confirmTokenByManager(String tokenValue) {
        Token tokenFound = tokenService.getToken(tokenValue);
        setConfirmationAtToken(tokenFound);
        enableManager(tokenFound.getCustomer().getEmail());
        return "email confirmed,thank you";
    }


    @Override
    @Transactional
    public void editSubServices(String subServiceName, SubServices subServices) {
        subServicesService.editSubService(subServiceName, subServices);
    }


    private boolean isExistManager(Manager manager) {
        if (managerRepository.findManagerByUsername(manager.getUsername()).isPresent()) {
            throw new CustomizedDuplicatedItemException("username is duplicate");
        }
        if (managerRepository.findManagerByEmail(manager.getEmail()).isPresent()) {
            throw new CustomizedDuplicatedItemException("email is duplicate");
        }
        return false;
    }

    private void enableManager(String email) {
        Manager manager = managerRepository.findManagerByEmail(email)
                .orElseThrow(() -> new CustomizedEmailException("email noy found"));
        manager.setEnabled(true);
        managerRepository.save(manager);

    }


}
