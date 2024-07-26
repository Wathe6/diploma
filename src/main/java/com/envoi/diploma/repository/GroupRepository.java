package com.envoi.diploma.repository;

import com.envoi.diploma.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<Group, String>{
    Group findByIdGroup(String id_group);
    Group findByIdProgramAndSemester(EducationalProgram ep, Integer semester);
    Group findByIdStudent(Student idStudent);
}
