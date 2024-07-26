package com.envoi.diploma.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class StudentProfileDTO
{
    private String subject;
    private List<String> work;
    private List<Integer> grade;
}
