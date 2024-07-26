package com.envoi.diploma.service;

import com.envoi.diploma.model.FieldOfStudy;
import com.envoi.diploma.repository.FieldOfStudyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class FieldOfStudyService extends BaseCRUDService<FieldOfStudy, String>
{
    @Autowired
    public FieldOfStudyService(FieldOfStudyRepository repository)
    {
        super(repository);
    }
}

