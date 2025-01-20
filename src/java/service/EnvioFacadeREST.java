package service;

import entities.Envio;
import entities.EnvioRutaVehiculo;
import entities.Estado;
import entities.Ruta;
import exception.CreateException;
import exception.DeleteException;
import exception.SelectException;
import exception.UpdateException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
        try {
            super.create(entity);
        } catch (Exception e) {
            throw new CreateException("Error al crear el envío: " + e.getMessage());
        }
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Envio entity) throws UpdateException {
        try {
            super.edit(entity);
        } catch (Exception e) {
            throw new UpdateException("Error al actualizar el envío con id " + id + ": " + e.getMessage());
        }
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Envio find(@PathParam("id") Integer id) throws SelectException {
        try {
            return super.find(id);
        } catch (Exception e) {
            throw new SelectException("Error al obtener el envío con id " + id + ": " + e.getMessage(), e);
        }
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Envio> findAll() throws SelectException {
        try {
            return super.findAll();
        } catch (Exception e) {
            throw new SelectException("Error al obtener todos los envíos: " + e.getMessage(), e);
        }
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Envio> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) throws SelectException {
        try {
            return super.findRange(new int[]{from, to});
        } catch (Exception e) {
            throw new SelectException("Error al obtener el rango de envíos: " + e.getMessage(), e);
        }
    }

    @GET
    @Path("filterByDates")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Envio> filterByDates(@QueryParam("firstDate") String firstDate, @QueryParam("secondDate") String secondDate) throws SelectException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            java.util.Date first = dateFormat.parse(firstDate);
            java.util.Date second = dateFormat.parse(secondDate);

            Query query = em.createNamedQuery("Envio.filterByDates");
            query.setParameter("firstDate", first);
            query.setParameter("secondDate", second);
            return query.getResultList();
        } catch (ParseException e) {
            throw new SelectException("Error al parsear las fechas. Formato esperado: yyyy-MM-dd", e);
        } catch (Exception e) {
            throw new SelectException("Error al filtrar envíos por fechas: " + e.getMessage(), e);
        }
    }

    @GET
    @Path("filterEstado")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Envio> filterEstado(@QueryParam("estado") String estadoS) throws SelectException {
        try {
            Query query = em.createNamedQuery("Envio.filterEstado");
            Estado estado = Estado.valueOf(estadoS.toUpperCase());
            query.setParameter("estado", estado);
            return query.getResultList();
        } catch (IllegalArgumentException e) {
            throw new SelectException("Estado inválido: " + estadoS, e);
        } catch (Exception e) {
            throw new SelectException("Error al filtrar envíos por estado: " + e.getMessage(), e);
        }
    }

    @GET
    @Path("filterNumPaquetes")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Envio> filterNumPaquetes(@QueryParam("numPaquetes") Integer numPaquetes) throws SelectException {
        if (numPaquetes == null) {
            throw new SelectException("El número de paquetes no puede ser null.");
        }

        try {
            Query query = em.createNamedQuery("Envio.filterNumPaquetes");
            query.setParameter("numPaquetes", numPaquetes);
            return query.getResultList();
        } catch (Exception e) {
            throw new SelectException("Error al filtrar por número de paquetes: " + e.getMessage(), e);
        }
    }

    @GET
    @Path("getRutaYEnvioRutaVehiculoPorMatricula")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getRutaYEnvioRutaVehiculoPorMatricula(@QueryParam("matricula") String matricula) {
        if (matricula == null || matricula.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("El parámetro matrícula es obligatorio.")
                    .build();
        }

        try {
            Query vehiculoQuery = em.createQuery("SELECT v.id FROM Vehiculo v WHERE v.matricula = :matricula");
            vehiculoQuery.setParameter("matricula", matricula);
            Integer vehiculoId = (Integer) vehiculoQuery.getSingleResult();

            Query query = em.createNamedQuery("Envio.getRutaYEnvioRutaVehiculoPorVehiculo");
            query.setParameter("vehiculoId", vehiculoId);
            Object[] result = (Object[]) query.getSingleResult();

            Ruta ruta = new Ruta();
            ruta.setLocalizador((Integer) result[0]);

            EnvioRutaVehiculo envioRutaVehiculo = new EnvioRutaVehiculo();
            envioRutaVehiculo.setRuta(ruta);
            envioRutaVehiculo.setId((Integer) result[1]);

            return Response.ok(envioRutaVehiculo).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al procesar la solicitud: " + e.getMessage())
                    .build();
        }
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        try {
            Envio envio = super.find(id);

            if (envio == null) {
                throw new NotFoundException("Envio con ID " + id + " no encontrado.");
            }
            Logger.getLogger(EnvioFacadeREST.class.getName()).log(Level.WARNING, envio.toString());
            super.remove(envio);
            Logger.getLogger(EnvioFacadeREST.class.getName()).log(Level.INFO, "Envio eliminado exitosamente: " + envio.toString());
        } catch (SelectException | DeleteException ex) {
            throw new InternalServerErrorException("Error removing Envio entity.", ex);
        }
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

}
