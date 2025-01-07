package entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
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
@Table(name = "envio", schema = "FleetIQ")
@XmlRootElement
public class Envio implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "descripcion", nullable = false, length = 255)
    private String descripcion;

    @Column(name = "destinatario", nullable = false, length = 100)
    private String destinatario;

    @Column(name = "direccion_entrega", nullable = false, length = 255)
    private String direccionEntrega;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_envio", nullable = false)
    private Date fechaEnvio;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_entrega", nullable = true)
    private Date fechaEntrega;

    @Column(name = "estado", nullable = false, length = 50)
    private String estado; // Ejemplo: "Pendiente", "En tránsito", "Entregado"

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "envio_ruta_vehiculo_id", nullable = false)
    private EnvioRutaVehiculo envioRutaVehiculo;

    @ManyToMany(mappedBy = "enviosList")
    private List<User> usersList;

    @OneToMany(cascade=ALL,mappedBy="envio",fetch=EAGER)
    private List<Paquete> packageList;
    
 
    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public String getDireccionEntrega() {
        return direccionEntrega;
    }

    public void setDireccionEntrega(String direccionEntrega) {
        this.direccionEntrega = direccionEntrega;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public EnvioRutaVehiculo getEnvioRutaVehiculo() {
        return envioRutaVehiculo;
    }

    public void setEnvioRutaVehiculo(EnvioRutaVehiculo envioRutaVehiculo) {
        this.envioRutaVehiculo = envioRutaVehiculo;
    }

    @XmlTransient
    public List<User> getUsersList() {
        return usersList;
    }

    public void setUsersList(List<User> usersList) {
        this.usersList = usersList;
    }

    @Override
    public String toString() {
        return "Envio{"
                + "id=" + id
                + ", descripcion='" + descripcion + '\''
                + ", destinatario='" + destinatario + '\''
                + ", direccionEntrega='" + direccionEntrega + '\''
                + ", fechaEnvio=" + fechaEnvio
                + ", fechaEntrega=" + fechaEntrega
                + ", estado='" + estado + '\''
                + ", envioRutaVehiculo=" + (envioRutaVehiculo != null ? envioRutaVehiculo.getId() : "null")
                + '}';
    }
}
