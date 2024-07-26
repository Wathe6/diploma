package com.envoi.diploma.service;

import com.envoi.diploma.model.*;
import com.envoi.diploma.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService extends BaseCRUDService<Student, Integer>
{
    @Autowired
    public StudentService(StudentRepository repository)
    {
        super(repository);
    }
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MyUserDetailsService myUserDetailsService;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private SubjectRepository subjectRepository;
    public List<Subject> getSubjectsByStudentId(Integer idPerson) {
        Student student = studentRepository.findByIdStudent(idPerson);
        if (student == null) {
            throw new RuntimeException("Студент не найден");
        }

        Group group = groupRepository.findByIdGroup(student.getIdGroup());
        if (group == null) {
            throw new RuntimeException("Группа не найдена");
        }

        return subjectRepository.findBySemesterAndIdProgram_IdProgram(group.getSemester(), group.getIdProgram());
    }
    public List<Student> findByGroup(Group group){
        return studentRepository.findByIdGroupOrderBySurnameAscNameAscPatronymicAsc(group);
    }
}
