package com.example.homeservicespringmvc.service;

import com.example.homeservicespringmvc.entity.capability.SubServices;

import java.util.Optional;

public interface SubServicesService {
   Optional<SubServices> fetchSubServiceWithName(String name);
  void createSubService(SubServices subServices);

   void removeOfServiceById(Long subId);

   void editSubService(String subServiceName, SubServices subServices);
}
