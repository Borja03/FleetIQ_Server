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
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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

            Query query = em.createNamedQuery("Envio.filterByDates");
            query.setParameter("firstDate", first);
            query.setParameter("secondDate", second);
            return query.getResultList();

        } catch (Exception e) {
            throw new InternalServerErrorException("Fecha inválida, por favor use el formato yyyy-MM-dd");
        }
    }

    @GET
    @Path("filterEstado")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Envio> filterEstado(@QueryParam("estado") String estadoS) throws SelectException {
        Query query = em.createNamedQuery("Envio.filterEstado");
        Estado estado = Estado.valueOf(estadoS.toUpperCase());
        query.setParameter("estado", estado);
        return query.getResultList();
    }

    @GET
    @Path("filterNumPaquetes")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Envio> filterNumPaquetes(@QueryParam("numPaquetes") Integer numPaquetes) throws SelectException {
        if (numPaquetes == null) {
            throw new IllegalArgumentException("El número de paquetes no puede ser null.");
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
                    .entity("El parámetro matricula es obligatorio.")
                    .build();
        }

        try {
            // Buscar el ID del vehículo a partir de la matrícula
            Query vehiculoQuery = em.createQuery("SELECT v.id FROM Vehiculo v WHERE v.matricula = :matricula");
            vehiculoQuery.setParameter("matricula", matricula);

            // Obtener el vehiculoId
            Integer vehiculoId = (Integer) vehiculoQuery.getSingleResult();

            if (vehiculoId == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("No se encontró ningún vehículo con matrícula: " + matricula)
                        .build();
            }

            // Ejecutar la consulta para obtener la ruta y el ID de EnvioRutaVehiculo usando vehiculoId
            Query query = em.createNamedQuery("Envio.getRutaYEnvioRutaVehiculoPorVehiculo");
            query.setParameter("vehiculoId", vehiculoId);

            // Obtener el primer (y único) resultado de la consulta
            Object[] result = (Object[]) query.getSingleResult(); // Obtiene el único resultado

            if (result == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("No se encontró ninguna ruta o EnvioRutaVehiculo para el vehículo con ID: " + vehiculoId)
                        .build();
            }

            // Crear los objetos Ruta y EnvioRutaVehiculo
            Integer localizador = (Integer) result[0];
            Integer envioRutaVehiculoId = (Integer) result[1];

            Ruta ruta = new Ruta();
            ruta.setLocalizador(localizador);

            EnvioRutaVehiculo envioRutaVehiculo = new EnvioRutaVehiculo();
            envioRutaVehiculo.setRuta(ruta);
            envioRutaVehiculo.setId(envioRutaVehiculoId);

            // Devolver el resultado en formato JSON
            return Response.ok(envioRutaVehiculo).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al procesar la solicitud: " + e.getMessage())
                    .build();
        }
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

}
