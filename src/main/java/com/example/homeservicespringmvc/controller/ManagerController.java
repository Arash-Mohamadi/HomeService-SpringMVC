package com.example.homeservicespringmvc.controller;

import com.example.homeservicespringmvc.entity.capability.MainServices;
import com.example.homeservicespringmvc.entity.capability.Order;
import com.example.homeservicespringmvc.entity.capability.SubServices;
import com.example.homeservicespringmvc.entity.enums.OrderStatus;
import com.example.homeservicespringmvc.entity.users.Customer;
import com.example.homeservicespringmvc.entity.users.Manager;
import com.example.homeservicespringmvc.entity.users.Person;
import com.example.homeservicespringmvc.entity.users.Specialist;
import com.example.homeservicespringmvc.service.ManagerService;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/v1/manager")
@RequiredArgsConstructor
public class ManagerController {

    private final ManagerService managerService;


    @PostMapping("/signup")
    public void signup(@RequestBody Manager manager) {
        managerService.signup(manager);
    }

    @GetMapping("/confirm/{tokenValue}")
    public String confirmTokenWithManager(@PathVariable String tokenValue) {
        return managerService.confirmTokenByManager(tokenValue);
    }

    @PostMapping("/add-service")
    public void addService(@RequestBody MainServices service) {
        managerService.addService(service);
    }

    @PostMapping("/add-sub-service")
    public void addSubServicesToServices(@RequestBody AddSubServiceDTO dto) {
        managerService.addSubServicesToServices(dto.getServiceName(), dto.getSubServices());
    }

    @DeleteMapping("/remove-sub-service/{subServiceName}")
    public void removeSubServicesOfServices(@PathVariable String subServiceName) {
        managerService.removeSubServicesOfServices(subServiceName);
    }


    @PutMapping("/add-specialist")
    public void addSpecialistToSubServices(@RequestBody SpecialistDTO dto) {
        managerService.addSpecialistToSubServices(dto.getSubServiceName(), dto.getSpecialistUsername());
    }

    @DeleteMapping("/remove-specialist")
    public void removeSpecialistOfSubServices(@RequestBody SpecialistDTO dto) {
        managerService.removeSpecialistOfSubServices(dto.getSubServiceName(), dto.getSpecialistUsername());
    }

    @PutMapping("/confirm-specialist/{username}")
    public void confirmSpecialist(@PathVariable String username) {
        managerService.confirmSpecialist(username);
    }

    @PutMapping("/edit-sub-service")
    public void editSubServices(@RequestBody EditSubServiceDTO dto) {
        managerService.editSubServices(dto.getSubServiceName(), dto.getSubServices());
    }

    @GetMapping("/show-all-service")
    public List<ServicesDTO> showAllService() {
        List<MainServices> servicesList = managerService.showAllService();
        return convertToServicesDTO(servicesList);
    }

    @GetMapping("show-all-customerByFilter")
    public List<CustomerDTO> searchAllCustomerByPersonalInfo(@RequestParam Map<String, String> info){
        List<Customer> customerList = managerService.searchCustomerByPersonalInfo(info);
        return convertToCustomerDTO(customerList);

    }

    @GetMapping("show-all-specialistByFilter")
    public List<ExpertDTO> searchAllSpecialistByPersonalInfo(@RequestParam Map<String, String> info){
        List<Specialist> specialistList = managerService.searchSpecialistByPersonalInfo(info);
        return convertToExpertDTO(specialistList);
    }

    @GetMapping("show-all-specialistByScoreFilter/{score}")
    public List<ExpertDTO> searchAllSpecialistByScore(@PathVariable String score){
        List<Specialist> specialistList = managerService.searchSpecialistByScore(score);
        return convertToExpertDTO(specialistList);
    }

    @GetMapping("show-all-specialistBySubServiceFilter/{name}")
    public List<ExpertDTO> searchAllSpecialistBySubService(@PathVariable String name){
        List<Specialist> specialistList = managerService.searchSpecialistBySubService(name);
        return convertToExpertDTO(specialistList);
    }



    @GetMapping("show-all-order-specialist/{username}")
    public List<ViewOrderDTO> viewOrdersSpecialist(@PathVariable String username) {
        List<Order> orderList = managerService.viewAllOrdersSpecialist(username);
        return convertToViewOrderDTO(orderList);
    }

    @GetMapping("show-all-order-customer/{username}")
    public List<ViewOrderDTO> viewOrdersCustomer(@PathVariable String username) {
        List<Order> orderList = managerService.viewAllOrdersCustomer(username);
        return convertToViewOrderDTO(orderList);
    }


