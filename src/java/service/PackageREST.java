package service;

import entities.Paquete;
import entities.PackageSize;
import exception.CreateException;
import exception.DeleteException;
import exception.SelectException;
import exception.UpdateException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Omar
 */
/**
 *
 */
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
    @Produces({MediaType.APPLICATION_XML , MediaType.APPLICATION_JSON})
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
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Paquete updatePackage(@PathParam("id") Long id, Paquete paquete) {
        try {
            LOGGER.log(Level.INFO, "Attempting to update package with ID: {0}", id);

            // Check if entity exists and is in persistence context
            if (!getEntityManager().contains(find(id))) {
                LOGGER.log(Level.WARNING, "Package with ID {0} is not in persistence context", id);
                throw new NotFoundException("Package not found or no longer in persistence context: " + id);
            }

            paquete.setId(id);
            super.edit(paquete);

            LOGGER.log(Level.INFO, "Successfully updated package with ID: {0}", id);
            return paquete;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to update package with ID: " + id, e);
            throw new InternalServerErrorException("Failed to update package" +e.getMessage());
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
                throw new NotFoundException("Package not found with ID: " + id);
            }
            super.remove(packageToDelete);
            LOGGER.log(Level.INFO, "Successfully deleted package with ID: {0}", id);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to delete package with ID: " + id, e);
            throw new InternalServerErrorException("Failed to delete package");
        }
    }

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
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
    @Produces({MediaType.APPLICATION_XML , MediaType.APPLICATION_JSON})
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
    @Path("name/{name}")
    @Produces({MediaType.APPLICATION_XML , MediaType.APPLICATION_JSON})
    public List<Paquete> findPackagesByName(@PathParam("name") String name) {
        try {
            LOGGER.log(Level.INFO, "Searching packages by name: {0}", name);
            List<Paquete> packages = em.createNamedQuery("findByName", Paquete.class)
                            .setParameter("name", "%" + name + "%")
                            .getResultList();
            LOGGER.log(Level.INFO, "Found {0} packages matching name: {1}", new Object[]{packages.size(), name});
            return packages;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to fetch packages by name: " + name, e);
            throw new InternalServerErrorException("Failed to fetch packages by name");
        }
    }

    @GET
    @Path("date/between")
    @Produces({MediaType.APPLICATION_XML , MediaType.APPLICATION_JSON})
    public List<Paquete> findPackagesBetweenDates(
                    @QueryParam("startDate") @DefaultValue("") String startDate,
                    @QueryParam("endDate") @DefaultValue("") String endDate) {
        try {
            LOGGER.log(Level.INFO, "Fetching packages between {0} and {1}", new Object[]{startDate, endDate});

            if (startDate.isEmpty() || endDate.isEmpty()) {
                throw new BadRequestException("Both startDate and endDate must be provided");
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date start = dateFormat.parse(startDate);
            Date end = dateFormat.parse(endDate);

            if (start.after(end)) {
                throw new BadRequestException("Start date must be before end date");
            }

            return em.createNamedQuery("findByDateRange", Paquete.class)
                            .setParameter("startDate", start)
                            .setParameter("endDate", end)
                            .getResultList();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to parse date parameters", e);
            throw new InternalServerErrorException("Invalid date format. Expected dd-MM-yyyy.");
        }
    }

    @GET
    @Path("date/after")
    @Produces({MediaType.APPLICATION_XML , MediaType.APPLICATION_JSON})
    public List<Paquete> findPackagesAfterDate(
                    @QueryParam("startDate") @DefaultValue("") String startDate) {
        try {
            LOGGER.log(Level.INFO, "Fetching packages after {0}", startDate);

            if (startDate.isEmpty()) {
                throw new BadRequestException("startDate must be provided");
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date start = dateFormat.parse(startDate);

            return em.createNamedQuery("findAfterDate", Paquete.class)
                            .setParameter("startDate", start)
                            .getResultList();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to parse startDate parameter", e);
            throw new InternalServerErrorException("Invalid date format. Expected dd-MM-yyyy.");
        }
    }

    @GET
    @Path("date/before")
    @Produces({MediaType.APPLICATION_XML , MediaType.APPLICATION_JSON})
    public List<Paquete> findPackagesBeforeDate(
                    @QueryParam("endDate") @DefaultValue("") String endDate) {
        try {
            LOGGER.log(Level.INFO, "Fetching packages before {0}", endDate);

            if (endDate.isEmpty()) {
                throw new BadRequestException("endDate must be provided");
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date end = dateFormat.parse(endDate);

            return em.createNamedQuery("findBeforeDate", Paquete.class)
                            .setParameter("endDate", end)
                            .getResultList();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to parse endDate parameter", e);
            throw new InternalServerErrorException("Invalid date format. Expected dd-MM-yyyy.");
        }
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
