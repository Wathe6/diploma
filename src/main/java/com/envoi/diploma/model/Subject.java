package com.envoi.diploma.model;

import com.envoi.diploma.types.enums.AbstractENUM;
import com.envoi.diploma.types.enums.subjtypes;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Map;

@Entity
@Data
@Table(name = "subjects", schema = "public")
public class Subject
{
    public Subject(){}
    public Subject(Map<String, Object> data) {
        this.idSubject = (Integer) data.get("idSubject");
        this.idProgram = new EducationalProgram();
        this.idProgram.setIdProgram((Integer) data.get("idProgram"));
        this.name = (String) data.get("name");
        this.abbreviation = (String) data.get("abbreviation");
        this.semester = (Integer) data.get("semester");
        this.setSubjtype((String) data.get("subjtype"));
    }
    @Id
    private Integer idSubject;
    @ManyToOne
    private EducationalProgram idProgram;
    private String name;
    private String abbreviation;
    private Integer semester;
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Basic(optional = false)
    private subjtypes subjtype;

    public int getIdProgram()
    {
        return idProgram.getIdProgram();
    }
    public void setSubjtype(String type){
        this.subjtype = AbstractENUM.fromValue(subjtypes.class, type);
    }
    @JsonIgnore
    public EducationalProgram getProgram(){
        return idProgram;
    }
}
