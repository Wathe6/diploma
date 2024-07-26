package com.envoi.diploma.controller;

import com.envoi.diploma.dto.GradeWorkDTO;
import com.envoi.diploma.dto.StudentWorksDTO;
import com.envoi.diploma.dto.TeacherSubjectDTO;
import com.envoi.diploma.dto.TeacherWorksDTO;
import com.envoi.diploma.model.*;
import com.envoi.diploma.repository.UserRepository;
import com.envoi.diploma.service.*;
import com.envoi.diploma.types.enums.roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Controller
public class MainController
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
    @GetMapping("user-info")
    public ResponseEntity<Map<String, Object>> get(){
        Map<String, Object> responseBody = new HashMap<>();

        MyUserDetails myUserDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Collection<roles> coll = (Collection<roles>) myUserDetails.getAuthorities();
        //Получаем id пользователя
        User user = userRepository.findById(myUserDetails.getId());
        Integer id = Integer.parseInt(user.getIdPerson());
        if(coll.contains(roles.TEACHER) || coll.contains(roles.HEAD))
        {
            responseBody.put("user","teacher");
            responseBody.put("info", getForTeacher(id));
        }
        if(coll.contains(roles.STUDENT)|| coll.contains(roles.HEADMAN))
        {
            responseBody.put("user","student");
            responseBody.put("info", getForStudent(id));
        }

        return ResponseEntity.ok(responseBody);
    }
    public List<StudentWorksDTO> getForStudent(Integer id) {
        //Получаем предметы студента
        List<Subject> subjects = studentService.getSubjectsByStudentId(id);

        List<StudentWorksDTO> dtos = new ArrayList<>();
        for (Subject subj : subjects) {
            StudentWorksDTO dto = new StudentWorksDTO();

            dto.setIdSubject(subj.getIdSubject());
            dto.setSubjectName(subj.getName());
            dto.setAbbreviation(subj.getAbbreviation());
            //Записываем работу + оценку текущую
            List<Work> works = workService.getWorksBySubjectId(subj.getIdSubject());
            for (Work w: works )
            {
                //Пришло время для пользователя видеть работу или нет
                if(w.getDateOpenAsDate().toInstant().isBefore(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))
                {
                    GradeWorkDTO gwdto = new GradeWorkDTO();
                    gwdto.setWork(w);
                    gwdto.setGrade(gradeService.findByStudentAndWork(studentService.getById(id), w));
                    dto.addGradeWork(gwdto);
                }
            }
            //Записываем всех преподавателей по предмету
            List<TeachSubj> teachers = teachSubjService.getTeachersBySubject(subj);
            for (TeachSubj t: teachers)
            {
                TeacherSubjectDTO tsdto = new TeacherSubjectDTO();
                tsdto.setTeacher(teacherService.getById(t.getId().getIdTeacher()));
                tsdto.setReviewer(t.getReviewer());
                tsdto.setLector(t.getLector());
                dto.addTeachers(tsdto);
            }
            dtos.add(dto);
        }
        return dtos;
    }
    public List<TeacherWorksDTO> getForTeacher(Integer id) {
        Teacher teacher = teacherService.getById(id);
        List<TeachSubj> teachSubjs = teachSubjService.getSubjectsByTeacher(teacher);

        List<TeacherWorksDTO> dto = new ArrayList<>();
        for (TeachSubj ts : teachSubjs) {
            TeacherWorksDTO twdto = new TeacherWorksDTO();

            twdto.setLector(ts.getLector());
            twdto.setReviewer(ts.getReviewer().getValue());

            Subject subject = subjectService.getById(ts.getId().getIdSubject());

            twdto.setIdSubject(subject.getIdSubject());
            twdto.setSubjectName(subject.getName());
            twdto.setAbbreviation(subject.getAbbreviation());
            twdto.setSubjtype(subject.getSubjtype().getValue());

            twdto.setGroup(
                    groupService.findByEdProgramAndSemester(
                        educationalProgramService.getById(subject.getIdProgram()),
                        subject.getSemester()
                    )
            .getIdGroup());
            //ALLERT

            List<Work> works = workService.getWorksBySubjectId(subject.getIdSubject());
            List<TeacherWorksDTO.SGWDTO> sgwdtoList = new ArrayList<>();
            for (Work work : works)
            {
                for (Grade grade : gradeService.findByWork(work))
                {
                    TeacherWorksDTO.SGWDTO sgwdto = new TeacherWorksDTO.SGWDTO();
                    sgwdto.setWork(work);
                    sgwdto.setGrade(grade);
                    sgwdto.setFio(grade.getId().getStudent());
                    sgwdtoList.add(sgwdto);
                }
            }

            sgwdtoList.sort(Comparator.comparing(sgwdto -> sgwdto.getWork().getNumber()));

            sgwdtoList.forEach(twdto::addSGWDTO);

            dto.add(twdto);
        }
        return dto;
    }
}
