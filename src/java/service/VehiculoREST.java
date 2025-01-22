/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import entities.Vehiculo;
import exception.CreateException;
import exception.DeleteException;
import exception.SelectException;
import exception.UpdateException;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.swing.text.DateFormatter;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Adrian
 */
@Stateless
@Path("vehiculo")
public class VehiculoREST extends AbstractFacade<Vehiculo> {

    @PersistenceContext(unitName = "FleetIQ_ServerPU")
    private EntityManager em;

    public VehiculoREST() {
        super(Vehiculo.class);
    }

    @POST
    @Consumes({MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_XML})
    public Vehiculo createVehicle(Vehiculo vehicle) {
        try {
            //LOGGER.log(Level.INFO, "Attempting to create vehicle with name: {0}", vehicle.getId());
            vehicle.setId(null);
            super.create(vehicle);
            //LOGGER.log(Level.INFO, "Successfully created vehicle with ID: {0}", vehicle.getId());
            return vehicle;
        } catch (CreateException e) {
            //LOGGER.log(Level.SEVERE, "Failed to create package", e);
            throw new InternalServerErrorException("Failed to create package");
        }
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Vehiculo entity) throws UpdateException {
        try {
            super.edit(entity);
        } catch (Exception e) {
            throw new InternalServerErrorException("Error updating vehicle: " + e.getMessage());
        }
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) throws SelectException, DeleteException {
        try {
            super.remove(super.find(id));
        } catch (Exception e) {
            throw new InternalServerErrorException("Error deleting vehicle: " + e.getMessage());
        }
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Vehiculo find(@PathParam("id") Integer id) throws SelectException {
        try {
            return super.find(id);
        } catch (Exception e) {
            throw new InternalServerErrorException("Error finding vehicle: " + e.getMessage());
        }
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Vehiculo> findAll() throws SelectException {
        try {
            return super.findAll();
        } catch (Exception e) {
            throw new InternalServerErrorException("Error retrieving vehicles: " + e.getMessage());
        }
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Vehiculo> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) throws SelectException {
        try {
            return super.findRange(new int[]{from, to});
        } catch (Exception e) {
            throw new InternalServerErrorException("Error finding vehicle range: " + e.getMessage());
        }
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        try {
            return String.valueOf(super.count());
        } catch (Exception e) {
            throw new InternalServerErrorException("Error counting vehicles: " + e.getMessage());
        }
    }

    @GET
    @Path("capacity/{capacity}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Vehiculo> findByCapacity(@PathParam("capacity") Integer capacity) {
        try {
            return em.createNamedQuery("findByCapacity", Vehiculo.class)
                    .setParameter("capacidadCarga", capacity)
                    .getResultList();
        } catch (Exception e) {
            throw new InternalServerErrorException("Error finding vehicle by capacity: " + e.getMessage());
        }
    }

    @GET
    @Path("plate/{matricula}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Vehiculo> findByPlate(@PathParam("matricula") String matricula) {
        try {
            return em.createNamedQuery("findByPlate", Vehiculo.class)
                    .setParameter("matricula", "%" + matricula + "%")
                    .getResultList();
        } catch (Exception e) {
            throw new InternalServerErrorException("Error finding vehicle by plate: " + e.getMessage());
        }
    }

    @GET
    @Path("dates")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Vehiculo> findByDateRange(@QueryParam("startDate") String startDate,
            @QueryParam("endDate") String endDate) throws SelectException {
        try {
            if (startDate == null && endDate == null) {
                return findAll();
            }

            String queryStr = "SELECT v FROM Vehiculo v WHERE 1=1";
            if (startDate != null) {
                queryStr += " AND v.registrationDate >= :startDate";
            }
            if (endDate != null) {
                queryStr += " AND v.registrationDate <= :endDate";
            }

            javax.persistence.TypedQuery<Vehiculo> query = em.createQuery(queryStr, Vehiculo.class);
            return query.getResultList();
        } catch (Exception e) {
            throw new InternalServerErrorException("Error finding vehicles by date range: " + e.getMessage());
        }
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
