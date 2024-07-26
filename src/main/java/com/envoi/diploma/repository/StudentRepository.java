package com.envoi.diploma.repository;

import com.envoi.diploma.model.Group;
import com.envoi.diploma.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer>{
    Student findByIdStudent(Integer idStudent);
    List<Student> findByIdGroupOrderBySurnameAscNameAscPatronymicAsc(Group group);
}
