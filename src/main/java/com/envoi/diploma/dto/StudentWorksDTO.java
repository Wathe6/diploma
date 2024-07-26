package com.envoi.diploma.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class StudentWorksDTO
{
    private Integer idSubject;
    private String subjectName;
    private String abbreviation;
    private List<GradeWorkDTO> gradeWork = new ArrayList<>();
    private List<TeacherSubjectDTO> teachers = new ArrayList<>();
    public void addGradeWork(GradeWorkDTO gwdto){
        this.gradeWork.add(gwdto);
    }
    public void addTeachers(TeacherSubjectDTO tsdto){
        this.teachers.add(tsdto);
    }
}
