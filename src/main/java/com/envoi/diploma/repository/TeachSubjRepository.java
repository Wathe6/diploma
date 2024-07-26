package com.envoi.diploma.repository;

import com.envoi.diploma.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeachSubjRepository extends JpaRepository<TeachSubj, TeachSubj.TeachSubjId>{
    List<TeachSubj> findByIdIdSubject(Subject subject);
    List<TeachSubj> findByIdIdTeacher(Teacher teacher);
}
