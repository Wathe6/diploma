package com.envoi.diploma.model;

import com.envoi.diploma.types.enums.*;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Map;

@Entity
@Data
@Table(name = "teachers", schema = "public")
public class Teacher
{
    public Teacher(){}
    public Teacher(Map<String, Object> data)
    {
        this.setIdTeacher(((Integer) data.get("idTeacher")));
        this.setDepartment((String) data.get("department"));
        this.setPosition((String) data.get("position"));
        this.setAcademicDegree((String) data.get("academicdegree"));
        this.setAcademicTitle((String) data.get("academictitle"));
        this.setSurname((String) data.get("surname"));
        this.setName((String) data.get("name"));
        this.setPatronymic((String) data.get("patronymic"));
    }
    @Id
    private Integer idTeacher;
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Basic(optional = false)
    private departments department;
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Basic(optional = false)
    private positions position;
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Basic(optional = false)
    private academicdegrees academicdegree;
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Basic(optional = false)
    private academictitles academictitle;
    private String surname;
    private String name;
    private String patronymic;

    public void setDepartment(String department){
        this.department = AbstractENUM.fromValue(departments.class, department);
    }
    public void setPosition(String position){
        this.position = AbstractENUM.fromValue(positions.class, position);
    }
    public void setAcademicDegree(String degree){
        if(degree.equals("NULL"))
            this.academicdegree = academicdegrees.UNKNOWN;
        else
            this.academicdegree = AbstractENUM.fromValue(academicdegrees.class, degree);
    }
    public void setAcademicTitle(String title){
        if(title.equals("NULL"))
            this.academictitle = academictitles.UNKNOWN;
        else
            this.academictitle = AbstractENUM.fromValue(academictitles.class, title);
    }
}