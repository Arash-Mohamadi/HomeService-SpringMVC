package com.example.homeservicespringmvc.specification;

import com.example.homeservicespringmvc.entity.capability.Order;
import com.example.homeservicespringmvc.entity.capability.SubServices;
import com.example.homeservicespringmvc.entity.enums.UserRole;
import com.example.homeservicespringmvc.entity.users.Specialist;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

@Component
public class SpecialistSpecification {





    public static Specification<Specialist> hasFirstname(String firstname) {
        return ((root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("firstname"), firstname);
        });
    }

    public static Specification<Specialist> hasLastname(String lastname) {
        return ((root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("lastname"), lastname);
        });
    }

    public static Specification<Specialist> hasEmail(String email) {
        return ((root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("email"), email);
        });
    }


    public static Specification<Specialist> greaterThanOrEqualToScore(Integer score) {
        return ((root, query, criteriaBuilder) -> {
            return criteriaBuilder.greaterThanOrEqualTo(root.get("avg"), score);
        });
    }

    public static Specification<Specialist> containSubService(SubServices subServices) {
        return ((root, query, criteriaBuilder) -> {
            Join<Object, Object> join = root.join("subServicesSet");
          return   criteriaBuilder.equal(join.get("name"),subServices.getName());
        });
    }

    public static Specification<Specialist> hasRole(String userType) {
        UserRole type = UserRole.valueOf(userType);
        return ((root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("userType"), type);
        });
    }

    public static Specification<Specialist> searchSpecialist(Map<String, String> filters) {
        Specification<Specialist> specification = Specification.where(null);
        for (Map.Entry<String, String> entry : filters.entrySet()) {
                specification = specification.and((specialist, cq, cb) ->
                        cb.equal(specialist.get(entry.getKey()), entry.getValue()));
        }
        return specification;
    }

    public static Specification<Specialist> dateOfSignupReport(LocalDateTime dateOfSignup){
        return (root, criteriaQuery, criteriaBuilder) -> {
            return criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo
                    (root.get("dateOfSignup"), dateOfSignup));
        };
    }

    public static Specification<Specialist> greaterThanOrEqualOrder(Long count){

        return (root, criteriaQuery, criteriaBuilder) -> {
            criteriaQuery.distinct(true);
            Subquery<Long> subQuery = criteriaQuery.subquery(Long.class);
            Root<Order> from = subQuery.from(Order.class);
            subQuery.where(criteriaBuilder.equal(from.get("specialist").get("id"),root.get("id")));
            return criteriaBuilder.greaterThanOrEqualTo(subQuery,count);
        };
    }




}
