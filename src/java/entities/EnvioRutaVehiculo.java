package entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
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
    ,
     @NamedQuery(
            name = "EnvioRutaVehiculo.getId",
            query = "SELECT e FROM EnvioRutaVehiculo e WHERE e.vehiculo.id = :vehiculoId"
    )
    ,

    @NamedQuery(
            name = "EnvioRutaVehiculo.getRutaId",
            query = "SELECT r FROM EnvioRutaVehiculo e, Ruta r WHERE e.vehiculo.id = :vehiculoId AND e.ruta.id = r.id"
    ),
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

    @OneToMany(mappedBy = "envioRutaVehiculo", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Envio> envios;

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
    public List<Envio> getEnvios() {
        return envios;
    }

    public void setEnvios(List<Envio> envios) {
        this.envios = envios;
    }
    
      @Transient  
    private Integer rutaLocalizador;  
      
        @Transient  
    private Integer vehiculoID;  

   

    public Integer getVehiculoID() {
        return vehiculoID;
    }

    public void setVehiculoID(Integer vehiculoID) {
        this.vehiculoID = vehiculoID;
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
    
     public Integer getRutaLocalizador() {
        return rutaLocalizador;
    }

    public void setRutaLocalizador(Integer rutaLocalizador) {
        this.rutaLocalizador = rutaLocalizador;
    }
    
     @Override
    public String toString() {
        return "EnvioRutaVehiculo{" + "id=" + id + ", envios=" + envios + ", ruta=" + ruta + ", vehiculo=" + vehiculo + ", fechaAsignacion=" + fechaAsignacion + ", rutaLocalizador=" + rutaLocalizador + ", vehiculoID=" + vehiculoID + '}';
    }

   
}
