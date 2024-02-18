package fr.istic.taa.jaxrs.domain;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.*;

@Entity
@XmlRootElement(name = "Tag")
public class Tag {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @XmlElement(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
