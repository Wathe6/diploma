package com.envoi.diploma.repository;

import com.envoi.diploma.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Integer> {
    List<Subject> findBySemesterAndIdProgram_IdProgram(Integer semester, Integer id_program);
}
