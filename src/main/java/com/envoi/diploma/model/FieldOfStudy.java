package com.envoi.diploma.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Map;

@Entity
@Data
@Table(name = "fields_of_study", schema = "public")
public class FieldOfStudy
{
    public FieldOfStudy(){}
    public FieldOfStudy(Map<String, Object> data)
    {
        this.idFieldCode = (String) data.get("idFieldCode");
        this.idExtendedGroupCode = new ExtendedGroup();
        this.idExtendedGroupCode.setIdExtendedGroupCode((String) data.get("idExtendedGroupCode"));
        this.name = (String) data.get("name");
    }
    @Id
    private String idFieldCode;
    @ManyToOne
    private ExtendedGroup idExtendedGroupCode;
    private String name;
    public String getIdExtendedGroupCode()
    {
        return idExtendedGroupCode.getIdExtendedGroupCode();
    }
}
