package service;

import entities.EnvioRutaVehiculo;
import entities.Ruta;
import entities.Vehiculo;
import exception.CreateException;
import exception.DeleteException;
import exception.SelectException;
import exception.UpdateException;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
 * @author Borja
 */
@Stateless
@Path("entities.enviorutavehiculo")
public class EnvioRutaVehiculoFacadeREST extends AbstractFacade<EnvioRutaVehiculo> {

    @PersistenceContext(unitName = "FleetIQ_ServerPU")
    private EntityManager em;

    public EnvioRutaVehiculoFacadeREST() {
        super(EnvioRutaVehiculo.class);
    }

    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(EnvioRutaVehiculo entity) throws CreateException {
        try  {
            // Buscar la Ruta por su localizador
            Ruta ruta = em.createNamedQuery("Ruta.findByLocalizadorInteger", Ruta.class)
                    .setParameter("localizador", entity.getRuta().getLocalizador())
                    .getSingleResult();

            // Set the Ruta in the EnvioRutaVehiculo entity
            entity.setRuta(ruta);

            // Persist the entity
            super.create(entity);

        } catch (Exception e) {
            // Catch any errors and throw a custom exception
            throw new CreateException("Error al crear el EnvioRutaVehiculo: " + e.getMessage());
        }
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, EnvioRutaVehiculo entity) throws UpdateException {
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
    public EnvioRutaVehiculo find(@PathParam("id") Integer id) throws SelectException {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<EnvioRutaVehiculo> findAll() throws SelectException {
        // Usar la consulta "findAll" definida en la entidad
        return em.createNamedQuery("EnvioRutaVehiculo.findAll", EnvioRutaVehiculo.class).getResultList();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<EnvioRutaVehiculo> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) throws SelectException {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    @GET
    @Path("countByRutaId/{rutaId}")
    @Produces(MediaType.TEXT_PLAIN)
    public String countByRutaId(@PathParam("rutaId") Integer rutaId) {
        Long count = em.createNamedQuery("EnvioRutaVehiculo.countByRutaId", Long.class)
                .setParameter("rutaId", rutaId)
                .getSingleResult();
        return String.valueOf(count);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
