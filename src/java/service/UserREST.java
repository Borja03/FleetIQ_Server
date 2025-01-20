package service;

import encryption.HashMD5;
import encryption.SymmetricDecrypt;
import entities.User;
import exception.CreateException;
import exception.SelectException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import utils.EmailSender;
import utils.Utils;

@Stateless
@Path("user")
public class UserREST extends AbstractFacade<User> {

    private static final Logger LOGGER = Logger.getLogger(UserREST.class.getName());

    @PersistenceContext(unitName = "FleetIQ_ServerPU")
    private EntityManager em;

    private final SymmetricDecrypt symmetricDecrypt = new SymmetricDecrypt();

    public UserREST() {
        super(User.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML})
    public void create(User entity) throws CreateException {
        try {
            super.create(entity);
            LOGGER.log(Level.INFO, "User created: {0}", entity);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error creating user: {0}", e.getMessage());
            throw new CreateException("Error creating user.");
        }
    }

    @POST
    @Path("signup")
    @Consumes({MediaType.APPLICATION_XML})
    public User signUp(User entity) throws CreateException {
        try {

            User userExist = em.createNamedQuery("findUserByEmail", User.class)
                            .setParameter("userEmail", entity.getEmail())
                            .getSingleResult();

            if (userExist != null) { // Corrected the syntax and condition
                throw new CreateException("Email already exists.");
            }
            entity.setPassword(HashMD5.hashText(entity.getPassword()));
            super.create(entity);

            em.detach(entity);
            entity.setPassword(null);
            LOGGER.log(Level.INFO, "User signed up: {0}", entity);
            return entity;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Sign-up failed: {0}", e.getMessage());
            throw new CreateException("Sign-up failed: " + e.getMessage());
        }
    }

    @POST
    @Path("login")
    @Consumes({MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_XML})
    public User signIn(User user) throws SelectException {
        try {
            User userLogged = em.createNamedQuery("signin", User.class)
                            .setParameter("userEmail", user.getEmail())
                            .setParameter("userPassword", HashMD5.hashText(user.getPassword()))
                            .getSingleResult();

            em.detach(userLogged);
            userLogged.setPassword(null);
            LOGGER.log(Level.INFO, "User logged in: {0}", userLogged.getEmail());
            return userLogged;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Login failed: {0}", e.getMessage());
            throw new SelectException("Invalid credentials or login failed.");
        }
    }

    @POST
    @Path("reset")
    @Consumes({MediaType.APPLICATION_XML})
    public void resetPassword(User user) throws SelectException {
        try {
            User userExist = em.createNamedQuery("findUserByEmail", User.class)
                            .setParameter("userEmail", user.getEmail())
                            .getSingleResult();

            String resetCode = Utils.generateRandomCode();
            String[] credentials = symmetricDecrypt.descifrarCredenciales();
            EmailSender.sendEmail(credentials[0], credentials[1], user.getEmail(), resetCode, "Password Reset");

            userExist.setVerifcationCode(resetCode);
            em.merge(userExist);
            LOGGER.log(Level.INFO, "Password reset code sent to: {0}", user.getEmail());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to reset password: {0}", e.getMessage());
            throw new SelectException("Failed to reset password.");
        }
    }

    @POST
    @Path("check-exist")
    @Consumes({MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_XML})
    public User checkExist(User user) {
        User userExist;
        try {
            userExist = em.createNamedQuery("findUserByEmail", User.class)
                            .setParameter("userEmail", user.getEmail())
                            .getSingleResult();

            em.detach(userExist);
            userExist.setPassword(null);
            userExist.setActivo(true);
            LOGGER.log(Level.INFO, "User exists: {0}", user.getEmail());
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "User not found: {0}", user.getEmail());
            userExist = new User();
            userExist.setActivo(false);
        }
        return userExist;
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
