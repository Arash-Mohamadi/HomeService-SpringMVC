package com.example.homeservicespringmvc.service;

import com.example.homeservicespringmvc.entity.capability.MainServices;
import com.example.homeservicespringmvc.entity.capability.Order;
import com.example.homeservicespringmvc.entity.capability.SubServices;
import com.example.homeservicespringmvc.entity.users.Customer;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderService {

    void createOrder(Order order);

    void registerOrder(Order order, String subServiceName, Customer customer);
    List<Order> showOrderBelongToSpecialist(Long specialistId);

    List<Order> showOrderBelongToSubService(Long subServiceId);
    Optional<Order> fetchOrderById(Long orderId);

    void changeStatusOrderToStarted(Long orderId);
    void changeStatusOrderToDone(Long orderId);

    List<Order> viewOrderByTime(LocalDateTime start,LocalDateTime end);
    List<Order> viewOrderByStatus(String status);
    List<Order> viewOrderBySubService(String name);
    List<Order> viewOrderByService(String name);

}
