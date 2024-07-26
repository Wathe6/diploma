package com.envoi.diploma.repository;

import com.envoi.diploma.model.Work;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkRepository extends JpaRepository<Work, Integer>{
    List<Work> findByIdSubject_IdSubjectOrderByNumber(Integer idSubject);
    Work findByReference(String reference);
}