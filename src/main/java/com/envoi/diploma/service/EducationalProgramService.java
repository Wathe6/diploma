package com.envoi.diploma.service;

import com.envoi.diploma.model.EducationalProgram;
import com.envoi.diploma.repository.EducationalProgramRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class EducationalProgramService extends BaseCRUDService<EducationalProgram, Integer>
{
    @Autowired
    public EducationalProgramService(EducationalProgramRepository repository) {
        super(repository);
    }
}