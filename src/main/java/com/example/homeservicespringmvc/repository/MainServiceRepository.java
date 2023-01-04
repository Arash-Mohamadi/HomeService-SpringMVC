package com.example.homeservicespringmvc.repository;


import com.example.homeservicespringmvc.entity.capability.MainServices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface MainServiceRepository extends JpaRepository<MainServices,Long> {

    Optional<MainServices> findServicesByName(String name);
}
