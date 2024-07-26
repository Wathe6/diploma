package com.envoi.diploma.model;

import com.envoi.diploma.types.enums.AbstractENUM;
import com.envoi.diploma.types.enums.worktypes;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Entity
@Data
@Table(name = "works", schema = "public")
public class Work
{
    public Work(){}
    public Work(Map<String, Object> data) throws ParseException
    {
        if (data.containsKey("idWork")) {
            this.idWork = (Integer) data.get("idWork");
        }

        this.idSubject = new Subject();
        this.idSubject.setIdSubject((Integer) data.get("idSubject"));
        setWorktype((String) data.get("worktype"));
        this.number = (Integer) data.get("number");
        this.reference = (String) data.get("reference");

        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        this.dateOpen = formatter.parse((String) data.get("dateOpen"));
        this.dateEnd = formatter.parse((String) data.get("dateEnd"));
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idWork;
    @ManyToOne
    private Subject idSubject;
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Basic(optional = false)
    private worktypes worktype;
    private int number;
    private Date dateOpen;
    private Date dateEnd;
    private String reference;

    public int getIdSubject()
    {
        return idSubject.getIdSubject();
    }
    public String getDateOpen()
    {
        return new SimpleDateFormat("dd.MM.yyyy hh:mm").format(dateOpen);
    }
    @JsonIgnore
    public Date getDateOpenAsDate(){
        return this.dateOpen;
    }
    public String getDateEnd()
    {
        return new SimpleDateFormat("dd.MM.yyyy hh:mm").format(dateEnd);
    }
    public void setWorktype(String type)
    {
        this.worktype = AbstractENUM.fromValue(worktypes.class, type);
    }
    @Override
    public String toString() {
        return "Work{" +
                "idWork=" + idWork +
                ", idSubject=" + (idSubject != null ? idSubject.getIdSubject() : "null") +
                ", worktype=" + worktype +
                ", number=" + number +
                ", dateOpen=" + dateOpen +
                ", dateEnd=" + dateEnd +
                ", reference='" + reference + '\'' +
                '}';
    }
}