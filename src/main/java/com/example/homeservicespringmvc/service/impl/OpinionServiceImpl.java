package com.example.homeservicespringmvc.service.impl;

import com.example.homeservicespringmvc.entity.capability.Opinion;
import com.example.homeservicespringmvc.repository.OpinionRepository;
import com.example.homeservicespringmvc.service.OpinionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OpinionServiceImpl implements OpinionService {
    private final OpinionRepository opinionRepository;

    @Override
    public void createOpinion(Opinion opinion) {
        opinionRepository.save(opinion);
    }
}
