package com.envoi.diploma.dto;

import com.envoi.diploma.model.Teacher;
import com.envoi.diploma.types.enums.reviewers;
import lombok.*;

@Getter
@Setter
public class TeacherSubjectDTO
{
    private Teacher teacher = new Teacher();
    private String lector;
    private reviewers reviewer;
}
