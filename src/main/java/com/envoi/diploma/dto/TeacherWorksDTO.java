package com.envoi.diploma.dto;

import com.envoi.diploma.model.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TeacherWorksDTO
{
    private Integer idSubject;
    private String group;
    private String subjectName;
    private String abbreviation;
    private String lector;
    private String reviewer;
    private String subjtype;
    @Getter
    @Setter
    public static class SGWDTO{
        private String fio;
        private Work work;
        private Grade grade;
        public void setFio(Student st){
            this.fio = st.getSurname() + " " + st.getName().charAt(0) + ". " + st.getPatronymic().charAt(0) + ".";
        }
    }
    private List<SGWDTO> sgwdtos = new ArrayList<>();
    public void addSGWDTO(SGWDTO sgwdto){
        sgwdtos.add(sgwdto);
    }
}
