/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import entities.Ruta;
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
 * @author 2dam
 */
@Stateless
@Path("entities.ruta")
public class RutaFacadeREST extends AbstractFacade<Ruta> {

    @PersistenceContext(unitName = "JavaFX-WebApplicationUD5ExamplePU")
    private EntityManager em;

    public RutaFacadeREST() {
        super(Ruta.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Ruta entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Ruta entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Ruta find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Ruta> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Ruta> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    // Filter by dates
    @GET
    @Path("filterByDates/{firstDate}/{secondDate}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Ruta> filterByDates(
            @PathParam("firstDate") String firstDate,
            @PathParam("secondDate") String secondDate) {
        return em.createNamedQuery("Ruta.filterByDates", Ruta.class)
                .setParameter("firstDate", java.sql.Date.valueOf(firstDate))
                .setParameter("secondDate", java.sql.Date.valueOf(secondDate))
                .getResultList();
    }

    // Filter by tiempo > X
    @GET
    @Path("filterTiempoMayor/{tiempo}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Ruta> filterTiempoMayor(@PathParam("tiempo") Integer tiempo) {
        return em.createNamedQuery("Ruta.filterTiempoMayor", Ruta.class)
                .setParameter("tiempo", tiempo)
                .getResultList();
    }

    // Filter by tiempo < X
    @GET
    @Path("filterTiempoMenor/{tiempo}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Ruta> filterTiempoMenor(@PathParam("tiempo") Integer tiempo) {
        return em.createNamedQuery("Ruta.filterTiempoMenor", Ruta.class)
                .setParameter("tiempo", tiempo)
                .getResultList();
    }

    // Filter by tiempo = X
    @GET
    @Path("filterTiempoIgual/{tiempo}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Ruta> filterTiempoIgual(@PathParam("tiempo") Integer tiempo) {
        return em.createNamedQuery("Ruta.filterTiempoIgual", Ruta.class)
                .setParameter("tiempo", tiempo)
                .getResultList();
    }

    // Filter by distancia > X
    @GET
    @Path("filterDistanciaMayor/{distancia}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Ruta> filterDistanciaMayor(@PathParam("distancia") Float distancia) {
        return em.createNamedQuery("Ruta.filterDistanciaMayor", Ruta.class)
                .setParameter("distancia", distancia)
                .getResultList();
    }

    // Filter by distancia < X
    @GET
    @Path("filterDistanciaMenor/{distancia}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Ruta> filterDistanciaMenor(@PathParam("distancia") Float distancia) {
        return em.createNamedQuery("Ruta.filterDistanciaMenor", Ruta.class)
                .setParameter("distancia", distancia)
                .getResultList();
    }

    // Filter by distancia = X
    @GET
    @Path("filterDistanciaIgual/{distancia}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Ruta> filterDistanciaIgual(@PathParam("distancia") Float distancia) {
        return em.createNamedQuery("Ruta.filterDistanciaIgual", Ruta.class)
                .setParameter("distancia", distancia)
                .getResultList();
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
