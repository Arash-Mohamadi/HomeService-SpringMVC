package com.example.homeservicespringmvc.controller;

import com.example.homeservicespringmvc.entity.capability.Credit;
import com.example.homeservicespringmvc.entity.capability.Opinion;
import com.example.homeservicespringmvc.entity.capability.Order;
import com.example.homeservicespringmvc.entity.capability.Suggestion;
import com.example.homeservicespringmvc.entity.enums.OrderStatus;
import com.example.homeservicespringmvc.entity.users.Customer;
import com.example.homeservicespringmvc.entity.users.Specialist;
import com.example.homeservicespringmvc.service.CustomerService;
import lombok.*;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping("/signup")
    public void signup(@RequestBody Customer customer) {

        customerService.signup(customer);
    }

    @GetMapping("/confirm/{tokenValue}")
    public String confirmTokenWithCustomer(@PathVariable String tokenValue) {
        return customerService.confirmTokenByCustomer(tokenValue);
    }

    @PutMapping("/edit-password")
    public void editPassword(@RequestBody EditPasswordDTO dto) {
        customerService.editPassword(dto.getUsername(), dto.getPassword());
    }

    @PostMapping("/register-order")
    public void registerOrder(@RequestBody RegisterOrderDTO dto) {
        customerService.registerOrder(dto.getOrder(), dto.getSubServiceName(), dto.getCustomerUsername());
    }

    @PostMapping("/register-opinion")
    public void registerOpinion(@RequestBody RegisterOpinionDTO dto) {
        customerService.registerOpinion(dto.getOpinion(), dto.getOrderId());
    }


    @PutMapping("/select-specialist")
    public void selectSpecialist(@RequestBody SelectSpecialistDTO dto) {
        customerService.selectSpecialist(dto.getSuggestionId(), dto.getOrderId());
    }

    @PutMapping("/change-status-order-to-started/{orderId}")
    public void changeStatusOrderToStarted(@PathVariable Long orderId) {
        customerService.changeStatusOrderToStarted(orderId);
    }

    @PutMapping("/change-status-order-to-done/{orderId}")
    public void changeStatusOrderToDone(@PathVariable Long orderId) {
        customerService.changeStatusOrderToDone(orderId);
    }

    @GetMapping("/show-all-suggestion-with-price/{orderId}")
    public List<SuggestionBasedPriceDTO> showAllSuggestionWithPrice(@PathVariable Long orderId) {
        List<SuggestionBasedPriceDTO> list = new ArrayList<>();

        for (Suggestion suggestion : customerService.showAllSuggestionWithPrice(orderId)) {
            SuggestionBasedPriceDTO dto = new SuggestionBasedPriceDTO();
            dto.setPrice(suggestion.getPrice());
            dto.setSpecialistName(suggestion.getSpecialist().getUsername());
            list.add(dto);
        }
        return list;
    }


    @GetMapping("/show-all-suggestion-with-score/{orderId}")
    public List<SuggestionBasedScoreDTO> showAllSuggestionWithScore(@PathVariable Long orderId) {
        List<SuggestionBasedScoreDTO> list = new ArrayList<>();
        for (Suggestion suggestion : customerService.showAllSuggestionWithScore(orderId)) {
            SuggestionBasedScoreDTO dto = new SuggestionBasedScoreDTO();
            dto.setScore(suggestion.getSpecialist().getAvg());
            dto.setSpecialistName(suggestion.getSpecialist().getUsername());
            list.add(dto);
        }
        return list;
    }

    @GetMapping("/show-balance/{customerId}")
    public CreditDTO viewBalance(@PathVariable Long customerId) {
        Credit credit = customerService.showBalance(customerId);
        CreditDTO dto = new CreditDTO();
        dto.setBalance(credit.getBalance());
        return dto;
    }

    @GetMapping("/show-orders/{username}")
    public List<OrderDTO> viewBalance(@PathVariable String username) {
        List<Order> orderList = customerService.showOrders(username);
        return convertToOrderDTO(orderList);
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    static
    class EditPasswordDTO {
        private String username;
        private String password;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    static
    class RegisterOrderDTO {
        private Order order;
        private String subServiceName;
        private String customerUsername;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    static
    class RegisterOpinionDTO {
        private Long orderId;
        private Opinion opinion;

    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    static
    class SelectSpecialistDTO {
        private Long orderId;
        private Long suggestionId;

    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    static
    class SuggestionBasedPriceDTO {
        private double price;
        private String specialistName;


    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    static
    class SuggestionBasedScoreDTO {
        private String specialistName;
        private double score;

    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    static
    class CreditDTO {

        private double balance;

    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    static
    class OrderDTO {

        private double price;
        private LocalDateTime creationDate;
        private OrderStatus status;

    }

    private List<OrderDTO> convertToOrderDTO(List<Order> orderList) {
        List<OrderDTO> list = new ArrayList<>();
        for (Order order : orderList) {
            OrderDTO dto = new OrderDTO();
            dto.setCreationDate(order.getCreationDate());
            dto.setPrice(order.getPrice());
            dto.setStatus(order.getStatus());
            list.add(dto);
        }
        return list;


    }
}





