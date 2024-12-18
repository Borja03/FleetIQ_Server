package entities;

import java.io.Serializable;

import java.util.Date;
import java.util.List;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Entidad JPA que representa una ruta.
 */
@Entity
@Table(name = "ruta", schema = "FleetIQ2")
@XmlRootElement
public class Ruta implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer localizador;

    @Column(name = "origen")
    private String origen;

    @Column(name = "destino")
    private String destino;

    @Column(name = "distancia")
    private Float distancia;

    @Column(name = "tiempo", nullable = false)
    private Integer tiempo;

    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_creacion", nullable = false)
    private Date fechaCreacion;

    public Integer getNumVehiculos() {
        return numVehiculos;
    }

    public void setNumVehiculos(Integer numVehiculos) {
        this.numVehiculos = numVehiculos;
    }

    @Column(name = "num_vehiculos", nullable = true)
    private Integer numVehiculos;

    // OneToMany relationship with RutaVehiculo
    @OneToMany(mappedBy = "ruta", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<EnvioRutaVehiculo> envioRutaVehiculos;

    // Getters and Setters
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

    public Integer getTiempo() {
        return tiempo;
    }

    public void setTiempo(Integer tiempo) {
        this.tiempo = tiempo;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public List<EnvioRutaVehiculo> getRutaVehiculos() {
        return envioRutaVehiculos;
    }

    public void setRutaVehiculos(List<EnvioRutaVehiculo> rutaVehiculos) {
        this.envioRutaVehiculos = rutaVehiculos;
    }

    @Override
    public String toString() {
        return "Ruta{"
                + "localizador=" + localizador
                + ", origen='" + origen + '\''
                + ", destino='" + destino + '\''
                + ", distancia=" + distancia
                + ", tiempo=" + tiempo
                + ", fechaCreacion=" + fechaCreacion
                +", numPaquetes=" + numVehiculos
                + '}';
    }
}
