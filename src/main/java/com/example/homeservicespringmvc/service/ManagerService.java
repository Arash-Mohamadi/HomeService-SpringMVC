package com.example.homeservicespringmvc.service;


import com.example.homeservicespringmvc.entity.capability.MainServices;
import com.example.homeservicespringmvc.entity.capability.Order;
import com.example.homeservicespringmvc.entity.capability.SubServices;
import com.example.homeservicespringmvc.entity.users.Customer;
import com.example.homeservicespringmvc.entity.users.Manager;
import com.example.homeservicespringmvc.entity.users.Person;
import com.example.homeservicespringmvc.entity.users.Specialist;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


public interface ManagerService {

    void addService(MainServices services);

    void addSubServicesToServices(String serviceName, SubServices subServices);

    void removeSubServicesOfServices(String subServiceName);

    void addSpecialistToSubServices(String subServiceName, String specialistUsername);

    void removeSpecialistOfSubServices(String subServiceName, String specialistUsername);


    void confirmSpecialist(String specialistUsername);

    void editSubServices(String subServiceName, SubServices subServices);

    List<MainServices> showAllService();

    List<Customer> searchCustomerByPersonalInfo(Map<String, String> info);

    List<Customer> searchUserByCustomerType(String type);
    List<Specialist> searchUserBySpecialistType(String type);

    List<Specialist> searchSpecialistByScore(String score);
    List<Specialist> searchSpecialistBySubService(String name);

    List<Specialist> searchSpecialistByPersonalInfo(Map<String, String> info);

    List<Order> viewAllOrdersCustomer(String username);
    List<Order> viewAllOrdersSpecialist(String username);

    List<Order> viewOrdersByDate(LocalDateTime start,LocalDateTime end);
    List<Order> viewOrdersByStatus(String status);
    List<Order> viewOrdersBySubService(String name);
    List<Order> viewOrdersByService(String name);
   List<Customer> showAllCustomerByDateOfSignup(LocalDateTime dateOfSignup);
   List<Specialist> showAllSpecialistByDateOfSignup(LocalDateTime dateOfSignup);

   List<Customer> showAllCustomerByCountOrder(Long count);
   List<Specialist> showAllSpecialistByCountOrder(Long count);

    void signup(Manager manager);

    String confirmTokenByManager(String tokenValue);


}
