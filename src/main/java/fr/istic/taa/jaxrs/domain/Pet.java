package fr.istic.taa.jaxrs.domain;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.*;

@Entity
@Table(name = "Pet")
@XmlRootElement(name = "Pet")
public class Pet {
  @Id
  @GeneratedValue
  private long id;
  private String name;

  @XmlElement(name = "id")
  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  @XmlElement(name = "name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setId(Long id) {
    this.id = id;
  }

}