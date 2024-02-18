package fr.istic.taa.jaxrs.domain;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.*;

import java.util.Date;
@Entity
@XmlRootElement(name = "Comment")
public class Comment {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private User createdBy;

    @ManyToOne
    private Ticket ticket;

    private String content;
    private Date createdDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @XmlElement(name = "createdBy")
    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    @XmlElement(name = "content")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}