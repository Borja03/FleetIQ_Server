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
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.swing.text.DateFormatter;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author 2dam
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

    @GET
    @Path("dates")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Vehiculo> findByDateRange(@QueryParam("startDate") String startDate,
            @QueryParam("endDate") String endDate) throws SelectException {
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

        // Declaración explícita del tipo de variable
        javax.persistence.TypedQuery<Vehiculo> query = em.createQuery(queryStr, Vehiculo.class);
/*
        DateFormatter formatter = DateFormatter.ofPattern("dd-MM-yyyy");
        if (startDate != null) {
            Date start = Date.parse(startDate, formatter);
            query.setParameter("startDate", java.sql.Date.valueOf(start));
        }
        if (endDate != null) {
            Date end = Date.parse(endDate, formatter);
            query.setParameter("endDate", java.sql.Date.valueOf(end));
        }
*/
        return query.getResultList();
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
