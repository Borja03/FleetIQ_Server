package service;

import entities.Ruta;
import exception.CreateException;
import exception.DeleteException;
import exception.SelectException;
import exception.UpdateException;
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
import javax.ws.rs.InternalServerErrorException;

/**
 * REST service for managing Ruta entities.
 */
@Stateless
@Path("ruta")
public class RutaFacadeREST extends AbstractFacade<Ruta> {

    @PersistenceContext(unitName = "FleetIQ_ServerPU")
    private EntityManager em;

    public RutaFacadeREST() {
        super(Ruta.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Ruta entity) {
        try {
            super.create(entity);
        } catch (CreateException ex) {
            throw new InternalServerErrorException("Error creating Ruta entity.", ex);
        }
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Ruta entity) {
        try {
            super.edit(entity);
        } catch (UpdateException ex) {
            throw new InternalServerErrorException("Error updating Ruta entity.", ex);
        }
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        try {
            super.remove(super.find(id));
        } catch (SelectException | DeleteException ex) {
            throw new InternalServerErrorException("Error removing Ruta entity.", ex);
        }
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Ruta find(@PathParam("id") Integer id) {
        try {
            return super.find(id);
        } catch (SelectException ex) {
            throw new InternalServerErrorException("Error finding Ruta entity.", ex);
        }
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Ruta> findAll() {
        try {
            return super.findAll();
        } catch (SelectException ex) {
            throw new InternalServerErrorException("Error finding all Ruta entities.", ex);
        }
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Ruta> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        try {
            return super.findRange(new int[]{from, to});
        } catch (SelectException ex) {
            throw new InternalServerErrorException("Error finding range of Ruta entities.", ex);
        }
    }

    @GET
    @Path("filterBy2Dates/{firstDate}/{secondDate}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Ruta> filterBy2Dates(@PathParam("firstDate") String firstDate, @PathParam("secondDate") String secondDate) {
        try {
            return em.createNamedQuery("Ruta.filterBy2Dates", Ruta.class)
                    .setParameter("firstDate", java.sql.Date.valueOf(firstDate))
                    .setParameter("secondDate", java.sql.Date.valueOf(secondDate))
                    .getResultList();
        } catch (Exception ex) {
            throw new InternalServerErrorException("Error filtering Ruta by dates.", ex);
        }
    }

    @GET
    @Path("filterTiempoMayor/{tiempo}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Ruta> filterTiempoMayor(@PathParam("tiempo") Integer tiempo) {
        try {
            return em.createNamedQuery("Ruta.filterTiempoMayor", Ruta.class)
                    .setParameter("tiempo", tiempo)
                    .getResultList();
        } catch (Exception ex) {
            throw new InternalServerErrorException("Error filtering Ruta by tiempo mayor.", ex);
        }
    }

    @GET
    @Path("filterTiempoMenor/{tiempo}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Ruta> filterTiempoMenor(@PathParam("tiempo") Integer tiempo) {
        try {
            return em.createNamedQuery("Ruta.filterTiempoMenor", Ruta.class)
                    .setParameter("tiempo", tiempo)
                    .getResultList();
        } catch (Exception ex) {
            throw new InternalServerErrorException("Error filtering Ruta by tiempo menor.", ex);
        }
    }

    @GET
    @Path("filterTiempoIgual/{tiempo}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Ruta> filterTiempoIgual(@PathParam("tiempo") Integer tiempo) {
        try {
            return em.createNamedQuery("Ruta.filterTiempoIgual", Ruta.class)
                    .setParameter("tiempo", tiempo)
                    .getResultList();
        } catch (Exception ex) {
            throw new InternalServerErrorException("Error filtering Ruta by tiempo igual.", ex);
        }
    }

    @GET
    @Path("filterDistanciaMayor/{distancia}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Ruta> filterDistanciaMayor(@PathParam("distancia") Float distancia) {
        try {
            return em.createNamedQuery("Ruta.filterDistanciaMayor", Ruta.class)
                    .setParameter("distancia", distancia)
                    .getResultList();
        } catch (Exception ex) {
            throw new InternalServerErrorException("Error filtering Ruta by distancia mayor.", ex);
        }
    }

    @GET
    @Path("filterDistanciaMenor/{distancia}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Ruta> filterDistanciaMenor(@PathParam("distancia") Float distancia) {
        try {
            return em.createNamedQuery("Ruta.filterDistanciaMenor", Ruta.class)
                    .setParameter("distancia", distancia)
                    .getResultList();
        } catch (Exception ex) {
            throw new InternalServerErrorException("Error filtering Ruta by distancia menor.", ex);
        }
    }

    @GET
    @Path("filterDistanciaIgual/{distancia}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Ruta> filterDistanciaIgual(@PathParam("distancia") Float distancia) {
        try {
            return em.createNamedQuery("Ruta.filterDistanciaIgual", Ruta.class)
                    .setParameter("distancia", distancia)
                    .getResultList();
        } catch (Exception ex) {
            throw new InternalServerErrorException("Error filtering Ruta by distancia igual.", ex);
        }
    }

    @GET
    @Path("findByLocalizadorInteger/{localizador}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Ruta findByLocalizadorInteger(@PathParam("localizador") Integer localizador) {
        try {
            return em.createNamedQuery("Ruta.findByLocalizadorInteger", Ruta.class)
                    .setParameter("localizador", localizador)
                    .getSingleResult();
        } catch (Exception ex) {
            throw new InternalServerErrorException("Error finding Ruta by localizador integer.", ex);
        }
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
