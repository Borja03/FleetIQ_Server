package service;

import entities.EnvioRutaVehiculo;
import entities.Ruta;
import entities.Vehiculo;
import exception.CreateException;
import exception.DeleteException;
import exception.SelectException;
import exception.UpdateException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        try {
            // Buscar la Ruta usando el localizador
            Ruta ruta = em.createNamedQuery("Ruta.findByLocalizadorInteger", Ruta.class)
                    .setParameter("localizador", entity.getRutaLocalizador())
                    .getSingleResult();

            // Buscar el Vehículo usando su ID mediante una query
            Vehiculo vehiculo = em.createQuery("SELECT v FROM Vehiculo v WHERE v.id = :id", Vehiculo.class)
                    .setParameter("id", entity.getVehiculoID())
                    .getSingleResult();

            // Crear una nueva instancia manejada por JPA
            EnvioRutaVehiculo newEntity = new EnvioRutaVehiculo();
            newEntity.setRuta(ruta);
            newEntity.setVehiculo(vehiculo);
            newEntity.setFechaAsignacion(entity.getFechaAsignacion());
            
            // Persistir la nueva entidad
            em.persist(newEntity);
            em.flush();
        } catch (Exception e) {
            e.printStackTrace();
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

    @GET
    @Path("/getId/{vehiculoId}")
    @Produces(MediaType.APPLICATION_XML)
    public List<EnvioRutaVehiculo> getId(@PathParam("vehiculoId") Integer vehiculoId) {
        return em.createNamedQuery("EnvioRutaVehiculo.getId", EnvioRutaVehiculo.class)
                .setParameter("vehiculoId", vehiculoId)
                .getResultList();
    }

    @GET
    @Path("/getRutaId/{vehiculoId}")
    @Produces(MediaType.APPLICATION_XML)
    public List<Ruta> getRutaId(@PathParam("vehiculoId") Integer vehiculoId) {
        return em.createNamedQuery("EnvioRutaVehiculo.getRutaId", Ruta.class)
                .setParameter("vehiculoId", vehiculoId)
                .getResultList();
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
