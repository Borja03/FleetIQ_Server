package service;

import encryption.HashMD5;
import encryption.ServerSideDecryption;
import encryption.SymmetricDecrypt;
import entities.Admin;
import entities.Trabajador;
import entities.User;
import exception.*;
import java.util.Base64;
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
 * REST service for User entity management.
 */
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
    @Produces({MediaType.APPLICATION_XML})
    public User signUp(User entity) {
        String decryptedPassword = null;
        try {
            LOGGER.log(Level.INFO, "User login attempt: {0}", entity.getEmail());
            String encryptedBase64 = entity.getPassword();
            if (encryptedBase64 == null || encryptedBase64.isEmpty()) {
                LOGGER.log(Level.SEVERE, "Encrypted Base64 string is empty");
            } else {
                try {
                    byte[] encryptedData = Base64.getDecoder().decode(encryptedBase64);
                    // Call ServerSideDecryption to decrypt the message
                    decryptedPassword = ServerSideDecryption.decrypt(encryptedData);
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Decryption failed", e);
                }
            }
            LOGGER.log(Level.INFO, "User signup initiated for email: {0}", entity.getEmail());

            // Check if user already exists by email
            List<User> existingUsers = em.createNamedQuery("findUserByEmail", User.class)
                            .setParameter("userEmail", entity.getEmail())
                            .getResultList();
            if (!existingUsers.isEmpty()) {
                LOGGER.log(Level.WARNING, "User already exists with email: {0}", entity.getEmail());
                throw new NotAuthorizedException("User already exists with this email.");
            }

            // Validate user type and create entity accordingly
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
                throw new BadRequestException("Unsupported user type.");
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

            newUser.setPassword(HashMD5.hashText(decryptedPassword));

            // Persist the user
            super.create(newUser);
            em.detach(newUser);

            newUser.setPassword(null);
            return newUser;
        } catch (NotAuthorizedException ex) {
            LOGGER.log(Level.WARNING, "Signup authorization issue: {0}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error during signup", ex);
            throw new InternalServerErrorException("Signup failed. Please try again later." + ex.getMessage());
        }
    }

    @POST
    @Path("login")
    @Consumes({MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_XML})
    public User signIn(User user) {
        String decryptedPassword = null;
        try {

            LOGGER.log(Level.INFO, "User login attempt: {0}", user.getEmail());
            String encryptedBase64 = user.getPassword();

            if (encryptedBase64 == null || encryptedBase64.isEmpty()) {
                LOGGER.log(Level.SEVERE, "Encrypted Base64 string is empty");
            } else {
                try {
                    byte[] encryptedData = Base64.getDecoder().decode(encryptedBase64);

                    // Call ServerSideDecryption to decrypt the message
                    decryptedPassword = ServerSideDecryption.decrypt(encryptedData);
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Decryption failed", e);
                }
            }
            User loggedInUser = em.createNamedQuery("signin", User.class)
                            .setParameter("userEmail", user.getEmail())
                            .setParameter("userPassword", HashMD5.hashText(decryptedPassword))
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
                super.edit(existingUser);
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error during password reset", ex);
            throw new InternalServerErrorException("Password reset failed.");
        }
    }

    @POST
    @Path("verify-code")
    @Consumes({MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_XML})
    public User verifyCode(User user) {
        try {
            User dbUser = em.createNamedQuery("findUserByEmail", User.class)
                            .setParameter("userEmail", user.getEmail())
                            .getSingleResult();

            if (dbUser.getVerifcationCode().equals(user.getVerifcationCode())) {
                dbUser.setVerifcationCode("");
                super.edit(dbUser);
                user.setActivo(true);
                return user;
            }
            LOGGER.log(Level.WARNING, "Verification codes do not match for email: {0}", user.getEmail());
            return user;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error verifying code: {0}", e.getMessage());
            throw new InternalServerErrorException("Error verifying code.");
        }
    }

    @PUT
    @Path("update-password")
    @Consumes({MediaType.APPLICATION_XML})
    public void updatePassword(User user) {
        String decryptedPass = null;
        try {
            LOGGER.log(Level.INFO, "Updating password for email: {0}", user.getEmail());
            User existingUser = em.createNamedQuery("findUserByEmail", User.class)
                            .setParameter("userEmail", user.getEmail())
                            .getSingleResult();
            if (existingUser != null) {
                String encryptedBase64 = user.getPassword();
                try {
                    byte[] encryptedData = Base64.getDecoder().decode(encryptedBase64);
                    decryptedPass = ServerSideDecryption.decrypt(encryptedData);
                    System.out.println("Decrypted Message: " + decryptedPass);
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Decryption failed", e);
                }
                existingUser.setPassword(HashMD5.hashText(decryptedPass));
                super.edit(existingUser);

                String[] credentials = symmetricDecrypt.descifrarCredenciales();
                EmailSender.sendEmail(credentials[0], credentials[1], user.getEmail(), "", "changed");
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error updating password: {0}", ex.getMessage());
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
