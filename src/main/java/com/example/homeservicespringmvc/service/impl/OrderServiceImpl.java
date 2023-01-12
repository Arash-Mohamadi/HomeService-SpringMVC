package com.example.homeservicespringmvc.service.impl;

import com.example.homeservicespringmvc.entity.capability.MainServices;
import com.example.homeservicespringmvc.entity.capability.Order;
import com.example.homeservicespringmvc.entity.capability.SubServices;
import com.example.homeservicespringmvc.entity.enums.OrderStatus;
import com.example.homeservicespringmvc.entity.enums.SpecialistStatus;
import com.example.homeservicespringmvc.entity.users.Customer;
import com.example.homeservicespringmvc.exception.CustomizedInvalidStatusException;
import com.example.homeservicespringmvc.exception.CustomizedNotFoundException;
import com.example.homeservicespringmvc.repository.OrderRepository;
import com.example.homeservicespringmvc.service.MainServicesService;
import com.example.homeservicespringmvc.service.OrderService;
import com.example.homeservicespringmvc.service.SubServicesService;
import com.example.homeservicespringmvc.specification.OrderSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final SubServicesService subServicesService;
    private final MainServicesService mainServicesService;


    @Override
    @Transactional
    public void createOrder(Order order) {
        orderRepository.save(order);
    }

    @Override
    @Transactional
    public void registerOrder(Order order, String subServiceName, Customer customer) {
        SubServices subServices;
        subServices = subServicesService.fetchSubServiceWithName(subServiceName).orElseThrow(
                () -> new CustomizedNotFoundException(" desired subServicesService not found ."));

        if (order.getPrice() >= subServices.getBasePrice()) {
            order.setStatus(OrderStatus.PENDING_FOR_SPECIALIST_SUGGESTION);
            subServices.addOrder(order);
            order.setCustomer(customer);
            orderRepository.save(order);
        } else {
            throw new CustomizedInvalidStatusException(" price of order less than base price subService .");
        }
    }

    @Override
    @Transactional
    public List<Order> showOrderBelongToSpecialist(Long specialistId) {
        return orderRepository.showOrderBelongToSpecialist(specialistId);
    }

    @Override
    public List<Order> showOrderBelongToSubService(Long subServiceId) {
        return orderRepository.showOrderBelongToSubService(subServiceId);
    }

    @Override
    @Transactional
    public Optional<Order> fetchOrderById(Long orderId) {
        return orderRepository.findById(orderId);
    }

    @Override
    @Transactional
    public void changeStatusOrderToStarted(Long orderId) {
        Order order = fetchOrder(orderId);
        if (order.getStatus() != OrderStatus.PENDING_FOR_SPECIALIST_COMING) {
            throw new CustomizedInvalidStatusException(" order not allowed to change");
        }
        if (LocalDateTime.now().isAfter(order.getSelectedSuggestion().getStartWork())) {
            order.setStatus(OrderStatus.STARTED);
        } else {
            throw new RuntimeException("time for change status of order finished");
        }
    }

    @Override
    @Transactional
    public void changeStatusOrderToDone(Long orderId) {
        Order order = fetchOrder(orderId);
        checkStatusOrderEqualToStarted(order);
        LocalDateTime endWork = order.getSelectedSuggestion().getEndWork();
        if (LocalDateTime.now().isAfter(endWork)) {
            checkTimeOut(endWork, order);
        }
        order.setStatus(OrderStatus.DONE);
    }

    @Override
    @Transactional
    public List<Order> viewOrderByTime(LocalDateTime start, LocalDateTime end) {
        return orderRepository.findAll(OrderSpecification.hasDate(start, end));
    }

    @Override
    @Transactional
    public List<Order> viewOrderByStatus(String status) {
        return orderRepository.findAll(OrderSpecification.hasStatus(status));
    }

    @Override
    @Transactional
    public List<Order> viewOrderBySubService(String name) {
        SubServices subServices = subServicesService.fetchSubServiceWithName(name).orElseThrow(
                () -> new CustomizedNotFoundException("subService notfound"));
        return orderRepository.findAll(OrderSpecification.hasSubService(subServices));
    }

    @Override
    @Transactional
    public List<Order> viewOrderByService(String name) {
        MainServices services = mainServicesService.findServiceByName(name).orElseThrow(
                () -> new CustomizedNotFoundException("service not found"));
        return orderRepository.findAll(OrderSpecification.hasService(services));
    }


    @Override
    public int hashCode() {
        return super.hashCode();
    }

    private Order fetchOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomizedNotFoundException("this order not found"));
    }

    private void checkStatusOrderEqualToStarted(Order order) {
        if (order.getStatus() != OrderStatus.STARTED) {
            throw new CustomizedInvalidStatusException(" order not allowed to change");
        }
    }

    private void checkTimeOut(LocalDateTime endWork, Order order) {
        Duration duration = Duration.between(endWork, LocalDateTime.now());
        Integer hours = (int) duration.toHours();
        order.getSpecialist().setAvg(order.getSpecialist().getAvg() - hours);
        if (order.getSpecialist().getAvg() <= 0) {
            order.getSpecialist().setStatus(SpecialistStatus.DISABLE);
        }
    }


}
