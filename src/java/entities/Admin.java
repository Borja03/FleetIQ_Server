/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Omar
 */



@Entity
@DiscriminatorValue("admin")
@XmlRootElement
public class Admin extends User  implements Serializable {
    private static final long serialVersionUID = 1L;

    private Date ultimoInicioSesion;

    public Admin() {
    }

    public Admin(Date ultimoInicioSesion, String email, String name, String password, String country, String city, String street, Integer zip, String verifcationCode, boolean activo) {
        super(email, name, password, country, city, street, zip, verifcationCode, activo);
        this.ultimoInicioSesion = ultimoInicioSesion;
    }
 
    public Date getUltimoInicioSesion() {
        return ultimoInicioSesion;
    }

    public void setUltimoInicioSesion(Date ultimoInicioSesion) {
        this.ultimoInicioSesion = ultimoInicioSesion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        //  hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Admin)) {
            return false;
        }
        Admin other = (Admin) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Admin{" + "ultimoInicioSesion=" + ultimoInicioSesion + '}';
    }


}
