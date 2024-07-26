package com.envoi.diploma.controller;

import com.envoi.diploma.dto.StudentProfileDTO;
import com.envoi.diploma.model.*;
import com.envoi.diploma.repository.UserRepository;
import com.envoi.diploma.service.*;
import com.envoi.diploma.types.enums.roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.*;

@Controller
public class ProfileController
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

    @GetMapping("profile")
    public ResponseEntity<Map<String, Object>> getProfile()
    {
        Map<String, Object> response = new HashMap<>();

        MyUserDetails myUserDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Collection<roles> coll = (Collection<roles>) myUserDetails.getAuthorities();
        //Получаем id пользователя
        User user = userRepository.findById(myUserDetails.getId());
        Integer id = Integer.parseInt(user.getIdPerson());
        if (coll.contains(roles.HEADMAN))
        {
            response.put("user", "headman");
            response.put("info", getForHeadman(id));
        } else if (coll.contains(roles.TEACHER))
        {
            response.put("user", "teacher");
            response.put("info", getForTeacher(id));
        } else if (coll.contains(roles.STUDENT))
        {
            response.put("user", "student");
            response.put("info", getForStudent(id));
        } else if (coll.contains(roles.HEAD))
        {
            response.put("user", "head");
            response.put("info", getForHead());
        }


        return ResponseEntity.ok(response);
    }

    public Map<String, Object> getForStudent(Integer id)
    {
        Student student = studentService.getById(id);
        List<Subject> subjects = studentService.getSubjectsByStudentId(id);

        List<StudentProfileDTO> response = new ArrayList<>();
        for (Subject subj : subjects)
        {
            StudentProfileDTO spdto = new StudentProfileDTO();

            spdto.setSubject(subj.getAbbreviation());
            spdto.setWork(new ArrayList<>());
            spdto.setGrade(new ArrayList<>());
            //Записываем работу + текущие оценки
            List<Work> works = workService.getWorksBySubjectId(subj.getIdSubject());
            for (Work w : works)
            {
                spdto.getWork().add(w.getWorktype().getValue() + "" + w.getNumber());
                spdto.getGrade().add(gradeService.findByStudentAndWork(studentService.getById(id), w).getGrade());
                response.add(spdto);
            }

        }

        Map<String, Object> map = new HashMap<>();
        map.put("fio", student.getSurname() + " " + student.getName().charAt(0) + ". " + student.getPatronymic().charAt(0) + ".");
        map.put("subjects", response);

        return map;
    }

    public Map<String, Object> getForHeadman(Integer id)
    {
        Student student = studentService.getById(id);
        Group group = groupService.findByStudent(student);
        List<Student> students = studentService.findByGroup(group);

        Map<String, Object> response = new HashMap<>();

        Map<String, Object> grades = new HashMap<>();
        for (Student st : students)
        {
            List<StudentProfileDTO> dtos = (List<StudentProfileDTO>) getForStudent(st.getIdStudent()).get("subjects");

            Map<String, Object> grade = new HashMap<>();
            for (StudentProfileDTO spdto : dtos)
            {
                grade.put(spdto.getSubject(), spdto.getGrade());
            }
            grades.put(
                    st.getSurname() + " " +
                            st.getName().charAt(0) + ". " +
                            st.getPatronymic().charAt(0) + ".",
                    grade);
        }
        Map<String, Object> works = new HashMap<>();
        List<StudentProfileDTO> dtos = (List<StudentProfileDTO>) getForStudent(students.get(0).getIdStudent()).get("subjects");
        for (StudentProfileDTO spdto : dtos)
        {
            works.put(spdto.getSubject(), spdto.getWork());
        }
        response.put("works", works);
        response.put("grades", grades);
        return response;
    }

    public Map<String, Object> getForTeacher(Integer id)
    {
        Teacher teacher = teacherService.getById(id);
        List<TeachSubj> teachSubjs = teachSubjService.getSubjectsByTeacher(teacher);
        Map<String, Object> map = new HashMap<>();
        for (TeachSubj ts : teachSubjs)
        {
            Subject subject = subjectService.getById(ts.getId().getIdSubject());
            Map<String, List<Integer>> grades = new HashMap<>();

            List<Work> works = workService.getWorksBySubjectId(subject.getIdSubject());
            List<String> workNames = new ArrayList<>();
            for (Work w : works)
            {
                workNames.add(w.getWorktype().getValue() + "" + w.getNumber());
                List<Grade> gs = gradeService.findByWork(w);

                for (Grade g : gs)
                {
                    Student st = g.getId().getStudent();
                    String studentKey = st.getSurname() + " " +
                            st.getName().charAt(0) + ". " +
                            st.getPatronymic().charAt(0) + ".";

                    List<Integer> studentGrades = grades.getOrDefault(studentKey, new ArrayList<>());

                    studentGrades.add(g.getGrade());

                    grades.put(studentKey, studentGrades);
                }
            }
            Map<String, Object> gw = new HashMap<>();
            gw.put("works", workNames);
            gw.put("grades", grades);
            map.put(subject.getAbbreviation() + " (" +
                    groupService.findByEdProgramAndSemester(
                            subject.getProgram(), subject.getSemester()
                    ).getIdGroup() + ")", gw);
        }
        //аббревиатура + (группа)
        return map;
    }

    public Map<String, Object> getForHead()
    {
        List<Group> groups = groupService.findAll();
        Map<String, Object> answer = new HashMap<>();
        for (Group group : groups)
        {
            List<Student> students = studentService.findByGroup(group);

            Map<String, Object> response = new HashMap<>();

            Map<String, Object> grades = new HashMap<>();
            for (Student st : students)
            {
                List<StudentProfileDTO> dtos = (List<StudentProfileDTO>) getForStudent(st.getIdStudent()).get("subjects");

                Map<String, Object> grade = new HashMap<>();
                for (StudentProfileDTO spdto : dtos)
                {
                    grade.put(spdto.getSubject(), spdto.getGrade());
                }
                grades.put(
                        st.getSurname() + " " +
                                st.getName().charAt(0) + ". " +
                                st.getPatronymic().charAt(0) + ".",
                        grade);
            }
            Map<String, Object> works = new HashMap<>();
            List<StudentProfileDTO> dtos = (List<StudentProfileDTO>) getForStudent(students.get(0).getIdStudent()).get("subjects");
            for (StudentProfileDTO spdto : dtos)
            {
                works.put(spdto.getSubject(), spdto.getWork());
            }
            response.put("works", works);
            response.put("grades", grades);

            answer.put(group.getIdGroup(), response);
        }
        return answer;
    }
}