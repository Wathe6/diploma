package com.envoi.diploma.model;

import com.envoi.diploma.types.enums.*;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.util.Map;

@Entity
@Data
@Table(name = "teach_subj", schema = "public")
public class TeachSubj
{
    public TeachSubj(){}
    public TeachSubj(Map<String, Object> data)
    {
        this.id = new TeachSubjId(
                (Integer) data.get("idTeacher"),
                (Integer) data.get("idSubject")
        );
        this.lector = Boolean.parseBoolean(String.valueOf(data.get("lector")));
        setReviewer((String) data.get("reviewer"));
    }
    @Embeddable
    public static class TeachSubjId implements Serializable
    {
        @ManyToOne
        private Teacher idTeacher;
        @ManyToOne
        private Subject idSubject;
        public TeachSubjId() {
        }
        public TeachSubjId(Teacher tch, Subject sbj)
        {
            this.idTeacher = tch;
            this.idSubject = sbj;
        }
        public TeachSubjId(Integer idTeacher, Integer idSubject) {
            Teacher tch = new Teacher();
            tch.setIdTeacher(idTeacher);
            this.idTeacher = tch;

            Subject sbj = new Subject();
            sbj.setIdSubject(idSubject);
            this.idSubject = sbj;
        }
        public Integer getIdTeacher()
        {
            return idTeacher.getIdTeacher();
        }
        public Integer getIdSubject()
        {
            return idSubject.getIdSubject();
        }
    }

    @EmbeddedId
    @JsonUnwrapped
    private TeachSubjId id;
    private boolean lector;
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Basic(optional = false)
    private reviewers reviewer;

    public String getLector() {
        if(lector)
            return "Да";
        else
            return "Нет";
    }
    public void setReviewer(String rev)
    {
        this.reviewer = AbstractENUM.fromValue(reviewers.class, rev);
    }
}