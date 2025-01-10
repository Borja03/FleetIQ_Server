package service;

import entities.Vehiculo;
import exception.CreateException;
import exception.DeleteException;
import exception.SelectException;
import exception.UpdateException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Stateless
@Path("vehiculo")
public class VehiculoREST extends AbstractFacade<Vehiculo> {

    @PersistenceContext(unitName = "FleetIQ_ServerPU")
    private EntityManager em;

    public VehiculoREST() {
        super(Vehiculo.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Vehiculo entity) throws CreateException {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Vehiculo entity) throws UpdateException {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) throws SelectException, DeleteException {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Vehiculo find(@PathParam("id") Integer id) throws SelectException {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Vehiculo> findAll() throws SelectException {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Vehiculo> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) throws SelectException {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    @GET
    @Path("capacity/{capacity}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Vehiculo> findByCapacity(@PathParam("capacity") Integer capacity) {
        return em.createNamedQuery("findByCapacity", Vehiculo.class)
                .setParameter("capacidadCarga", capacity)
                .getResultList();
    }

    @GET
    @Path("plate/{matricula}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Vehiculo> findByPlate(@PathParam("matricula") String matricula) {
        return em.createNamedQuery("findByPlate", Vehiculo.class)
                .setParameter("matricula", "%" + matricula + "%")
                .getResultList();
    }

    // Method to filter vehicles by ITVDATE
    @GET
    @Path("filterByITVDate")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Vehiculo> findVehiculosByItvDateRange(
        @QueryParam("startDate") @DefaultValue("") String startDate,
        @QueryParam("endDate") @DefaultValue("") String endDate) throws SelectException {
        
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            
            Date start = null;
            Date end = null;

            // Parse the start date if provided
            if (!startDate.isEmpty()) {
                start = dateFormat.parse(startDate);
            }
            
            // Parse the end date if provided
            if (!endDate.isEmpty()) {
                end = dateFormat.parse(endDate);
            }

            // Check if both dates are provided
            if (start != null && end != null) {
                // Ensure the start date is before the end date
                if (start.after(end)) {
                    throw new SelectException("Start date must be before end date");
                }
                return em.createNamedQuery("findByDateRangeITV", Vehiculo.class)
                        .setParameter("startDate", start)
                        .setParameter("endDate", end)
                        .getResultList();
            } 
            // If only the start date is provided
            else if (start != null) {
                return em.createNamedQuery("findAfterDateITV", Vehiculo.class)
                        .setParameter("startDate", start)
                        .getResultList();
            } 
            // If only the end date is provided
            else if (end != null) {
                return em.createNamedQuery("findBeforeDateITV", Vehiculo.class)
                        .setParameter("endDate", end)
                        .getResultList();
            } 
            // Throw an exception if no dates are provided
            else {
                throw new SelectException("At least one date parameter must be provided");
            }
        } catch (Exception e) {
            throw new SelectException("Error retrieving vehicles by ITV date range: " + e.getMessage());
        }
    }

    // Method to filter vehicles by registration date range
    @GET
    @Path("filterByITVRegistration")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Vehiculo> findVehiculosByRegistrationDateRange(
        @QueryParam("startDate") @DefaultValue("") String startDate,
        @QueryParam("endDate") @DefaultValue("") String endDate) throws SelectException {
        
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            
            Date start = null;
            Date end = null;

            // Parse the start date if provided
            if (!startDate.isEmpty()) {
                start = dateFormat.parse(startDate);
            }
            
            // Parse the end date if provided
            if (!endDate.isEmpty()) {
                end = dateFormat.parse(endDate);
            }

            // Check if both dates are provided
            if (start != null && end != null) {
                // Ensure the start date is before the end date
                if (start.after(end)) {
                    throw new SelectException("Start date must be before end date");
                }
                return em.createNamedQuery("findByDateRangeRegistration", Vehiculo.class)
                        .setParameter("startDate", start)
                        .setParameter("endDate", end)
                        .getResultList();
            } 
            // If only the start date is provided
            else if (start != null) {
                return em.createNamedQuery("findAfterDateRegistration", Vehiculo.class)
                        .setParameter("startDate", start)
                        .getResultList();
            } 
            // If only the end date is provided
            else if (end != null) {
                return em.createNamedQuery("findBeforeDateRegistration", Vehiculo.class)
                        .setParameter("endDate", end)
                        .getResultList();
            } 
            // Throw an exception if no dates are provided
            else {
                throw new SelectException("At least one date parameter must be provided");
            }
        } catch (Exception e) {
            throw new SelectException("Error retrieving vehicles by registration date range: " + e.getMessage());
        }
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
