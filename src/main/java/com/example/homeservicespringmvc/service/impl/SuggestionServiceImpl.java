package com.example.homeservicespringmvc.service.impl;

import com.example.homeservicespringmvc.entity.capability.Order;
import com.example.homeservicespringmvc.entity.capability.Suggestion;
import com.example.homeservicespringmvc.entity.enums.OrderStatus;
import com.example.homeservicespringmvc.entity.users.Specialist;
import com.example.homeservicespringmvc.repository.SuggestionRepository;
import com.example.homeservicespringmvc.service.OrderService;
import com.example.homeservicespringmvc.service.SpecialistService;
import com.example.homeservicespringmvc.service.SuggestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SuggestionServiceImpl implements SuggestionService {

    private final SuggestionRepository suggestionRepository;
    private final OrderService orderService ;
    private final SpecialistService specialistService ;

    @Override
    @Transactional
    public void createSuggestion(Suggestion suggestion) {
        suggestionRepository.save(suggestion);
    }

    @Override
    @Transactional
    public List<Suggestion> findSuggestionWithPrice(Long id) {
        return suggestionRepository.findAllSuggestionOneOrderByPrice(id);
    }

    @Override
    @Transactional
    public List<Suggestion> findSuggestionWithSpecialistScore(Long id) {
        return suggestionRepository.findAllSuggestionOneOrderBySpecialistScore(id);
    }

    @Override
    @Transactional
    public Optional<Suggestion> findSuggestionWithId(Long suggestionId) {
        return suggestionRepository.findById(suggestionId);
    }

    @Override
    @Transactional
    public void sendSuggestion(Suggestion suggestion, Order order, Specialist specialist) {
        if(suggestion.getPrice()>=order.getPrice()){
            order.addSuggestion(suggestion);
            order.setStatus(OrderStatus.PENDING_FOR_SPECIALIST_SELECTION);
            suggestion.setSpecialist(specialist);
            suggestionRepository.save(suggestion);
        }else
            throw new RuntimeException("price of suggestion less than base price");
    }
}
