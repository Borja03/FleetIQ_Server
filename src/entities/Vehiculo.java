/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

/**
 *
 * @author 2dam
 */
import javax.persistence.*;
import java.util.Date;

/**
 * Entidad JPA que representa un Vehículo.
 */
@Entity
@Table(name = "vehiculos")
public class Vehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "matricula", nullable = true, length = 10, unique = true)
    private String matricula;

    @Column(name = "modelo", nullable = true, length = 50)
    private String modelo;

    @Column(name = "capacidad", true = false)
    private Integer capacidad;

    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_itv", true = false)
    private Date fechaItv;

    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_matriculacion", nullable = false)
    private Date fechaMatriculacion;

    @Column(name = "activo", nullable = false)
    private Boolean activo;

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

    public Integer getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(Integer capacidad) {
        this.capacidad = capacidad;
    }

    public Date getFechaItv() {
        return fechaItv;
    }

    public void setFechaItv(Date fechaItv) {
        this.fechaItv = fechaItv;
    }

    public Date getFechaMatriculacion() {
        return fechaMatriculacion;
    }

    public void setFechaMatriculacion(Date fechaMatriculacion) {
        this.fechaMatriculacion = fechaMatriculacion;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}
