package com.example.homeservicespringmvc.controller;

import com.example.homeservicespringmvc.entity.capability.Credit;
import com.example.homeservicespringmvc.entity.capability.Opinion;
import com.example.homeservicespringmvc.entity.capability.Order;
import com.example.homeservicespringmvc.entity.capability.Suggestion;
import com.example.homeservicespringmvc.entity.enums.OrderStatus;
import com.example.homeservicespringmvc.entity.users.Specialist;
import com.example.homeservicespringmvc.service.SpecialistService;
import lombok.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api/v1/specialist")
@RequiredArgsConstructor
public class SpecialistController {

    private final SpecialistService specialistService;

    @PostMapping("/signup")
    public void signup(@RequestBody Specialist specialist) {
        specialistService.signup(specialist);
    }

    @GetMapping("/confirm/{tokenValue}")
    public String confirmTokenWithSpecialist(@PathVariable String tokenValue) {
        return specialistService.confirmTokenBySpecialist(tokenValue);
    }

    @PutMapping("/set-avatar/{username}")
    public void setAvatar(@RequestBody MultipartFile image, @PathVariable String username) {
        specialistService.setAvatar(image, username);
    }

    @PutMapping("/edit-password")
    public void editPassword(@RequestBody EditPasswordDTO dto) {
        specialistService.editPassword(dto.getUsername(), dto.getPassword());
    }

    @PostMapping("/send-suggestion")
    public void sendSuggestion(@RequestBody SuggestionDTO dto) {
        specialistService.sendSuggestion(dto.getSuggestion(), dto.getOrderId(), dto.getSpecialistUsername());
    }

    @GetMapping("/show-opinion/{orderId}")
    public OpinionDTO showOpinion(@PathVariable Long orderId) {
        Opinion opinion = specialistService.showOpinion(orderId);
        OpinionDTO dto = new OpinionDTO();
        dto.setScore(opinion.getScore());
        return dto;
    }

    @GetMapping("/show-order/{specialistId}")
    public List<OrderDTO> showOrderBelongToSpecialist(@PathVariable Long specialistId) {
        List<Order> orderList = specialistService.showOrderBelongToSpecialist(specialistId);
        return convertToOrderDTO(orderList);
    }

    @GetMapping("/show-order/{subServiceId}")
    public List<OrderDTO> showOrderBelongToSubService(@PathVariable Long subServiceId) {
        List<Order> orderList = specialistService.showOrderBelongToSubService(subServiceId);
        return convertToOrderDTO(orderList);
    }

    @GetMapping("/show-balance/{specialistId}")
    public CreditDTO viewBalance(@PathVariable Long specialistId) {
        Credit credit = specialistService.showBalance(specialistId);
        CreditDTO dto = new CreditDTO();
        dto.setBalance(credit.getBalance());
        return dto;
    }

    @GetMapping("/show-orders/{username}")
    public List<ViewOrderDTO> viewOrders(@PathVariable String username) {
        List<Order> orderList = specialistService.showOrders(username);
        return convertToViewOrderDTO(orderList);
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    static class EditPasswordDTO {
        private String username;
        private String password;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    static class SignupDTO {
        private MultipartFile file;
        private String username;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    static class SuggestionDTO {
        private Suggestion suggestion;
        private Long orderId;
        private String specialistUsername;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    static class OpinionDTO {
        private Integer score;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    static class OrderDTO {
        private Long orderId;
    }

    private List<OrderDTO> convertToOrderDTO(List<Order> orderList) {
        List<OrderDTO> list = new ArrayList<>();
        for (Order order : orderList) {
            OrderDTO dto = new OrderDTO();
            dto.setOrderId(order.getId());
            list.add(dto);
        }
        return list;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    static class CreditDTO {

        private double balance;

    }


    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    static
    class ViewOrderDTO {

        private double price;
        private LocalDateTime creationDate;
        private OrderStatus status;

    }

    private List<ViewOrderDTO> convertToViewOrderDTO(List<Order> orderList) {
        List<ViewOrderDTO> list = new ArrayList<>();
        for (Order order : orderList) {
            ViewOrderDTO dto = new ViewOrderDTO();
            dto.setCreationDate(order.getCreationDate());
            dto.setPrice(order.getPrice());
            dto.setStatus(order.getStatus());
            list.add(dto);
        }
        return list;
    }
}
