package entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Entidad JPA que representa un Envío.
 */
@Entity
@Table(name = "envios", schema="FleetIQ")
@XmlRootElement
public class Envio implements Serializable{

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

    @Column(name = "creador_envio", nullable = true, length = 100)
    private String creadorEnvio;

    @Column(name = "ruta", length = 7)
    private String ruta;

    @Column(name = "vehiculo", length = 50)
    private String vehiculo;

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
