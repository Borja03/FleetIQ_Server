package service;

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

@Stateless
@Path("user")
public class UserREST extends AbstractFacade<User> {

    private static final Logger LOGGER = Logger.getLogger(UserREST.class.getName());

    @PersistenceContext(unitName = "FleetIQ_ServerPU")
    private EntityManager em;

    public UserREST() {
        super(User.class);
    }

    @GET
    @Produces({MediaType.APPLICATION_XML})
    public List<User> findAllUsers() throws SelectException {
        try {
            return super.findAll();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error retrieving users", ex);
            throw new SelectException("Failed to retrieve users");
        }
    }

    @POST
    @Consumes({MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_XML})
    public User createUser(User user) throws CreateException {
        try {        
            // Check for existing email
            if (findUserByEmail(user.getEmail()) != null) {
                throw new CreateException("Email already exists");
            }

            // Hash password before storing
            //user.setPassword(hashPassword(user.getPassword()));
            super.create(user);
            return user;

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error creating user", ex);
            throw new CreateException(ex.getMessage());
        }
    }

//    @POST
//    @Consumes({MediaType.APPLICATION_XML})
//    @Produces({MediaType.APPLICATION_XML})
//    public User loginUser(User user) throws SelectException {
//        try {
//            User dbUser = findUserByEmail(user.getEmail());
//            
//            if (dbUser == null) {
//                throw new SelectException("Invalid credentials");
//            }
//
//            // Verify password
//            if (user.getPassword().equals(dbUser.getPassword())) {
//                throw new SelectException("Invalid credentials");
//            }
//            // do not send password 
//            dbUser.setPassword(null);
//            return dbUser;
//
//        } catch (Exception ex) {
//            LOGGER.log(Level.SEVERE, "Error during login", ex);
//            throw new SelectException("Login failed");
//        }
//    }

    @PUT
    @Path("/reset-password")
    @Consumes({MediaType.APPLICATION_XML})
    public void requestPasswordReset(String email) throws SelectException {
        try {
            User user = findUserByEmail(email);
            if (user != null) {
                String verificationCode = Utils.generateRandomCode();
                user.setVerifcationCode(verificationCode);
                super.edit(user);
                EmailSender.sendEmail(email, verificationCode);
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Failed to process password reset", ex);
            throw new SelectException("Password reset failed");
        }
    }

//    @PUT
//    @Path("/verify-reset")
//    @Consumes(MediaType.APPLICATION_XML)
//    public void verifyAndUpdatePassword(User user) throws UpdateException {
//        try {
//            User user = findUserByEmail(user.getEmail());
//            if (user == null) {
//                throw new UpdateException("Invalid request");
//            }
//
//            if (!SecurityUtils.verifyCode(user.getCode(), user.getVerifcationCode()) || 
//                SecurityUtils.isCodeExpired(user.getVerificationExpiry())) {
//                throw new UpdateException("Invalid or expired verification code");
//            }
//
//            if (!isValidPassword(user.getNewPassword())) {
//                throw new UpdateException("Invalid password format");
//            }
//
//            user.setPassword(SecurityUtils.hashPassword(user.getNewPassword()));
//            user.setVerifcationCode(null);
//            super.edit(user);
//
//        } catch (Exception ex) {
//            LOGGER.log(Level.SEVERE, "Error updating password", ex);
//            throw new UpdateException("Password update failed");
//        }
//    }

    @PUT
    @Path("/update/{id}")
    @Consumes({MediaType.APPLICATION_XML})
    public void updateUser(@PathParam("id") Long id, User user) throws UpdateException {
        try {
            User existUser = super.find(id);
            if (existUser == null) {
                throw new UpdateException("User not found");
            }
            existUser.setPassword(user.getPassword());
            super.edit(existUser);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error updating user", ex);
            throw new UpdateException("Update failed");
        }
    }

    private User findUserByEmail(String email) {
        try {
            return em.createNamedQuery("findUserByEmail", User.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}