/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entitie;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import static javax.persistence.CascadeType.ALL;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import static javax.persistence.FetchType.EAGER;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Omar
 */

@NamedQueries({
   @NamedQuery(
           name="findAllPackages",
           query="SELECT * FROM package ORDER BY id"),
   @NamedQuery(
           name="findAllAccounts",
           query="SELECT s FROM Account s ORDER BY s.id"
   )
})

@Entity
@Table(schema="retodb" ,name = "package")
@XmlRootElement
public class PackageEntity implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)// shoud we use here identity or auto 
    private Long id;
    private String sender;
    private String receiver;
    private double weight;
    @Enumerated(EnumType.STRING)
    private PackageSize size;
    @Temporal(TemporalType.DATE)
    private Date creationDate;
    private boolean fragile;

    public PackageEntity() {
    }

    @OneToMany(fetch=EAGER,cascade=ALL,mappedBy="package")
   
      private List<Envio> enviosList;
     public List<Envio> getEnviosList() {
        return enviosList;
    }

    public void setEnviosList(List<Envio> enviosList) {
        this.enviosList = enviosList;
    }
    
    
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

    public PackageSize getSize() {
        return size;
    }

    public void setSize(PackageSize size) {
        this.size = size;
    }

        @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
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