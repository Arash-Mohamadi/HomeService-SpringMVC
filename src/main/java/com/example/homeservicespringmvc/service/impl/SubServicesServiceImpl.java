package com.example.homeservicespringmvc.service.impl;

import com.example.homeservicespringmvc.entity.capability.SubServices;

import com.example.homeservicespringmvc.repository.SubServiceRepository;
import com.example.homeservicespringmvc.service.SubServicesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubServicesServiceImpl implements SubServicesService {
    private final SubServiceRepository subServiceRepository;


    @Override
    @Transactional
    public Optional<SubServices> fetchSubServiceWithName(String name) {
        return subServiceRepository.findSubServicesByName(name);
    }

    @Override
    @Transactional
    public void createSubService(SubServices subServices) {
        subServiceRepository.save(subServices);

    }

    @Override
    @Transactional
    public void removeOfServiceById(Long subId) {
        subServiceRepository.deleteById(subId);
    }

    @Override
    @Transactional
    public void editSubService(String subServiceName, SubServices subServices) {
        SubServices subServicesFound = subServiceRepository.findSubServicesByName(subServiceName)
                .orElseThrow(() -> new RuntimeException("subService notfound"));
        replaceSubService(subServicesFound, subServices);
    }

    private void replaceSubService(SubServices subServicesFound, SubServices subServices) {

            subServicesFound.setDescription(subServices.getDescription());
            subServicesFound.setBasePrice(subServices.getBasePrice());
            subServiceRepository.save(subServicesFound);//update

    }


}
