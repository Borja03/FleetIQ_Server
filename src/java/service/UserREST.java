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
import utils.EmailSender;
import utils.Utils;

/**
 * RESTful service for User management.
 */
@Stateless
@Path("user")
public class UserREST extends AbstractFacade<User> {

    private static final Logger LOGGER = Logger.getLogger(UserREST.class.getName());
    @PersistenceContext(unitName = "FleetIQ_ServerPU")
    private EntityManager em;
    private final SymmetricDecrypt symDecrypt = new SymmetricDecrypt();

    public UserREST() {
        super(User.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML})
    public void create(User entity) throws CreateException {
        try {
            LOGGER.log(Level.INFO, "Creating user with email: {0}", entity.getEmail());
            super.create(entity);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error creating user: {0}", ex.getMessage());
            throw new InternalServerErrorException("Failed to create user.");
        }
    }

    @POST
    @Path("signup")
    @Consumes({MediaType.APPLICATION_XML})
    public User signUp(User entity) {
        LOGGER.log(Level.INFO, "User signup attempt: {0}", entity.getEmail());
        try {
            entity.setPassword(HashMD5.hashText(entity.getPassword()));
            User newUser;
            if ("admin".equalsIgnoreCase(entity.getUser_type())) {
                LOGGER.info("Registering as Admin.");
                newUser = new Admin();
            } else if ("trabajador".equalsIgnoreCase(entity.getUser_type())) {
                LOGGER.info("Registering as Trabajador.");
                newUser = new Trabajador();
            } else {
                LOGGER.warning("Invalid user type during signup.");
                throw new NotAuthorizedException("Invalid user type.");
            }
            if (em.contains(entity)) {
                LOGGER.warning("email already existe.");
                throw new NotAuthorizedException("email already existe");
            }
            newUser.setId(entity.getId());
            newUser.setName(entity.getName());
            newUser.setCity(entity.getCity());
            newUser.setEmail(entity.getEmail());
            newUser.setPassword(entity.getPassword());
            newUser.setStreet(entity.getStreet());
            newUser.setZip(entity.getZip());
            super.create(newUser);

            em.detach(newUser);
            newUser.setPassword(null);
            LOGGER.log(Level.INFO, "User {0} signed up successfully.", newUser.getEmail());
            return newUser;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error during signup: {0}", ex.getMessage());
            throw new InternalServerErrorException("Signup failed: " + ex.getMessage());
        }
    }

    @POST
    @Path("login")
    @Consumes({MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_XML})
    public User signIn(User user) {
        LOGGER.log(Level.INFO, "User login attempt: {0}", user.getEmail());
        try {
            User userLogged = em.createNamedQuery("signin", User.class)
                            .setParameter("userEmail", user.getEmail())
                            .setParameter("userPassword", HashMD5.hashText(user.getPassword()))
                            .getSingleResult();
            if (userLogged == null) {
                LOGGER.warning("Invalid login credentials for user: " + user.getEmail());
                throw new NotAuthorizedException("Invalid credentials.");
            }
            em.detach(userLogged);
            userLogged.setPassword(null);
            LOGGER.log(Level.INFO, "User {0} logged in successfully.", userLogged.getEmail());
            return userLogged;
        } catch (NoResultException ex) {
            LOGGER.warning("Login failed - no user found: " + user.getEmail());
            throw new NotAuthorizedException("Invalid credentials.");
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error during login: {0}", ex.getMessage());
            throw new InternalServerErrorException("Login failed: " + ex.getMessage());
        }
    }

    @POST
    @Path("reset")
    @Consumes({MediaType.APPLICATION_XML})
    public void resetPassword(User user) {
        LOGGER.log(Level.INFO, "Reset password attempt for user: {0}", user.getEmail());
        try {
            User userExist = em.createNamedQuery("findUserByEmail", User.class)
                            .setParameter("userEmail", user.getEmail())
                            .getSingleResult();
            if (userExist != null) {
                String resetCode = Utils.generateRandomCode();
                String[] credentials = symDecrypt.descifrarCredenciales();
                EmailSender.sendEmail(credentials[0], credentials[1], user.getEmail(), resetCode, "reset");
                userExist.setVerifcationCode(resetCode);
                em.merge(userExist);
                LOGGER.log(Level.INFO, "Reset code sent to user: {0}", user.getEmail());
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error resetting password: {0}", ex.getMessage());
            throw new InternalServerErrorException("Failed to reset password.");
        }
    }

    @PUT
    @Path("update-password")
    @Consumes({MediaType.APPLICATION_XML})
    public void updatePassword(User user) {
        LOGGER.log(Level.INFO, "Updating password for user: {0}", user.getEmail());
        try {
            User dbUser = em.createNamedQuery("findUserByEmail", User.class)
                            .setParameter("userEmail", user.getEmail())
                            .getSingleResult();
            if (dbUser != null) {
                dbUser.setPassword(HashMD5.hashText(user.getPassword()));
                em.merge(dbUser);
                String[] credentials = symDecrypt.descifrarCredenciales();
                EmailSender.sendEmail(credentials[0], credentials[1], user.getEmail(), "", "changed");
                LOGGER.log(Level.INFO, "Password updated successfully for user: {0}", user.getEmail());
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error updating password: {0}", ex.getMessage());
            throw new InternalServerErrorException("Failed to update password.");
        }
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML})
    public List<User> findAll() throws SelectException {
        try {
            List<User> users = super.findAll();
            LOGGER.log(Level.INFO, "Retrieved all users.");
            return users;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to fetch users: {0}", e.getMessage());
            throw new SelectException("Unable to fetch users.");
        }
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
