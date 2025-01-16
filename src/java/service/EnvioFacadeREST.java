package service;

import entities.Envio;
import exception.CreateException;
import exception.DeleteException;
import exception.SelectException;
import exception.UpdateException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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
 * @author Omar
 */
@Stateless
@Path("envio")
public class EnvioFacadeREST extends AbstractFacade<Envio> {

    @PersistenceContext(unitName = "FleetIQ_ServerPU")
    private EntityManager em;

    public EnvioFacadeREST() {
        super(Envio.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Envio entity) throws CreateException {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Envio entity) throws UpdateException {
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
    public Envio find(@PathParam("id") Integer id) throws SelectException {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Envio> findAll() throws SelectException {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Envio> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) throws SelectException {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    @GET
    @Path("filterByDates")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Envio> filterByDates(@QueryParam("firstDate") String firstDate, @QueryParam("secondDate") String secondDate) throws SelectException {
        // Conversión de String a Date
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            java.util.Date first = dateFormat.parse(firstDate);
            java.util.Date second = dateFormat.parse(secondDate);

            Query query = em.createNamedQuery("Ruta.filterByDates");
            query.setParameter("firstDate", first);
            query.setParameter("secondDate", second);
            return query.getResultList();

        } catch (ParseException e) {
            throw new SelectException("Fecha inválida, por favor use el formato yyyy-MM-dd");
        }
    }

    @GET
    @Path("filterEstado")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Envio> filterEstado(@QueryParam("estado") String estado) throws SelectException {
        Query query = em.createNamedQuery("Ruta.filterEstado");
        query.setParameter("estado", estado);
        return query.getResultList();
    }

    @GET
    @Path("filterNumPaquetes")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Envio> filterNumPaquetes(@QueryParam("numPaquetes") Integer numPaquetes) throws SelectException {
        Query query = em.createNamedQuery("Ruta.filterNumPaquetes");
        query.setParameter("numPaquetes", numPaquetes);
        return query.getResultList();
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

}
