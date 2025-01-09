package service;

import entities.EnvioRutaVehiculo;
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
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(EnvioRutaVehiculo entity) throws CreateException {
        super.create(entity);
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

    @POST
    @Path("assignVehicleToRoute")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public EnvioRutaVehiculo assignVehicleToRoute(EnvioRutaVehiculo input) throws CreateException {
        try {
            if (input.getRuta() == null || input.getVehiculo() == null) {
                throw new IllegalArgumentException("La ruta y el vehículo son obligatorios.");
            }

            // Asegurarse de que el envío no sea null
            if (input.getEnvio() == null) {
                throw new IllegalArgumentException("El envío es obligatorio.");
            }

            // Asignar la fecha local del sistema
            input.setFechaAsignacion(new Date());

            // Insertar en la base de datos
            em.persist(input);

            return input; // Devolver el objeto creado con su ID generado
        } catch (Exception e) {
            throw new CreateException("Error al asignar vehículo a la ruta: " + e.getMessage());
        }
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
