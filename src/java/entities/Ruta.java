package entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Entidad JPA que representa una Ruta.
 */
@Entity
@Table(name = "ruta", schema = "FleetIQ")
@XmlRootElement
public class Ruta implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "descripcion", nullable = true, length = 255)
    private String descripcion;

    @Column(name = "duracion_estimada", nullable = true)
    private Double duracionEstimada;

    @Column(name = "distancia", nullable = true)
    private Double distancia;

    @OneToMany(mappedBy = "ruta", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<EnvioRutaVehiculo> envioRutaVehiculoList;

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getDuracionEstimada() {
        return duracionEstimada;
    }

    public void setDuracionEstimada(Double duracionEstimada) {
        this.duracionEstimada = duracionEstimada;
    }

    public Double getDistancia() {
        return distancia;
    }

    public void setDistancia(Double distancia) {
        this.distancia = distancia;
    }

    @XmlTransient
    public List<EnvioRutaVehiculo> getEnvioRutaVehiculoList() {
        return envioRutaVehiculoList;
    }

    public void setEnvioRutaVehiculoList(List<EnvioRutaVehiculo> envioRutaVehiculoList) {
        this.envioRutaVehiculoList = envioRutaVehiculoList;
    }

    @Override
    public String toString() {
        return "Ruta{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", duracionEstimada=" + duracionEstimada +
                ", distancia=" + distancia +
                '}';
    }
}
