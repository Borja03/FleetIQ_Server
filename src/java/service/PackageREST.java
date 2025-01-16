/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import entities.Paquete;
import entities.PackageSize;
import exception.CreateException;
import exception.DeleteException;
import exception.SelectException;
import exception.UpdateException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
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
@Path("paquete")
public class PackageREST extends AbstractFacade<Paquete> {

    @PersistenceContext(unitName = "FleetIQ_ServerPU")
    private EntityManager em;

    public PackageREST() {
        super(Paquete.class);
    }

    @POST
    @Consumes({MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_XML})
    public Paquete createPackage(Paquete paquete) throws CreateException {
        try {
            super.create(paquete);
            return paquete;
        } catch (Exception e) {
            throw new CreateException("Error creating package: " + e.getMessage());
        }
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_XML})
    public Paquete updatePackage(@PathParam("id") Long id, Paquete paquete) throws UpdateException {
        try {
            Paquete existingPackage = super.find(paquete.getId());
            if (existingPackage == null) {
                throw new UpdateException("Package not found with id: " + paquete.getId());
            }
            super.edit(paquete);
            return paquete;
        } catch (Exception e) {
            throw new UpdateException("Error updating package: " + e.getMessage());
        }
    }

    @DELETE
    @Path("{id}")
    public void deletePackage(@PathParam("id") Long id) throws DeleteException {
        try {
            Paquete packageToDelete = super.find(id);
            if (packageToDelete == null) {
                throw new DeleteException("Package not found with id: " + id);
            }
            super.remove(packageToDelete);
        } catch (Exception ex) {
            throw new DeleteException("Error deleting package: " + ex.getMessage());
        }
    }

    @GET
    @Produces({MediaType.APPLICATION_XML})
    public List<Paquete> findAllPackages() throws SelectException {
        try {
            return super.findAll();
        } catch (Exception ex) {
            throw new SelectException("Error retrieving packages: " + ex.getMessage());
        }
    }

    @GET
    @Path("size/{size}")
    @Produces({MediaType.APPLICATION_XML})
    public List<Paquete> findPackagesBySize(@PathParam("size") PackageSize size) throws SelectException {
        try {
            return em.createNamedQuery("findBySize", Paquete.class)
                            .setParameter("size", size)
                            .getResultList();
        } catch (Exception ex) {
            throw new SelectException("Error retrieving packages by size: " + ex.getMessage());
        }
    }

    @GET
    @Path("date")
    @Produces({MediaType.APPLICATION_XML})
    public List<Paquete> findPackagesByDates(
                    @QueryParam("startDate") @DefaultValue("") String startDate,
                    @QueryParam("endDate") @DefaultValue("") String endDate) throws SelectException {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            
            Date start = null;
            Date end = null;

            if (!startDate.isEmpty()) {
                start = dateFormat.parse(startDate);
            }
            if (!endDate.isEmpty()) {
                end = dateFormat.parse(endDate);
            }

            if (start != null && end != null) {
                if (start.after(end)) {
                    throw new SelectException("Start date must be before end date");
                }
                return em.createNamedQuery("findByDateRange", Paquete.class)
                                .setParameter("startDate", start)
                                .setParameter("endDate", end)
                                .getResultList();
            } else if (start != null) {
                return em.createNamedQuery("findAfterDate", Paquete.class)
                                .setParameter("startDate", start)
                                .getResultList();
            } else if (end != null) {
                return em.createNamedQuery("findBeforeDate", Paquete.class)
                                .setParameter("endDate", end)
                                .getResultList();
            } else {
                throw new SelectException("At least one date parameter must be provided");
            }
        } catch (Exception e) {
            throw new SelectException("Error retrieving packages by dates: " + e.getMessage());
        }
    }

    @GET
    @Path("name/{name}")
    @Produces({MediaType.APPLICATION_XML})
    public List<Paquete> findPackagesByName(@PathParam("name") String name) throws SelectException {
        try {
            return em.createNamedQuery("findByName", Paquete.class)
                            .setParameter("name", "%" + name + "%")
                            .getResultList();
        } catch (Exception ex) {
            throw new SelectException("Error retrieving packages by name: " + ex.getMessage());
        }
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
