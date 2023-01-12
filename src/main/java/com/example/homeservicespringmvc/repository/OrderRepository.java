package com.example.homeservicespringmvc.repository;

import com.example.homeservicespringmvc.entity.capability.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>
        , JpaSpecificationExecutor<Order> {

    @Query("from Order o " +
            "join SubServices sub " +
            "join Specialist sp " +
            "where o.status=\"PENDING_FOR_SPECIALIST_SUGGESTION\" " +
            "or o.status=\"PENDING_FOR_SPECIALIST_SELECTION\" " +
            "and sp.id=:specialistId")
    List<Order> showOrderBelongToSpecialist(Long specialistId);


    @Query("from Order o " +
            "where o.status=\"PENDING_FOR_SPECIALIST_SUGGESTION\" " +
            "or o.status=\"PENDING_FOR_SPECIALIST_SELECTION\" " +
            "and o.subServices.id=:subServiceId")
    List<Order> showOrderBelongToSubService(Long subServiceId);
}
