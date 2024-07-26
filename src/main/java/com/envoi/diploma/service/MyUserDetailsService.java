package com.envoi.diploma.service;

import com.envoi.diploma.model.MyUserDetails;
import com.envoi.diploma.model.User;
import com.envoi.diploma.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService
{
    @Autowired
    private UserRepository myUserDetailsService;
    public UserDetails loadUserById(int id) throws UsernameNotFoundException
    {
        User user = myUserDetailsService.findById(id);
        return new MyUserDetails(user);
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException
    {
        User user = myUserDetailsService.findByLogin(login);
        return new MyUserDetails(user);
    }
}
