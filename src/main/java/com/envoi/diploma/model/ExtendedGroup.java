package com.envoi.diploma.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Map;

@Entity
@Data
@Table(name = "extended_groups")
public class ExtendedGroup
{
    public ExtendedGroup(){}
    public ExtendedGroup(Map<String, Object> data)
    {
        this.idExtendedGroupCode = (String) data.get("idExtendedGroupCode");
        this.name = (String) data.get("name");
    }
    @Id
    private String idExtendedGroupCode;
    private String name;
}
