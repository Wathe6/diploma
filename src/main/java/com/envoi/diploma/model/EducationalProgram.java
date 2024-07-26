package com.envoi.diploma.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Map;

@Entity
@Data
@Table(name = "educational_programs", schema = "public")
public class EducationalProgram
{
    public EducationalProgram(){}
    public EducationalProgram(Map<String, Object> data)
    {
        this.idProgram = (Integer) data.get("idProgram");
        this.idFieldCode = new FieldOfStudy();
        this.idFieldCode.setIdFieldCode((String) data.get("idFieldCode"));
        this.name = (String) data.get("name");
        this.abbreviation = (String) data.get("abbreviation");
        this.profile = (String) data.get("profile");
    }
    @Id
    private Integer idProgram;
    @ManyToOne
    private FieldOfStudy idFieldCode;
    private String name;
    private String abbreviation;
    private String profile;
    public void setIdProgram(Integer id){
        this.idProgram = id;
    }
    public String getIdFieldCode()
    {
        return idFieldCode.getIdFieldCode();
    }
}
