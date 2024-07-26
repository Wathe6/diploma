package com.envoi.diploma.service;

import com.envoi.diploma.model.User;
import com.envoi.diploma.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class UserService extends BaseCRUDService<User, Integer>
{
    @Autowired
    public UserService(UserRepository repository){
        super(repository);
    }
}
