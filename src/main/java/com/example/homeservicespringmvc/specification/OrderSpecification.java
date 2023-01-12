package com.example.homeservicespringmvc.specification;

import com.example.homeservicespringmvc.entity.capability.MainServices;
import com.example.homeservicespringmvc.entity.capability.Order;
import com.example.homeservicespringmvc.entity.capability.SubServices;
import com.example.homeservicespringmvc.entity.enums.OrderStatus;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class OrderSpecification {
    public static Specification<Order> hasDate(LocalDateTime start, LocalDateTime end) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            return criteriaBuilder.between(root.get("creationDate"),start,end);
        };
    }

    public static Specification<Order> hasStatus(String status) {
        OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
        return ((root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("status"), orderStatus);
        });
    }

    public static Specification<Order> hasSubService(SubServices subServices) {
        return ((root, query, criteriaBuilder) -> {
            Join<Object, Object> join = root.join("subServices");
            return criteriaBuilder.equal(join.get("name"), subServices.getName());
        });
    }

    public static Specification<Order> hasService(MainServices services) {
        return ((root, query, criteriaBuilder) -> {
            Join<Object, Object> join = root.join("subServices")
                    .join("services");
            return criteriaBuilder.equal(join.get("name"), services.getName());
        });
    }


}
