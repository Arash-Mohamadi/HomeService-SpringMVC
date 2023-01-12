package com.example.homeservicespringmvc.service;


import com.example.homeservicespringmvc.entity.capability.MainServices;
import com.example.homeservicespringmvc.entity.capability.SubServices;

import java.util.List;
import java.util.Optional;


public interface MainServicesService {
    void addService(MainServices services);
   List<MainServices> showAllService();
   void addSubServicesToServices(String serviceName, SubServices subServices);
   void removeSubServiceByManager(String subServiceName);
    Optional<MainServices> findServiceByName(String name);
}