    @GetMapping("/show-all-order-in-time")
    public List<ViewOrderDTO> viewOrdersByDate(@RequestBody TimeDTO timeDTO) {
        List<Order> orderList = managerService.viewOrdersByDate(timeDTO.getStart().atStartOfDay(),
                timeDTO.getEnd().atStartOfDay());
        return convertToViewOrderDTO(orderList);
    }

    @GetMapping("/show-all-order-in-status/{status}")
    public List<ViewOrderDTO> viewOrdersByStatus(@PathVariable String status) {
        List<Order> orderList = managerService.viewOrdersByStatus(status);
        return convertToViewOrderDTO(orderList);
    }

    @GetMapping("/show-all-order-in-sub-service/{name}")
    public List<ViewOrderDTO> viewOrdersBySubService(@PathVariable String name) {
        List<Order> orderList = managerService.viewOrdersBySubService(name);
        return convertToViewOrderDTO(orderList);
    }

    @GetMapping("/show-all-order-in-service/{name}")
    public List<ViewOrderDTO> viewOrdersByService(@PathVariable String name) {
        List<Order> orderList = managerService.viewOrdersByService(name);
        return convertToViewOrderDTO(orderList);
    }


    @GetMapping("/customer-dateOfSignup-report")
    public List<CustomerDTO> showAllCustomerByDateOfSignup(@RequestBody SignUpDTO dto) {
        List<Customer> customerList = managerService.showAllCustomerByDateOfSignup(dto.getDate().atStartOfDay());
        return convertToCustomerDTO(customerList);
    }


    @GetMapping("/specialist-dateOfSignup-report")
    public List<ExpertDTO> showAllSpecialistByDateOfSignup(@RequestBody SignUpDTO dto) {
        List<Specialist> specialistList = managerService.showAllSpecialistByDateOfSignup(dto.getDate().atStartOfDay());
        return convertToExpertDTO(specialistList);
    }

    @GetMapping("/customer-count-order-report/{count}")
    public List<CustomerDTO> customerGreaterThanOrEqualOrders(@PathVariable Long count) {
        List<Customer> list = managerService.showAllCustomerByCountOrder(count);
        return convertToCustomerDTO(list);
    }


    @GetMapping("/specialist-count-order-report/{count}")
    public List<ExpertDTO> specialistGreaterThanOrEqualOrders(@PathVariable Long count) {
        List<Specialist> list = managerService.showAllSpecialistByCountOrder(count);
        return convertToExpertDTO(list);
    }



    @GetMapping("show-all-user-by-customer-type-filter/{type}")
    public List<CustomerDTO> searchAllUserByCustomerType(@PathVariable String type){
        List<Customer> list = managerService.searchUserByCustomerType(type);
        return convertToCustomerDTO(list);

    }

    @GetMapping("show-all-user-by-specialist-type-filter/{type}")
    public List<ExpertDTO> searchAllUserBySpecialistType(@PathVariable String type){
        List<Specialist> list = managerService.searchUserBySpecialistType(type);
        return convertToExpertDTO(list);
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    static class AddSubServiceDTO {
        private String serviceName;
        private SubServices subServices;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    static class EditSubServiceDTO {
        private String subServiceName;
        private SubServices subServices;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    static class SpecialistDTO {
        private String subServiceName;
        private String specialistUsername;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    static class ServicesDTO {
        private String serviceName;
    }

    private List<ServicesDTO> convertToServicesDTO(List<MainServices> servicesList) {
        List<ServicesDTO> list = new ArrayList<>();
        for (MainServices service : servicesList) {
            ServicesDTO dto = new ServicesDTO();
            dto.setServiceName(service.getName());
            list.add(dto);
        }
        return list;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    static class CustomerDTO {
        private String firstname;
        private String lastname;
        private String email;
    }

    private List<CustomerDTO> convertToCustomerDTO(List<Customer> customerList) {
        List<CustomerDTO> list = new ArrayList<>();
        for (Customer customer : customerList) {
            CustomerDTO dto = new CustomerDTO();
            dto.setFirstname(customer.getFirstname());
            dto.setLastname(customer.getLastname());
            dto.setEmail(customer.getEmail());
            list.add(dto);
        }
        return list;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    static class ExpertDTO {
        private String firstname;
        private String lastname;
        private String email;
        private Integer score;
    }

    private List<ExpertDTO> convertToExpertDTO(List<Specialist> specialistList) {
        List<ExpertDTO> list = new ArrayList<>();
        for (Specialist specialist : specialistList) {
            ExpertDTO dto = new ExpertDTO();
            dto.setFirstname(specialist.getFirstname());
            dto.setLastname(specialist.getLastname());
            dto.setEmail(specialist.getEmail());
            dto.setScore(specialist.getAvg());
            list.add(dto);
        }
        return list;
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

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    static class TimeDTO {

        private LocalDate start;
        private LocalDate end;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    static class SignUpDTO {

        private LocalDate date;

    }


}




