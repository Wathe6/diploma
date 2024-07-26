package com.envoi.diploma.service;

import com.envoi.diploma.model.Subject;
import com.envoi.diploma.model.TeachSubj;
import com.envoi.diploma.model.Teacher;
import com.envoi.diploma.repository.TeachSubjRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeachSubjService extends BaseCRUDService<TeachSubj, TeachSubj.TeachSubjId>
{
    @Autowired
    private TeachSubjService(TeachSubjRepository repository)
    {
        super(repository);
    }
    @Autowired
    private TeachSubjRepository teachSubjRepository;
    public List<TeachSubj> getTeachersBySubject(Subject subject)
    {
        return teachSubjRepository.findByIdIdSubject(subject);
    }
    public List<TeachSubj> getSubjectsByTeacher(Teacher teacher)
    {
        return teachSubjRepository.findByIdIdTeacher(teacher);
    }
}