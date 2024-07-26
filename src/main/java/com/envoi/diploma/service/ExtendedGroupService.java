package com.envoi.diploma.service;

import com.envoi.diploma.model.ExtendedGroup;
import com.envoi.diploma.repository.ExtendedGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class ExtendedGroupService extends BaseCRUDService<ExtendedGroup, String> {
    @Autowired
    public ExtendedGroupService(ExtendedGroupRepository repository) {
        super(repository);
    }
}