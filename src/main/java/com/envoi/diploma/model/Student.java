package com.envoi.diploma.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Map;

@Entity
@Data
@Table(name = "students", schema = "public")
public class Student
{
    public Student(){}
    public Student(Map<String, Object> data){
        this.idStudent = (Integer) data.get("idStudent");
        this.surname = ((String) data.get("surname"));
        this.name = ((String) data.get("name"));
        this.patronymic = ((String) data.get("patronymic"));
        this.idGroup = new Group();
        this.idGroup.setIdGroup((String) data.get("idGroup"));
    }
    @Id
    private Integer idStudent;
    private String surname;
    private String name;
    private String patronymic;
    @ManyToOne
    private Group idGroup;
    public String getIdGroup()
    {
        return idGroup.getIdGroup();
    }
    @Override
    public String toString() {
        return "Student{" +
                "idStudent=" + idStudent +
                ", surname='" + surname + '\'' +
                ", name='" + name + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", idGroup=" + (idGroup != null ? idGroup.getIdGroup() : "null") +
                '}';
    }
}
