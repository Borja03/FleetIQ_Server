package entities;

import entities.Estado;
import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;
import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

    @NamedQueries({
        @NamedQuery(
            name = "Envio.findAll",
            query = "SELECT e FROM Envio e"
        ),
        @NamedQuery(
            name = "Ruta.filterByDates",
            query = "SELECT e FROM Envio e WHERE e.fechaEnvio BETWEEN :firstDate AND :secondDate"
        ),
        @NamedQuery(
            name = "Ruta.filterEstado",
            query = "SELECT e FROM Envio e WHERE e.estado = :estado"
        ),
        @NamedQuery(
            name = "Ruta.filterNumPaquetes",
            query = "SELECT e FROM Envio e WHERE e.numPaquetes = :numPaquetes"
        ),
    })

/**
 * Entidad JPA que representa un Envío.
 */
@Entity
@Table(name = "envios", schema = "FleetIQ")
@XmlRootElement
public class Envio implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_envio", nullable = true)
    private Date fechaEnvio;

    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_entrega")
    private Date fechaEntrega;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = true)
    private Estado estado;

    @Column(name = "num_paquetes", nullable = true)
    private Integer numPaquetes;

    @Column(name = "creador_envio", nullable = true, length = 30)
    private String creadorEnvio;

    @Column(name = "ruta", length = 7)
    private String ruta;

    @Column(name = "vehiculo", length = 10)
    private String vehiculo;

    @OneToMany(fetch = EAGER, cascade = ALL, mappedBy = "envio")
    private List<PackageEntity> packageList;

    @ManyToOne(fetch = EAGER, cascade = ALL)
    @JoinColumn(name = "envio_ruta_vehiculo_id")
    private EnvioRutaVehiculo envioRutaVehiculo;

    @ManyToMany(fetch = EAGER, cascade = ALL)
    @JoinTable(
            name = "envios_usuarios", // Nombre de la tabla intermedia
            joinColumns = @JoinColumn(name = "envio_id"), // Columna que referencia la tabla 'envios'
            inverseJoinColumns = @JoinColumn(name = "user_id") // Columna que referencia la tabla 'user_entity'
    )
    private List<UserEntity> userList;

    @XmlTransient
    public List<PackageEntity> getPackageList() {
        return packageList;
    }

    public void setPackageList(List<PackageEntity> packageList) {
        this.packageList = packageList;
    }

    @XmlTransient
    public List<UserEntity> getUserList() {
        return userList;
    }

    public void setUserList(List<UserEntity> userList) {
        this.userList = userList;
    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(Date fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public Date getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(Date fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Integer getNumPaquetes() {
        return numPaquetes;
    }

    public void setNumPaquetes(Integer numPaquetes) {
        this.numPaquetes = numPaquetes;
    }

    public String getCreadorEnvio() {
        return creadorEnvio;
    }

    public void setCreadorEnvio(String creadorEnvio) {
        this.creadorEnvio = creadorEnvio;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public String getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(String vehiculo) {
        this.vehiculo = vehiculo;
    }

    @Override
    public String toString() {
        return "Envio{" + "id=" + id + ", fechaEnvio=" + fechaEnvio + ", fechaEntrega=" + fechaEntrega + ", estado=" + estado + ", numPaquetes=" + numPaquetes + ", creadorEnvio=" + creadorEnvio + ", ruta=" + ruta + ", vehiculo=" + vehiculo + '}';
    }
}
