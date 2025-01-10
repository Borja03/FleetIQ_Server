package entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@NamedQueries({
    @NamedQuery(
            name = "EnvioRutaVehiculo.countByRutaId",
            query = "SELECT COUNT(e) FROM EnvioRutaVehiculo e WHERE e.ruta.localizador = :rutaId"
    )
    ,
    @NamedQuery(
            name = "EnvioRutaVehiculo.findAll",
            query = "SELECT e FROM EnvioRutaVehiculo e"
    )
})

/**
 * Entidad JPA que representa la relación entre Envío, Ruta y Vehículo.
 */
@Entity
@Table(name = "envio_ruta_vehiculo", schema = "FleetIQ")
@XmlRootElement
public class EnvioRutaVehiculo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "envio_id", nullable = false)
    private Envio envio;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "ruta_id", nullable = false)
    private Ruta ruta;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "vehiculo_id", nullable = false)
    private Vehiculo vehiculo;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_asignacion", nullable = false)
    private Date fechaAsignacion;

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @XmlTransient
    public Envio getEnvio() {
        return envio;
    }

    public void setEnvio(Envio envio) {
        this.envio = envio;
    }

    @XmlTransient
    public Ruta getRuta() {
        return ruta;
    }

    public void setRuta(Ruta ruta) {
        this.ruta = ruta;
    }

    @XmlTransient
    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    public Date getFechaAsignacion() {
        return fechaAsignacion;
    }

    public void setFechaAsignacion(Date fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }

    @Override
    public String toString() {
        return "EnvioRutaVehiculo{"
                + "id=" + id
                + ", envio=" + (envio != null ? envio.toString() : "null")
                + ", ruta=" + (ruta != null ? ruta.toString() : "null")
                + ", vehiculo=" + (vehiculo != null ? vehiculo.toString() : "null")
                + ", fechaAsignacion=" + fechaAsignacion
                + '}';
    }

}
