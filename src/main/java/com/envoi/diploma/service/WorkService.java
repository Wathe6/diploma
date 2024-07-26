package com.envoi.diploma.service;

import com.envoi.diploma.model.Work;
import com.envoi.diploma.repository.WorkRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkService extends BaseCRUDService<Work, Integer>
{
    @Autowired
    private WorkService(WorkRepository repository) { super(repository);}
    @Autowired
    private WorkRepository workRepository;
    @Autowired
    private GradeService gradeService;
    public List<Work> getWorksBySubjectId(Integer idSubject)
    {
        return workRepository.findByIdSubject_IdSubjectOrderByNumber(idSubject);
    }
    public Work findByReference(String reference){
        return workRepository.findByReference(reference);
    }
}
