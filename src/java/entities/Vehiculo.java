package entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@NamedQueries({
    
    @NamedQuery(
            name = "vfindAll",
            query = "SELECT v FROM Vehiculo v"
    ),
    @NamedQuery(
            name = "findByCapacity",
            query = "SELECT v FROM Vehiculo v WHERE v.capacidadCarga = :capacidadCarga"
    ),
    @NamedQuery(
            name = "findByDateRangeITV",
            query = "SELECT v FROM Vehiculo v WHERE v.itvDate BETWEEN :startDate AND :endDate"
    ),
    @NamedQuery(
            name = "findByDateRangeRegistration",
            query = "SELECT v FROM Vehiculo v WHERE v.registrationDate BETWEEN :startDate AND :endDate"
    ),
    @NamedQuery(
            name = "findByPlate",
            query = "SELECT v FROM Vehiculo v WHERE LOWER(v.matricula) LIKE LOWER(:matricula)"
    ),
    @NamedQuery(
            name = "findAfterDateITV",
            query = "SELECT v FROM Vehiculo v WHERE v.itvDate >= :startDate"
    ),
    @NamedQuery(
            name = "findAfterDateRegistration",
            query = "SELECT v FROM Vehiculo v WHERE v.registrationDate >= :startDate"
    ),
    @NamedQuery(
            name = "findBeforeDateITV",
            query = "SELECT v FROM Vehiculo v WHERE v.itvDate <= :endDate"
    ),
    @NamedQuery(
            name = "findBeforeDateRegistration",
            query = "SELECT v FROM Vehiculo v WHERE v.registrationDate <= :endDate"
    )
})


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

    @Column(name = "modelo", nullable = true, length = 50)
    private String modelo;

    @Column(name = "capacidad_carga", nullable = true)
    private Integer capacidadCarga;
    
    @Column(name = "registrationDate", nullable = true)
    private Date registrationDate;
    
    @Column(name = "itvDate", nullable = true)
    private Date itvDate;

    @Column(name = "activo", nullable = false)
    private boolean activo; // Ejemplo: "Disponible", "En uso", "Mantenimiento"

    @OneToMany(mappedBy = "vehiculo", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
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

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Integer getCapacidadCarga() {
        return capacidadCarga;
    }

    public void setCapacidadCarga(Integer capacidadCarga) {
        this.capacidadCarga = capacidadCarga;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Date getItvDate() {
        return itvDate;
    }

    public void setItvDate(Date itvDate) {
        this.itvDate = itvDate;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
   

    @XmlTransient
    public List<EnvioRutaVehiculo> getEnvioRutaVehiculoList() {
        return envioRutaVehiculoList;
    }

    public void setEnvioRutaVehiculoList(List<EnvioRutaVehiculo> envioRutaVehiculoList) {
        this.envioRutaVehiculoList = envioRutaVehiculoList;
    }

}
