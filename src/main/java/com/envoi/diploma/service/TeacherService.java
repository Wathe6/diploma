package com.envoi.diploma.service;

import com.envoi.diploma.model.Teacher;
import com.envoi.diploma.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class TeacherService extends BaseCRUDService<Teacher, Integer>
{
    @Autowired
    private TeacherService(TeacherRepository repository)
    {
        super(repository);
    }
}
