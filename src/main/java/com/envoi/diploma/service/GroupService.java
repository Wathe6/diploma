package com.envoi.diploma.service;

import com.envoi.diploma.model.*;
import com.envoi.diploma.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class GroupService extends BaseCRUDService<Group, String>
{
    @Autowired
    public GroupService(GroupRepository repository)
    {
        super(repository);
    }
    @Autowired GroupRepository groupRepository;
    public Group findByEdProgramAndSemester(EducationalProgram ep, Integer semester){
        return groupRepository.findByIdProgramAndSemester(ep, semester);
    }
    public Group findByStudent(Student st){
        return groupRepository.findByIdStudent(st);
    }
}
