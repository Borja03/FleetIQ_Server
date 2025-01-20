package service;

import encryption.HashMD5;
import encryption.SymmetricDecrypt;
import entities.Admin;
import entities.Trabajador;
import entities.User;
import exception.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import utils.EmailSender;
import utils.Utils;

/**
 *
 * @author Omar
 */
/**
 * REST service for User entity management.
 */
@Stateless
@Path("user")
public class UserREST extends AbstractFacade<User> {

    private static final Logger LOGGER = Logger.getLogger(UserREST.class.getName());
    @PersistenceContext(unitName = "FleetIQ_ServerPU")
    private EntityManager em;

    SymmetricDecrypt symmetricDecrypt = new SymmetricDecrypt();

    public UserREST() {
        super(User.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML})
    public void create(User entity) {
        try {
            LOGGER.log(Level.INFO, "Creating user with ID: {0}", entity.getId());
            super.create(entity);
        } catch (CreateException ex) {
            LOGGER.log(Level.SEVERE, "Error creating user: {0}", ex.getMessage());
            throw new InternalServerErrorException("Unable to create user: " + ex.getMessage());
        }
    }

    @POST
    @Path("signup")
    @Consumes({MediaType.APPLICATION_XML})
    public User signUp(User entity) {
        try {
            LOGGER.log(Level.INFO, "User signup initiated for email: {0}", entity.getEmail());
            // Check if user already exists by email
            try {
                User existingUser = em.createNamedQuery("findUserByEmail", User.class)
                                .setParameter("userEmail", entity.getEmail())
                                .getSingleResult();
                if (existingUser != null) {
                    LOGGER.log(Level.WARNING, "User already exists with email: {0}", entity.getEmail());
                    throw new NotAuthorizedException("User already exists with this email.");
                }
            } catch (NoResultException ex) {
                LOGGER.log(Level.INFO, "No existing user found with email: {0}", entity.getEmail());
            }
            // Hash the password
            entity.setPassword(HashMD5.hashText(entity.getPassword()));

            // Determine user type and create corresponding entity
            User newUser;
            if ("admin".equals(entity.getUser_type())) {
                newUser = new Admin();
                if (entity instanceof Admin) {
                    ((Admin) newUser).setUltimoInicioSesion(((Admin) entity).getUltimoInicioSesion());
                }
            } else if ("trabajador".equals(entity.getUser_type())) {
                newUser = new Trabajador();
                if (entity instanceof Trabajador) {
                    ((Trabajador) newUser).setDepartamento(((Trabajador) entity).getDepartamento());
                }
            } else {
                LOGGER.severe("Invalid user type");
                throw new InternalServerErrorException("Invalid user type provided.");
            }
            // Set common properties
            newUser.setId(entity.getId());
            newUser.setName(entity.getName());
            newUser.setActivo(entity.isActivo());
            newUser.setCity(entity.getCity());
            newUser.setEmail(entity.getEmail());
            newUser.setStreet(entity.getStreet());
            newUser.setZip(entity.getZip());
            newUser.setVerifcationCode(entity.getVerifcationCode());
            newUser.setEnviosList(entity.getEnviosList());
            newUser.setUser_type(entity.getUser_type());

            // Save the new user
            super.create(newUser);

            return newUser;
        } catch ( Exception ex) {
            LOGGER.log(Level.SEVERE, "Error during signup: {0}", ex.getMessage());
            throw new InternalServerErrorException("Signup failed: " + ex.getMessage());
        }
    }

    @POST
    @Path("login")
    @Consumes({MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_XML})
    public User signIn(User user) {
        try {
            LOGGER.log(Level.INFO, "User login attempt: {0}", user.getEmail());
            User loggedInUser = em.createNamedQuery("signin", User.class)
                            .setParameter("userEmail", user.getEmail())
                            .setParameter("userPassword", HashMD5.hashText(user.getPassword()))
                            .getSingleResult();
            em.detach(loggedInUser);
            loggedInUser.setPassword(null);
            return loggedInUser;
        } catch (NoResultException ex) {
            LOGGER.log(Level.WARNING, "Invalid login credentials: {0}", user.getEmail());
            throw new NotAuthorizedException("Invalid login credentials.");
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Login failed: {0}", ex.getMessage());
            throw new InternalServerErrorException("Login failed: " + ex.getMessage());
        }
    }

    @POST
    @Path("check-exist")
    @Consumes({MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_XML})
    public User checkExist(User user) {
        try {
            LOGGER.log(Level.INFO, "Checking existence for email: {0}", user.getEmail());
            User existingUser = em.createNamedQuery("findUserByEmail", User.class)
                            .setParameter("userEmail", user.getEmail())
                            .getSingleResult();
            em.detach(existingUser);
            existingUser.setPassword(null);
            existingUser.setActivo(true);
            return existingUser;
        } catch (NoResultException ex) {
            LOGGER.log(Level.INFO, "User does not exist: {0}", user.getEmail());
            user.setActivo(false);
            return user;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error checking user existence: {0}", ex.getMessage());
            throw new InternalServerErrorException("Error checking user existence.");
        }
    }

    @POST
    @Path("reset")
    @Consumes({MediaType.APPLICATION_XML})
    public void resetPassword(User user) {
        try {
            LOGGER.log(Level.INFO, "Password reset initiated for email: {0}", user.getEmail());
            User existingUser = em.createNamedQuery("findUserByEmail", User.class)
                            .setParameter("userEmail", user.getEmail())
                            .getSingleResult();
            if (existingUser != null) {
                String resetCode = Utils.generateRandomCode();
                String[] credentials = symmetricDecrypt.descifrarCredenciales();
                EmailSender.sendEmail(credentials[0], credentials[1], user.getEmail(), resetCode, "rest");
                existingUser.setVerifcationCode(resetCode);
                //em.merge(existingUser);
                super.edit(existingUser);
            }
        } catch (UpdateException ex) {
            LOGGER.log(Level.SEVERE, "Error during password reset: {0}", ex.getMessage());
            throw new InternalServerErrorException("Password reset failed.");
        } catch (Exception ex) {
            Logger.getLogger(UserREST.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @PUT
    @Path("update-password")
    @Consumes({MediaType.APPLICATION_XML})
    public void updatePassword(User user) {
        try {
            LOGGER.log(Level.INFO, "Updating password for email: {0}", user.getEmail());
            User existingUser = em.createNamedQuery("findUserByEmail", User.class)
                            .setParameter("userEmail", user.getEmail())
                            .getSingleResult();
            if (existingUser != null) {
                existingUser.setPassword(HashMD5.hashText(user.getPassword()));
                super.edit(existingUser);
                //em.merge(existingUser);

                String[] credentials = symmetricDecrypt.descifrarCredenciales();
                EmailSender.sendEmail(credentials[0], credentials[1], user.getEmail(), "", "changed");
            }
        } catch (UpdateException ex) {
            LOGGER.log(Level.SEVERE, "Error updating password: {0}", ex.getMessage());
            throw new InternalServerErrorException("Password update failed.");
        } catch (Exception ex) {
            Logger.getLogger(UserREST.class.getName()).log(Level.SEVERE, null, ex);
            throw new InternalServerErrorException("Password update failed.");

        }
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML})
    public List<User> findAll() {
        try {
            LOGGER.log(Level.INFO, "Fetching all users.");
            return super.findAll();
        } catch (SelectException ex) {
            LOGGER.log(Level.SEVERE, "Error fetching all users: {0}", ex.getMessage());
            throw new InternalServerErrorException("Failed to retrieve users.");
        }
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
