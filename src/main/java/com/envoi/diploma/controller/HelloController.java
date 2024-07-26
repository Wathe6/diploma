package com.envoi.diploma.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HelloController
{
    @RequestMapping("/")
    public String greet(){
        return "forward:/index.html";
    }
    @RequestMapping("/admin" )
    @PreAuthorize("hasAuthority('ADMIN')")
    public String greeting(){
        return "forward:/index.html";
    }
}
