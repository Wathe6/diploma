package com.envoi.diploma.service;

import com.envoi.diploma.model.*;
import com.envoi.diploma.repository.GradeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.ref.Reference;
import java.util.ArrayList;
import java.util.List;

@Service
public class GradeService extends BaseCRUDService<Grade, Grade.GradeId>
{
    @Autowired
    private GradeService(GradeRepository repository) {
        super(repository);
    }
    @Autowired
    private GradeRepository gradeRepository;
    public Grade findByStudentAndWork(Student student, Work work){
        return gradeRepository.findByIdIdStudentAndIdIdWork(student, work);
    }
    public List<Grade> findByTeacher(Teacher teacher)
    {
        return gradeRepository.findByIdIdTeacher(teacher);
    }
    public Grade findByReference(String reference)
    {
        return gradeRepository.findByReference(reference);
    }
    public void deleteByWork(Work work){
        gradeRepository.deleteAll(gradeRepository.findByIdIdWork(work));
    }
    public List<Grade> findByWork(Work work){
        return gradeRepository.findByIdIdWork(work);
    }
}
