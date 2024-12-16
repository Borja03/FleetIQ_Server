package java.entitie;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Time;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Entidad JPA que representa una ruta.
 */
@Entity
@Table(name = "ruta", schema="FleetIQ")
@XmlRootElement
public class Ruta  implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer localizador;

    @Column(name = "origen", nullable = true)
    private String origen;

    @Column(name = "destino", nullable = true)
    private String destino;

    @Column(name = "distancia", nullable = true)
    private Float distancia;

    @Temporal(TemporalType.DATE)
    @Column(name = "tiempo", nullable = true)
    private Time tiempo;

    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_creacion", nullable = true)
    private Date fechaCreacion;
    
    @Column(name = "vehiculo", length = 50)
    private String vehiculo;
    
    @Column(name = "num_vehiculos", nullable = true)
    private Integer numVehiculos;

  
    // Getters y Setters
    public Integer getLocalizador() {
        return localizador;
    }

    public void setLocalizador(Integer localizador) {
        this.localizador = localizador;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public Float getDistancia() {
        return distancia;
    }

    public void setDistancia(Float distancia) {
        this.distancia = distancia;
    }

    public Time getTiempo() {
        return tiempo;
    }

    public void setTiempo(Time tiempo) {
        this.tiempo = tiempo;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(String vehiculo) {
        this.vehiculo = vehiculo;
    }

    
    public Integer getNumVehiculos() {
        return numVehiculos;
    }

    public void setNumVehiculos(Integer numVehiculos) {
        this.numVehiculos = numVehiculos;
    }

    @Override
    public String toString() {
        return "Ruta{" + "localizador=" + localizador + ", origen=" + origen + ", destino=" + destino + ", distancia=" + distancia + ", tiempo=" + tiempo + ", fechaCreacion=" + fechaCreacion + ", vehiculo=" + vehiculo + ", numVehiculos=" + numVehiculos + '}';
    }
    
}
