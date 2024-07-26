package com.envoi.diploma.model;

import com.envoi.diploma.types.enums.AbstractENUM;
import com.envoi.diploma.types.enums.roles;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Map;

@Entity
@Data
@Table(name = "users", schema = "public")
public class User
{
    public User(){}
    public User(Map<String, Object> data)
    {
        this.id = (Integer) data.get("id");
        this.login = (String)  data.get("login");
        this.setPassword((String) data.get("password"));
        this.setRole((String) data.get("role"));
        this.idPerson = ((Integer) data.get("idPerson")).toString();
    }
    @Id
    private int id;
    private String login;
    private String password;
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Basic(optional = false)
    private roles role;
    private String idPerson;

    public void setPassword(String password){
        this.password = new BCryptPasswordEncoder().encode(password);
    }
    public void setRole(String r)
    {
        this.role = AbstractENUM.fromValue(roles.class, r);
    }
}