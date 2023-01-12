package com.example.homeservicespringmvc.service.impl;

import com.example.homeservicespringmvc.entity.capability.MainServices;
import com.example.homeservicespringmvc.entity.capability.SubServices;
import com.example.homeservicespringmvc.exception.CustomizedNotFoundException;
import com.example.homeservicespringmvc.repository.MainServiceRepository;
import com.example.homeservicespringmvc.service.MainServicesService;
import com.example.homeservicespringmvc.service.SubServicesService;
import com.example.homeservicespringmvc.validation.HibernateValidatorProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MainServiceImpl implements MainServicesService {

    private final MainServiceRepository mainServiceRepository;
    private final SubServicesService subServicesService;

    @Override
    @Transactional
    public void addService(MainServices services) {
        HibernateValidatorProvider.checkEntity(services);
        if (!isExistService(services)) {
            mainServiceRepository.save(services);
        }
    }

    @Override
    @Transactional
    public void addSubServicesToServices(String serviceName, SubServices subServices) {
        HibernateValidatorProvider.checkEntity(subServices);
        MainServices services = mainServiceRepository.findServicesByName(serviceName).get();
        if (!isExistSubService(subServices)) {
            checkSubService(subServices, services);
        }
    }


    @Override
    @Transactional
    public void removeSubServiceByManager(String subServiceName) {

        SubServices subServicesFound = subServicesService.fetchSubServiceWithName(subServiceName).orElseThrow(
                () -> new CustomizedNotFoundException("this subService notfound"));
        subServicesService.removeOfServiceById(subServicesFound.getId());
    }

    @Override
    @Transactional
    public Optional<MainServices> findServiceByName(String name) {
        return mainServiceRepository.findServicesByName(name);
    }

    @Override
    @Transactional
    public List<MainServices> showAllService() {
        return mainServiceRepository.findAll();
    }

    private boolean isExistService(MainServices service) {
        if (mainServiceRepository.findServicesByName(service.getName()).isPresent()) {
            throw new RuntimeException("service name is duplicate");
        }
        return false;
    }

    private boolean isExistSubService(SubServices subService) {
        if (subServicesService.fetchSubServiceWithName(subService.getName()).isPresent()) {
            throw new RuntimeException("subService name is duplicate");
        }
        return false;
    }

    private void checkSubService(SubServices subServices, MainServices services) {
        services.addSubServices(subServices);
        subServicesService.createSubService(subServices); //save
        mainServiceRepository.save(services);  // update
    }
}
