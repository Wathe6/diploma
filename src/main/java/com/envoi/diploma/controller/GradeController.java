package com.envoi.diploma.controller;

import com.envoi.diploma.model.Grade;
import com.envoi.diploma.model.MyUserDetails;
import com.envoi.diploma.service.GradeService;
import com.envoi.diploma.service.StudentService;
import com.envoi.diploma.service.TeacherService;
import com.envoi.diploma.service.WorkService;
import com.envoi.diploma.types.enums.roles;
import com.envoi.diploma.types.enums.statuses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;

@Controller
@RequestMapping("/grade/")
public class GradeController
{
    @Autowired
    private GradeService gradeService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private WorkService workService;
    @Autowired
    private TeacherService teacherService;

    @PostMapping("/{idStudent}/{idWork}/{number}")
    public ResponseEntity<String> setGrade(
            @PathVariable Integer idStudent,
            @PathVariable Integer idWork,
            @PathVariable Integer number
    ){
        try
        {
            MyUserDetails myUserDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Collection<roles> coll = (Collection<roles>) myUserDetails.getAuthorities();
            if(coll.contains(roles.TEACHER) || coll.contains(roles.HEAD)){
                Grade grade = gradeService.findByStudentAndWork(
                        studentService.getById(idStudent),
                        workService.getById(idWork)
                );
                grade.setGrade(number);
                grade.setStatuses(statuses.COMPLETED);
                gradeService.save(grade);
                return ResponseEntity.ok("");
            }
            else
                return ResponseEntity.ok("Not enough authorities.");
        }
        catch (Exception e)
        {
            throw new RuntimeException("Error processing the request: " + e.getMessage());
        }
    }
}
