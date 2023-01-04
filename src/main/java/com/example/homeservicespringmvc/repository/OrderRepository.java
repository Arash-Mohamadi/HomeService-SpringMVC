package com.example.homeservicespringmvc.repository;

import com.example.homeservicespringmvc.entity.capability.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("from Order o where o.status=\"OrderStatus.PENDING_FOR_SPECIALIST_SUGGESTION\" " +
            "or o.status=\"PENDING_FOR_SPECIALIST_SELECTION\" " +
            "and o.subServices.id=:id")
    List<Order> showOrder(Long id);
}
