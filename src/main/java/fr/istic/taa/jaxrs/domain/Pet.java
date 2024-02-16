package fr.istic.taa.jaxrs.domain;

import java.util.ArrayList;
import java.util.List;

import io.swagger.v3.oas.models.tags.Tag;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Pet")
public class Pet {
  private long id;
  private String name;
  private List<Tag> tags = new ArrayList<Tag>();

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

  @XmlElementWrapper(name = "tags")
  @XmlElement(name = "tag")
  public List<Tag> getTags() {
    return tags;
  }

  public void setTags(List<Tag> tags) {
    this.tags = tags;
  }
}