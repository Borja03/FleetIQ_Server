/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Omar
 */
@NamedQueries({
    @NamedQuery(
                    name = "findAllUsers",
                    query = "Select u From User u")
    ,
   @NamedQuery(
                    name = "signin",
                    query = "SELECT u FROM User u WHERE u.email = :userEmail AND u.password = :userPassword"
    )
    ,
   @NamedQuery(
                    name = "findUserByEmail",
                    query = "SELECT u FROM User u WHERE u.email=:userEmail")

})

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "user_type", discriminatorType = DiscriminatorType.STRING)
@Table(schema = "FleetIQ", name = "user")
//@XmlSeeAlso({Admin.class, Trabajador.class}) 
@XmlRootElement
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;
    @Column(unique = true)
    private String email;
    private String name;
    private String password;
    private String city;
    private String street;
    private Integer zip;
    private String verifcationCode;
    private boolean activo;

    @Column(insertable = false, updatable = false)
    //@Enumerated(EnumType.STRING)
    private String user_type;

    public User() {
    }

    public User(String email, String name, String password, String city, String street, Integer zip, String verifcationCode, boolean activo) {
        // anotation to be unnique
        this.email = email;
        this.name = name;
        this.password = password;
        this.city = city;
        this.street = street;
        this.zip = zip;
        this.verifcationCode = verifcationCode;
        this.activo = activo;
    }

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
                    schema = "FleetIQ", name = "user_envio",
                    joinColumns = @JoinColumn(name = "user_id"),
                    inverseJoinColumns = @JoinColumn(name = "envio_id")
    )

    private List<Envio> enviosList;

    @XmlTransient
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Integer getZip() {
        return zip;
    }

    public void setZip(Integer zip) {
        this.zip = zip;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public String getVerifcationCode() {
        return verifcationCode;
    }

    public void setVerifcationCode(String verifcationCode) {
        this.verifcationCode = verifcationCode;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    //
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", email=" + email + ", name=" + name + ", password=" + password + ", city=" + city + ", street=" + street + ", zip=" + zip + ", verifcationCode=" + verifcationCode + ", activo=" + activo + ", enviosList=" + enviosList + '}';
    }

}
