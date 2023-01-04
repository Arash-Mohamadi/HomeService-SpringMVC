package com.example.homeservicespringmvc.repository;


import com.example.homeservicespringmvc.entity.users.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ManagerRepository extends JpaRepository<Manager,Long> {

    Optional<Manager> findManagerByUsername(String username);
}
