/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import entities.Paquete;
import entities.Vehiculo;
import exception.CreateException;
import exception.DeleteException;
import exception.SelectException;
import exception.UpdateException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.swing.text.DateFormatter;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
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

    private static final Logger LOGGER = Logger.getLogger(VehiculoREST.class.getName());

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
            LOGGER.log(Level.INFO, "Attempting to create vehicle with name: {0}", vehicle.getId());
            vehicle.setId(null);
            super.create(vehicle);
            LOGGER.log(Level.INFO, "Successfully created vehicle with ID: {0}", vehicle.getId());
            return vehicle;
        } catch (CreateException e) {
            LOGGER.log(Level.SEVERE, "Failed to create package", e);
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
/*
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
*/
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
    @Path("date/betweenRegistration")
    @Produces({MediaType.APPLICATION_XML})
    public List<Vehiculo> findVehiclesBetweenDatesRegistration(
            @QueryParam("startDate") @DefaultValue("") String startDate,
            @QueryParam("endDate") @DefaultValue("") String endDate) {
        try {
            LOGGER.log(Level.INFO, "Fetching vehicles between {0} and {1}", new Object[]{startDate, endDate});

            if (startDate.isEmpty() || endDate.isEmpty()) {
                throw new InternalServerErrorException("Both startDate and endDate must be provided");
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date start = dateFormat.parse(startDate);
            Date end = dateFormat.parse(endDate);

            if (start.after(end)) {
                throw new BadRequestException("Start date must be before end date");
            }

            return em.createNamedQuery("findByDateRangeRegistration", Vehiculo.class)
                    .setParameter("startDate", start)
                    .setParameter("endDate", end)
                    .getResultList();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to parse date parameters", e);
            throw new InternalServerErrorException("Invalid date format. Expected dd-MM-yyyy.");
        }
    }

    @GET
    @Path("date/afterRegistration")
    @Produces({MediaType.APPLICATION_XML})
    public List<Vehiculo> findVehiclessAfterDateRegistration(
            @QueryParam("startDate") @DefaultValue("") String startDate) {
        try {
            LOGGER.log(Level.INFO, "Fetching vehicles after {0}", startDate);

            if (startDate.isEmpty()) {
                throw new InternalServerErrorException("startDate must be provided");
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date start = dateFormat.parse(startDate);

            return em.createNamedQuery("findAfterDateRegistration", Vehiculo.class)
                    .setParameter("startDate", start)
                    .getResultList();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to parse startDate parameter", e);
            throw new InternalServerErrorException("Invalid date format. Expected dd-MM-yyyy.");
        }
    }

    @GET
    @Path("date/beforerRegistration")
    @Produces({MediaType.APPLICATION_XML})
    public List<Vehiculo> findVehiclesBeforeDateRegistration(
            @QueryParam("endDate") @DefaultValue("") String endDate) {
        try {
            LOGGER.log(Level.INFO, "Fetching vehicles before {0}", endDate);

            if (endDate.isEmpty()) {
                throw new InternalServerErrorException("endDate must be provided");
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date end = dateFormat.parse(endDate);

            return em.createNamedQuery("findBeforeDateRegistration", Vehiculo.class)
                    .setParameter("endDate", end)
                    .getResultList();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to parse endDate parameter", e);
            throw new InternalServerErrorException("Invalid date format. Expected dd-MM-yyyy.");
        }
    }

    @GET
    @Path("date/betweenITV")
    @Produces({MediaType.APPLICATION_XML})
    public List<Vehiculo> findByDateRangeITV(
            @QueryParam("startDate") @DefaultValue("") String startDate,
            @QueryParam("endDate") @DefaultValue("") String endDate) {
        try {
            LOGGER.log(Level.INFO, "Fetching vehicles between {0} and {1}", new Object[]{startDate, endDate});

            if (startDate.isEmpty() || endDate.isEmpty()) {
                throw new InternalServerErrorException("Both startDate and endDate must be provided");
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date start = dateFormat.parse(startDate);
            Date end = dateFormat.parse(endDate);

            if (start.after(end)) {
                throw new BadRequestException("Start date must be before end date");
            }

            return em.createNamedQuery("findByDateRangeITV", Vehiculo.class)
                    .setParameter("startDate", start)
                    .setParameter("endDate", end)
                    .getResultList();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to parse date parameters", e);
            throw new InternalServerErrorException("Invalid date format. Expected dd-MM-yyyy.");
        }
    }

    @GET
    @Path("date/afterITV")
    @Produces({MediaType.APPLICATION_XML})
    public List<Vehiculo> findVehiclessAfterDateITV(
            @QueryParam("startDate") @DefaultValue("") String startDate) {
        try {
            LOGGER.log(Level.INFO, "Fetching vehicles after {0}", startDate);

            if (startDate.isEmpty()) {
                throw new InternalServerErrorException("startDate must be provided");
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date start = dateFormat.parse(startDate);

            return em.createNamedQuery("findAfterDateITV", Vehiculo.class)
                    .setParameter("startDate", start)
                    .getResultList();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to parse startDate parameter", e);
            throw new InternalServerErrorException("Invalid date format. Expected dd-MM-yyyy.");
        }
    }

    @GET
    @Path("date/beforeITV")
    @Produces({MediaType.APPLICATION_XML})
    public List<Vehiculo> findVehiclesBeforeDateITV(
            @QueryParam("endDate") @DefaultValue("") String endDate) {
        try {
            LOGGER.log(Level.INFO, "Fetching vehicles before {0}", endDate);

            if (endDate.isEmpty()) {
                throw new InternalServerErrorException("endDate must be provided");
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date end = dateFormat.parse(endDate);

            return em.createNamedQuery("findBeforeDateITV", Vehiculo.class)
                    .setParameter("endDate", end)
                    .getResultList();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to parse endDate parameter", e);
            throw new InternalServerErrorException("Invalid date format. Expected dd-MM-yyyy.");
        }
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}