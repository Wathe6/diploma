package com.envoi.diploma.repository;

import com.envoi.diploma.model.Grade;
import com.envoi.diploma.model.Student;
import com.envoi.diploma.model.Teacher;
import com.envoi.diploma.model.Work;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Grade.GradeId>{
    Grade findByIdIdStudentAndIdIdWork(Student student, Work work);
    List<Grade> findByIdIdTeacher(Teacher teacher);
    Grade findByReference(String reference);
    List<Grade> findByIdIdWork(Work work);
}