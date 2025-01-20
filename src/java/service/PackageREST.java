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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Stateless
@Path("paquete")
public class PackageREST extends AbstractFacade<Paquete> {

    private static final Logger LOGGER = Logger.getLogger(PackageREST.class.getName());

    @PersistenceContext(unitName = "FleetIQ_ServerPU")
    private EntityManager em;

    public PackageREST() {
        super(Paquete.class);
    }

    @POST
    @Consumes({MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_XML})
    public Paquete createPackage(Paquete paquete) {
        try {
            LOGGER.log(Level.INFO, "Attempting to create package with name: {0}", paquete.getId());
            super.create(paquete);
            LOGGER.log(Level.INFO, "Successfully created package with ID: {0}", paquete.getId());
            return paquete;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to create package", e);
            throw new InternalServerErrorException("Failed to create package");
        }
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_XML})
    public Paquete updatePackage(@PathParam("id") Long id, Paquete paquete) {
        try {
            LOGGER.log(Level.INFO, "Attempting to update package with ID: {0}", id);
            Paquete existingPackage = super.find(paquete.getId());
            if (existingPackage == null) {
                LOGGER.log(Level.WARNING, "Package not found with ID: {0}", id);
                throw new NotAuthorizedException("Package not found");
            }
            super.edit(paquete);
            LOGGER.log(Level.INFO, "Successfully updated package with ID: {0}", id);
            return paquete;
        } catch (NotAuthorizedException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to update package with ID: " + id, e);
            throw new InternalServerErrorException("Failed to update package");
        }
    }

    @DELETE
    @Path("{id}")
    public void deletePackage(@PathParam("id") Long id) {
        try {
            LOGGER.log(Level.INFO, "Attempting to delete package with ID: {0}", id);
            Paquete packageToDelete = super.find(id);
            if (packageToDelete == null) {
                LOGGER.log(Level.WARNING, "Package not found for deletion with ID: {0}", id);
                throw new NotAuthorizedException("Package not found");
            }
            super.remove(packageToDelete);
            LOGGER.log(Level.INFO, "Successfully deleted package with ID: {0}", id);
        } catch (NotAuthorizedException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to delete package with ID: " + id, e);
            throw new InternalServerErrorException("Failed to delete package");
        }
    }

    @GET
    @Produces({MediaType.APPLICATION_XML})
    public List<Paquete> findAllPackages() {
        try {
            LOGGER.log(Level.INFO, "Fetching all packages");
            List<Paquete> packages = super.findAll();
            LOGGER.log(Level.INFO, "Retrieved {0} packages", packages.size());
            return packages;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to fetch all packages", e);
            throw new InternalServerErrorException("Failed to fetch packages");
        }
    }

    @GET
    @Path("size/{size}")
    @Produces({MediaType.APPLICATION_XML})
    public List<Paquete> findPackagesBySize(@PathParam("size") PackageSize size) {
        try {
            LOGGER.log(Level.INFO, "Searching packages by size: {0}", size);
            List<Paquete> packages = em.createNamedQuery("findBySize", Paquete.class)
                    .setParameter("size", size)
                    .getResultList();
            LOGGER.log(Level.INFO, "Found {0} packages with size {1}", new Object[]{packages.size(), size});
            return packages;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to fetch packages by size: " + size, e);
            throw new InternalServerErrorException("Failed to fetch packages by size");
        }
    }

    @GET
    @Path("date")
    @Produces({MediaType.APPLICATION_XML})
    public List<Paquete> findPackagesByDates(
            @QueryParam("startDate") @DefaultValue("") String startDate,
            @QueryParam("endDate") @DefaultValue("") String endDate) {
        try {
            LOGGER.log(Level.INFO, "Searching packages between dates: {0} and {1}", 
                new Object[]{startDate, endDate});
            
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
                    LOGGER.log(Level.WARNING, "Invalid date range: start date after end date");
                    throw new NotAuthorizedException("Start date must be before end date");
                }
                List<Paquete> packages = em.createNamedQuery("findByDateRange", Paquete.class)
                        .setParameter("startDate", start)
                        .setParameter("endDate", end)
                        .getResultList();
                LOGGER.log(Level.INFO, "Found {0} packages in date range", packages.size());
                return packages;
            } else if (start != null) {
                List<Paquete> packages = em.createNamedQuery("findAfterDate", Paquete.class)
                        .setParameter("startDate", start)
                        .getResultList();
                LOGGER.log(Level.INFO, "Found {0} packages after date", packages.size());
                return packages;
            } else if (end != null) {
                List<Paquete> packages = em.createNamedQuery("findBeforeDate", Paquete.class)
                        .setParameter("endDate", end)
                        .getResultList();
                LOGGER.log(Level.INFO, "Found {0} packages before date", packages.size());
                return packages;
            } else {
                LOGGER.log(Level.WARNING, "No date parameters provided");
                throw new NotAuthorizedException("At least one date parameter must be provided");
            }
        } catch (NotAuthorizedException e) {
            throw e;
        } catch (ParseException e) {
            LOGGER.log(Level.SEVERE, "Failed to parse date parameters", e);
            throw new InternalServerErrorException("Invalid date format");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to fetch packages by date", e);
            throw new InternalServerErrorException("Failed to fetch packages by date");
        }
    }

    @GET
    @Path("name/{name}")
    @Produces({MediaType.APPLICATION_XML})
    public List<Paquete> findPackagesByName(@PathParam("name") String name) {
        try {
            LOGGER.log(Level.INFO, "Searching packages by name: {0}", name);
            List<Paquete> packages = em.createNamedQuery("findByName", Paquete.class)
                    .setParameter("name", "%" + name + "%")
                    .getResultList();
            LOGGER.log(Level.INFO, "Found {0} packages matching name: {1}", 
                new Object[]{packages.size(), name});
            return packages;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to fetch packages by name: " + name, e);
            throw new InternalServerErrorException("Failed to fetch packages by name");
        }
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}