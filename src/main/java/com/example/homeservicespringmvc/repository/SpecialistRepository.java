package com.example.homeservicespringmvc.repository;

import com.example.homeservicespringmvc.entity.users.Specialist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SpecialistRepository extends JpaRepository<Specialist,Long> {
    Optional<Specialist> findSpecialistByUsername(String username);
    Optional<Specialist> findSpecialistByEmail(String email);
    @Query("select s from Specialist s where s.Avg >= :score")
    List<Specialist> fetchSpecialistByScoreAvg(Integer score);
}

