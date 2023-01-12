package com.example.homeservicespringmvc.repository.impl;

import com.example.homeservicespringmvc.entity.users.Customer;
import com.example.homeservicespringmvc.entity.users.Person;
import com.example.homeservicespringmvc.repository.CustomerCustomRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class CustomerCustomRepositoryImpl implements CustomerCustomRepository {

    private final EntityManager entityManager;
    @Override
    public List<Customer> findCustomerByFilter(Map<String,String> filters) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Customer> criteriaQuery = criteriaBuilder.createQuery(Customer.class);
        Root<Customer> customer = criteriaQuery.from(Customer.class);
        List<Predicate> predicateList = new ArrayList<>();
        for (Map.Entry<String, String> filter : filters.entrySet()){
            if (filter.getKey().equals("firstname")){
                Predicate firstnamePredicate = criteriaBuilder.equal(customer.get("firstname"), filter.getValue());
                predicateList.add(firstnamePredicate);
            }
            if (filter.getKey().equals("lastname")){
                Predicate lastnamePredicate = criteriaBuilder.equal(customer.get("lastname"), filter.getValue());
                predicateList.add(lastnamePredicate);
            }
            if (filter.getKey().equals("email")){
                Predicate emailPredicate = criteriaBuilder.equal(customer.get("email"), filter.getValue());
                predicateList.add(emailPredicate);
            }
        }
        criteriaQuery.select(customer).where(predicateList.toArray(new Predicate[0]));
        return entityManager.createQuery(criteriaQuery).getResultList();
    }
}
