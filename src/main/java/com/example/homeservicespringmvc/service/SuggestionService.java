package com.example.homeservicespringmvc.service;

import com.example.homeservicespringmvc.entity.capability.Order;
import com.example.homeservicespringmvc.entity.capability.Suggestion;
import com.example.homeservicespringmvc.entity.users.Specialist;

import java.util.List;
import java.util.Optional;

public interface SuggestionService {

    void createSuggestion(Suggestion suggestion);

    List<Suggestion> findSuggestionWithPrice(Long id);
    List<Suggestion> findSuggestionWithSpecialistScore(Long id);

    Optional<Suggestion> findSuggestionWithId(Long suggestionId);

    void sendSuggestion(Suggestion suggestion, Order order, Specialist specialist);
}
