package com.example.homeservicespringmvc.repository;

import com.example.homeservicespringmvc.entity.capability.Credit;
import com.example.homeservicespringmvc.entity.capability.Order;
import com.example.homeservicespringmvc.entity.users.Customer;
import com.example.homeservicespringmvc.entity.users.Specialist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpecialistRepository extends JpaRepository<Specialist, Long>
        , JpaSpecificationExecutor<Specialist> {
    Optional<Specialist> findSpecialistByUsername(String username);

    Optional<Specialist> findSpecialistByEmail(String email);

    @Query("select s from Specialist s where s.avg >= :score")
    List<Specialist> fetchSpecialistByScoreAvg(Integer score);

    @Query("""
            select s.credit
            from Specialist s 
            where s.id=:specialistId
                        """)
    Credit viewBalance(Long specialistId);



    @Query("""
                        select o
                        from Order o
                        join Specialist s
                        on o.specialist.id=s.id
            where s.username =:username
                         """)
    List<Order> viewOrders(String username);
}

