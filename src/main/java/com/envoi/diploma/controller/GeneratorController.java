package com.envoi.diploma.controller;

import com.envoi.diploma.repository.UserRepository;
import com.envoi.diploma.service.*;
import com.envoi.diploma.model.*;
import com.envoi.diploma.types.enums.statuses;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("create")
public class GeneratorController
{
    @Autowired
    private StudentService studentService;
    @Autowired
    private WorkService workService;
    @Autowired
    private TeachSubjService teachSubjService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GradeService gradeService;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private SubjectService subjectService;
    @Autowired
    private GroupService groupService;
    @Autowired
    private EducationalProgramService educationalProgramService;

    @GetMapping("grades")
    public ResponseEntity<String> generateGrades(){
        List<Subject> subjects = subjectService.findAll();
        StringBuilder buffer = new StringBuilder();

        try
        {
            for (Subject subject : subjects)
            {
                Group group = groupService.findByEdProgramAndSemester(subject.getProgram(),
                        subject.getSemester());
                List<Student> students = studentService.findByGroup(group);
                List<TeachSubj> teachSubjs = teachSubjService.getTeachersBySubject(subject);
                Teacher teacher = teacherService.getById(teachSubjs.get(0).getId().getIdTeacher());
                List<Work> works = workService.getWorksBySubjectId(subject.getIdSubject());
                for (Work work : works)
                {
                    buffer.append(work).append("\n");
                    Grade grade = new Grade(0, statuses.NOT_COMPLETED);
                    for (Student student : students)
                    {
                        //idStudent/idSubject/worktype+number.pdf
                        grade.setReference(
                                "/" +
                                student.getIdStudent() + "/" +
                                subject.getIdSubject() + "/" +
                                work.getWorktype().getValue() + "" +
                                work.getNumber() + ".pdf"
                        );
                        grade.setId(new Grade.GradeId(student.getIdStudent(), work.getIdWork(), teacher.getIdTeacher()));
                        gradeService.save(grade);
                    }
                }
            }
        } catch (Exception exception){
            System.out.println(exception);
        } finally
        {
            System.out.println(buffer);
        }
        return ResponseEntity.ok("");
    }
    @PostMapping("work")
    public ResponseEntity<String> generateWork(@RequestBody Map<String, Object> entity) throws ParseException
    {
        Work work = new Work(entity);
        //создать reference
        Subject subject = subjectService.getById(work.getIdSubject());
        work.setReference("/" + subject.getIdSubject() + "/" +
                work.getWorktype().getValue() + work.getNumber() +
                ".pdf");
        //сохранили работу
        work = workService.save(work);
        //создать grades
        Group group = groupService.findByEdProgramAndSemester(subject.getProgram(),
                subject.getSemester());
        List<Student> students = studentService.findByGroup(group);

        List<TeachSubj> teachSubjs = teachSubjService.getTeachersBySubject(subject);
        Teacher teacher = teacherService.getById(teachSubjs.get(0).getId().getIdTeacher());

        Grade grade = new Grade(0, statuses.NOT_COMPLETED);
        for (Student student : students)
        {
            //idStudent\idSubject\worktype+number.pdf
            grade.setReference(
                    "/" + student.getIdStudent() + "/" +
                            subject.getIdSubject() + "/" +
                            work.getWorktype().getValue() + "" +
                            work.getNumber() + ".pdf"
            );
            grade.setId(new Grade.GradeId(student.getIdStudent(), work.getIdWork(), teacher.getIdTeacher()));
            gradeService.save(grade);
        }

        return ResponseEntity.ok("");
    }
}
