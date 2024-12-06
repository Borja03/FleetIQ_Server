/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entitie;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Omar
 */
@Entity
@Table(schema="retodb" ,name = "paquete")
public class PaqueteEntity implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)// shoud we use here identity or auto 
    private Long id;
    private String sender;
    private String receiver;
    private double weight;
    @Enumerated(EnumType.STRING)
    private PaqueteSize size;
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    private boolean fragile;

    public PaqueteEntity() {
    }

    
    
    
    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }


    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public boolean isFragile() {
        return fragile;
    }

    public void setFragile(boolean fragile) {
        this.fragile = fragile;
    }

    public PaqueteSize getSize() {
        return size;
    }

    public void setSize(PaqueteSize size) {
        this.size = size;
    }

    // Optional: toString() method for debugging
    @Override
    public String toString() {
        return "PaqueteEntity{" +
                "id=" + id +
                ", sender='" + sender + '\'' +
                ", receiver='" + receiver + '\'' +
                ", weight=" + weight +
                ", size=" + size +
                ", creationDate=" + creationDate +
                ", fragile=" + fragile +
                '}';
    }
}