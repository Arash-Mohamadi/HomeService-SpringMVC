package com.example.homeservicespringmvc.service;


import com.example.homeservicespringmvc.entity.capability.*;
import com.example.homeservicespringmvc.entity.users.Customer;
import com.example.homeservicespringmvc.entity.users.Specialist;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface SpecialistService {

    void signup(Specialist specialist);

    void editPassword(String username, String password);

    void sendSuggestion(Suggestion suggestion, Long orderId, String specialistUsername);

    void createSpecialist(Specialist specialist);

    void selectSpecialist(Long suggestionId, Long orderId);

    void addSpecialistToSubService(String specialistUsername, String subServiceName);

    void removeSpecialistOfSubServices(String subServiceName, String specialistUsername);

    void confirmSpecialist(String specialistUsername);

    void setAvatar(MultipartFile image, String username);


    List<Specialist> findSpecialistByScoreAvg(Integer score);
    List<Specialist> loadAll();
    Opinion showOpinion(Long orderId);

    List<Order> showOrderBelongToSpecialist(Long specialistId);
    List<Order> showOrderBelongToSubService(Long subServiceId);

    List<Specialist> searchSpecialistByPersonalInfo(Map<String, String> info);

    List<Specialist> searchSpecialistByType(String type);
    List<Specialist> searchSpecialistByScore(String score);
    List<Specialist> searchSpecialistBySubService(SubServices subServices);
    Credit showBalance(Long specialistId);
    List<Order> showOrders(String username);
    List<Specialist> showAllSpecialistByDateOfSignup(LocalDateTime dateOfSignup);

    List<Specialist> greaterThanOrEqualOrders(Long count);
    String confirmTokenBySpecialist(String tokenValue);

}
