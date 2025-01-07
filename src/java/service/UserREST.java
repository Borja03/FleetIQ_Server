/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import entities.User;
import exception.CreateException;
import exception.DeleteException;
import exception.EmailAlreadyExistsException;
import exception.SelectException;
import exception.UpdateException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

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
 * @author Omar
 */
@Stateless
@Path("entities.userentity")
public class UserREST extends AbstractFacade<User> {

    @PersistenceContext(unitName = "FleetIQ_ServerPU")
    private EntityManager em;

    public UserREST() {
        super(User.class);
    }

    @POST
    @Consumes({MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_XML})
    public User createUser(User user) throws CreateException {
        try {
            User existeUser = em.createNamedQuery("findUserByEmail", User.class)
                            .setParameter("email", user.getEmail())
                            .getSingleResult();
            throw new EmailAlreadyExistsException("User with this email already exists");

        } catch (NoResultException e) {
            super.create(user);
            return user;
        } catch (EmailAlreadyExistsException ex) {
            Logger.getLogger(UserREST.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML})
    public void edit(@PathParam("id") Long id, User entity) throws UpdateException {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Long id) throws SelectException, DeleteException {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML})
    public User find(@PathParam("id") Long id) throws SelectException {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML})
    public List<User> findAll() throws SelectException {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML})
    public List<User> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) throws SelectException {
        return super.findRange(new int[]{from, to});
    }

    //@GET
    //@Path("count")
    //@Produces(MediaType.TEXT_PLAIN)
    //public String countREST() {
    //    return String.valueOf(super.count());
    //}

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

}
