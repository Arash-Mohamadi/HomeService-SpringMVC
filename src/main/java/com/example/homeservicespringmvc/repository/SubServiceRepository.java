package com.example.homeservicespringmvc.repository;


import com.example.homeservicespringmvc.entity.capability.SubServices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface SubServiceRepository extends JpaRepository<SubServices, Long> {

    Optional<SubServices> findSubServicesByName(String name);

//    @Transactional
//    @Modifying
//    @Query("delete from SubServices sub where sub.name=:subServicesName and sub.services.id=:id")
//    void removeOfService(String subServicesName, Long serviceId);
}
