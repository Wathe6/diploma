package com.envoi.diploma.service;

import com.envoi.diploma.model.Subject;
import com.envoi.diploma.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class SubjectService extends BaseCRUDService<Subject, Integer>
{
    @Autowired
    public SubjectService(SubjectRepository repository)
    {
        super(repository);
    }
}
