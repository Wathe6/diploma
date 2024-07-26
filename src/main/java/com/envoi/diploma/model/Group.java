package com.envoi.diploma.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Map;

@Entity
@Data
@Table(name = "groups", schema = "public")
public class Group
{
    public Group(){}
    public Group(Map<String, Object> data){
        this.idGroup = (String) data.get("idGroup");
        this.idStudent = new Student();
        this.idStudent.setIdStudent((Integer) data.get("idStudent"));
        this.idProgram = new EducationalProgram();
        this.idProgram.setIdProgram((Integer) data.get("idProgram"));
        this.idTeacher = new Teacher();
        this.idTeacher.setIdTeacher((Integer) data.get("idTeacher"));
        this.semester = (Integer) data.get("semester");
    }
    @Id
    private String idGroup;
    @ManyToOne
    private Student idStudent;
    @ManyToOne
    private EducationalProgram idProgram;
    @ManyToOne
    private Teacher idTeacher;
    private Integer semester;
    public int getIdProgram()
    {
        return idProgram.getIdProgram();
    }
    public int getIdTeacher()
    {
        return idTeacher.getIdTeacher();
    }
    public int getIdStudent(){
        return idStudent.getIdStudent();
    }
}
