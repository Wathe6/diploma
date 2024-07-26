package com.envoi.diploma.repository;

import com.envoi.diploma.model.EducationalProgram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EducationalProgramRepository extends JpaRepository<EducationalProgram, Integer> {
}
