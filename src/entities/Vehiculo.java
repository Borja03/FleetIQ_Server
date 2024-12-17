package entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;
import static javax.persistence.FetchType.EAGER;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Entidad JPA que representa un Vehículo.
 */
@Entity
@Table(name = "vehiculo", schema = "FleetIQ")
@XmlRootElement
public class Vehiculo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "matricula", nullable = false, unique = true, length = 15)
    private String matricula;

    @Column(name = "marca", nullable = true, length = 50)
    private String marca;

    @Column(name = "modelo", nullable = true, length = 50)
    private String modelo;

    @Column(name = "capacidad_carga", nullable = true)
    private Float capacidadCarga;

    @Column(name = "estado", nullable = false, length = 20)
    private String estado; // Ejemplo: "Disponible", "En uso", "Mantenimiento"

    @OneToMany(fetch = EAGER, cascade = CascadeType.ALL, mappedBy = "vehiculo")
    private List<EnvioRutaVehiculo> envioRutaVehiculoList;

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Float getCapacidadCarga() {
        return capacidadCarga;
    }

    public void setCapacidadCarga(Float capacidadCarga) {
        this.capacidadCarga = capacidadCarga;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<EnvioRutaVehiculo> getEnvioRutaVehiculoList() {
        return envioRutaVehiculoList;
    }

    public void setEnvioRutaVehiculoList(List<EnvioRutaVehiculo> envioRutaVehiculoList) {
        this.envioRutaVehiculoList = envioRutaVehiculoList;
    }

    @Override
    public String toString() {
        return "Vehiculo{" +
                "id=" + id +
                ", matricula='" + matricula + '\'' +
                ", marca='" + marca + '\'' +
                ", modelo='" + modelo + '\'' +
                ", capacidadCarga=" + capacidadCarga +
                ", estado='" + estado + '\'' +
                '}';
    }
}
