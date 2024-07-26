package com.envoi.diploma.model;

import com.envoi.diploma.types.enums.AbstractENUM;
import com.envoi.diploma.types.enums.academicdegrees;
import com.envoi.diploma.types.enums.statuses;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Entity
@Data
@Table(name = "grades", schema = "public")
public class Grade
{
    public Grade(){
        this.activity = new ArrayList<>();
    }
    public Grade(int grade, statuses status){
        this.grade = grade;
        this.status = status;
    }
    public Grade(Map<String, Object> data)
    {
        this.id = new GradeId(
                (Integer) data.get("idStudent"),
                (Integer) data.get("idWork"),
                (Integer) data.get("idTeacher")
        );
        this.activity = (ArrayList<String>) data.getOrDefault("activity", new ArrayList<>());

        this.grade = (Integer) data.get("grade");
        setStatus((String) data.get("status"));
        this.reference = (String) data.get("reference");
    }
    @Getter
    @Setter
    @Embeddable
    public static class GradeId implements Serializable
    {
        public GradeId(){}
        public GradeId(Integer idstudent, Integer idwork, Integer idteacher)
        {
            this.idStudent = new Student();
            this.idStudent.setIdStudent(idstudent);

            this.idWork = new Work();
            this.idWork.setIdWork(idwork);

            this.idTeacher = new Teacher();
            this.idTeacher.setIdTeacher(idteacher);
        }
        @ManyToOne
        private Student idStudent;
        @ManyToOne
        private Work idWork;
        @ManyToOne
        private Teacher idTeacher;
        public Integer getIdStudent()
        {
            return idStudent.getIdStudent();
        }
        public Integer getIdWork()
        {
            return idWork.getIdWork();
        }
        public Integer getIdTeacher() {
            return idTeacher.getIdTeacher();
        }
        @JsonIgnore
        public Student getStudent(){
            return idStudent;
        }
    }
    @EmbeddedId
    @JsonUnwrapped
    private GradeId id;
    private ArrayList<String> activity;
    private Integer grade;
    private String reference;
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Basic(optional = false)
    private statuses status;
    public Integer getGrade() {
        return Objects.requireNonNullElse(grade, 0);
    }
    public String getReference() {
        return Objects.requireNonNullElse(reference, "NULL");
    }
    public void setGrade(Integer number){
        if(Objects.isNull(activity))
            this.activity = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        activity.add("[" + LocalDateTime.now().format(formatter) + "] Поставлена оценка: " + number + ";\n");
        grade = number;
    }
    public void setStatuses(statuses stat) {
        if(Objects.isNull(activity))
            this.activity = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        activity.add("[" + LocalDateTime.now().format(formatter) + "] Поставлен статус: " + stat.getValue() + ";\n");
        this.status = stat;
    }
    private void setStatus(String status) {
        this.status = AbstractENUM.fromValue(statuses.class, status);
    }
}