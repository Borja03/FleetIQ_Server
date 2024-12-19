/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import entities.Package;
import entities.PackageSize;
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
 * @author Omar
 */
@Stateless
@Path("package")
public class PackageREST extends AbstractFacade<Package> {

    @PersistenceContext(unitName = "FleetIQ_ServerPU")
    private EntityManager em;

    public PackageREST() {
        super(Package.class);
    }
 

    @POST
    @Consumes({MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_XML})
    public Package createPackage(Package packageEntity) throws CreateException {
        try {
            super.create(packageEntity);
            return packageEntity;
        } catch (Exception e) {
            throw new CreateException("Error creating package: " + e.getMessage());
        }
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_XML})
    public Package updatePackage(@PathParam("id") Long id, Package packageEntity) throws UpdateException {
        try {
            Package existingPackage = super.find(id);
            if (existingPackage == null) {
                throw new UpdateException("Package not found with id: " + id);
            }
            packageEntity.setId(id);
             super.edit(packageEntity);
             return packageEntity;
        } catch (Exception e) {
            throw new UpdateException("Error updating package: " + e.getMessage());
        }
    }

    @DELETE
    @Path("{id}")
    public void deletePackage(@PathParam("id") Integer id) throws DeleteException {
        try {
            Package packageEntity = super.find(id);
            if (packageEntity == null) {
                throw new DeleteException("Package not found with id: " + id);
            }
            super.remove(packageEntity);
        } catch (Exception e) {
            throw new DeleteException("Error deleting package: " + e.getMessage());
        }
    }

    @GET
    @Produces({MediaType.APPLICATION_XML})
    public List<Package> findAllPackages() throws SelectException {
        try {
            return super.findAll();
        } catch (Exception e) {
            throw new SelectException("Error retrieving packages: " + e.getMessage());
        }
    }

    @GET
    @Path("size/{size}")
    @Produces({MediaType.APPLICATION_XML})
    public List<Package> findPackagesBySize(@PathParam("size") PackageSize size) throws SelectException {
        try {
            return em.createNamedQuery("findBySize", Package.class)
                     .setParameter("size", size)
                     .getResultList();
        } catch (Exception e) {
            throw new SelectException("Error retrieving packages by size: " + e.getMessage());
        }
    }


    
    
    @GET
   @Path("date")
    @Produces({MediaType.APPLICATION_XML})
    public List<Package> findPackagesByDates( @QueryParam("startDate") Date startDate,  @QueryParam("endDate") Date endDate
    ) throws SelectException {
        try {
            if (startDate != null && endDate != null) {
                return em.createNamedQuery("findByDateRange", Package.class)
                         .setParameter("startDate", startDate)
                         .setParameter("endDate", endDate)
                         .getResultList();
            } else if (startDate != null) {
                return em.createNamedQuery("findAfterDate", Package.class)
                         .setParameter("startDate", startDate)
                         .getResultList();
            } else if (endDate != null) {
                return em.createNamedQuery("findBeforeDate", Package.class)
                         .setParameter("endDate", endDate)
                         .getResultList();
            } else {
                throw new SelectException("At least one date must be provided.");
            }
        } catch (Exception e) {
            throw new SelectException("Error retrieving packages by dates: " + e.getMessage());
        }
    }

    @GET
    @Path("name/{name}")
    @Produces({MediaType.APPLICATION_XML})
    public List<Package> findPackagesByName(@PathParam("name") String name) throws SelectException {
        try {
            return em.createNamedQuery("findByName", Package.class)
                     .setParameter("name", "%" + name + "%")
                     .getResultList();
        } catch (Exception e) {
            throw new SelectException("Error retrieving packages by name: " + e.getMessage());
        }
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}