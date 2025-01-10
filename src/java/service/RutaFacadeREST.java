/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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

/**
 *
 * @author Borja
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
    public void create(Ruta entity) throws CreateException {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Ruta entity) throws UpdateException {
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
    public Ruta find(@PathParam("id") Integer id) throws SelectException {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Ruta> findAll() throws SelectException {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Ruta> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) throws SelectException {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    @GET
    @Path("filterBy2Dates/{firstDate}/{secondDate}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Ruta> filterBy2Dates(@PathParam("firstDate") String firstDate, @PathParam("secondDate") String secondDate) throws SelectException {
        return em.createNamedQuery("Ruta.filterBy2Dates", Ruta.class)
                .setParameter("firstDate", java.sql.Date.valueOf(firstDate))
                .setParameter("secondDate", java.sql.Date.valueOf(secondDate))
                .getResultList();
    }

    @GET
    @Path("filterTiempoMayor/{tiempo}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Ruta> filterTiempoMayor(@PathParam("tiempo") Integer tiempo) throws SelectException {
        return em.createNamedQuery("Ruta.filterTiempoMayor", Ruta.class)
                .setParameter("tiempo", tiempo)
                .getResultList();
    }

    @GET
    @Path("filterTiempoMenor/{tiempo}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Ruta> filterTiempoMenor(@PathParam("tiempo") Integer tiempo) throws SelectException {
        return em.createNamedQuery("Ruta.filterTiempoMenor", Ruta.class)
                .setParameter("tiempo", tiempo)
                .getResultList();
    }

    @GET
    @Path("filterTiempoIgual/{tiempo}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Ruta> filterTiempoIgual(@PathParam("tiempo") Integer tiempo) throws SelectException {
        return em.createNamedQuery("Ruta.filterTiempoIgual", Ruta.class)
                .setParameter("tiempo", tiempo)
                .getResultList();
    }

    @GET
    @Path("filterDistanciaMayor/{distancia}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Ruta> filterDistanciaMayor(@PathParam("distancia") Float distancia) throws SelectException {
        return em.createNamedQuery("Ruta.filterDistanciaMayor", Ruta.class)
                .setParameter("distancia", distancia)
                .getResultList();
    }

    @GET
    @Path("filterDistanciaMenor/{distancia}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Ruta> filterDistanciaMenor(@PathParam("distancia") Float distancia) throws SelectException {
        return em.createNamedQuery("Ruta.filterDistanciaMenor", Ruta.class)
                .setParameter("distancia", distancia)
                .getResultList();
    }

    @GET
    @Path("filterDistanciaIgual/{distancia}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Ruta> filterDistanciaIgual(@PathParam("distancia") Float distancia) throws SelectException {
        return em.createNamedQuery("Ruta.filterDistanciaIgual", Ruta.class)
                .setParameter("distancia", distancia)
                .getResultList();
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}

