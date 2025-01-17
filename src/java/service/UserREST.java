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
@Stateless
@Path("user")
public class UserREST extends AbstractFacade<User> {

    private static final Logger LOGGER = Logger.getLogger(UserREST.class.getName());
    @PersistenceContext(unitName = "FleetIQ_ServerPU")
    private EntityManager em;
    SymmetricDecrypt symetrica = new SymmetricDecrypt();

    public UserREST() {
        super(User.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML})
    public void create(User entity) throws CreateException {
        super.create(entity);
    }

    @POST
    @Path("signup")
    @Consumes({MediaType.APPLICATION_XML})
    public User signUp(User entity) {
        LOGGER.info("before entering signup");
        User newUser = null;
        entity.setPassword(HashMD5.hashText(entity.getPassword()));
        LOGGER.info("after hashing");
   
        if ("admin".equals(entity.getUser_type())) {
            LOGGER.info("befor admin");
            newUser = new Admin(); // Create a new Admin object
            if (entity instanceof Admin) {
                ((Admin) newUser).setUltimoInicioSesion(((Admin) entity).getUltimoInicioSesion());
            }
            LOGGER.info("as admin");
        } else if ("trabajador".equals(entity.getUser_type())) {
            LOGGER.info("befor Trabajador");
            newUser = new Trabajador(); // Create a new Admin object
            if (entity instanceof Admin) {
                ((Trabajador) newUser).setDepartamento(((Trabajador) entity).getDepartamento());
            }
            LOGGER.info("as Trabajador");
        }
        newUser.setId(entity.getId());
        newUser.setName(entity.getName());
        newUser.setActivo(entity.isActivo());
        newUser.setCity(entity.getCity());
        newUser.setEmail(entity.getEmail());
        newUser.setPassword(entity.getPassword());
        newUser.setStreet(entity.getStreet());
        newUser.setZip(entity.getZip());
        newUser.setVerifcationCode(entity.getVerifcationCode());
        newUser.setEnviosList(entity.getEnviosList());
        newUser.setUser_type(entity.getUser_type());
        try {
            super.create(newUser);

        } catch (Exception e) {
            e.printStackTrace(); // This will help you see the actual error
        }
        return newUser;
    }

    @POST
    @Path("login")
    @Consumes({MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_XML})
    public User signIn(User user) throws SelectException {
        LOGGER.info("before entering try");
        try {
            // First try to find user with provided credentials
            LOGGER.info("before calling  signin");
            User userLogged = em.createNamedQuery("signin", User.class)
                            .setParameter("userEmail", user.getEmail())
                            .setParameter("userPassword", HashMD5.hashText(user.getPassword()))
                            .getSingleResult();
            LOGGER.info("after calling  signin :" + userLogged.toString());

            // Check if user exists
            if (userLogged == null) {
                throw new SelectException("Invalid credentials");
            }
            
            em.detach(userLogged);
            userLogged.setPassword(null);
            LOGGER.info(userLogged.toString());
            return userLogged;

        } catch (NoResultException nre) {
            throw new SelectException("Invalid credentials");
        } catch (Exception ex) {
            throw new SelectException("Login failed: " + ex.getMessage());
        }
    }

    @POST
    @Path("check-exist")
    @Consumes({MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_XML})
    public User checkExist(User user) {
        // User userExist = new User();
        User userExist = null;
        try {
            userExist = em.createNamedQuery("findUserByEmail", User.class)
                            .setParameter("userEmail", user.getEmail())
                            .getSingleResult();

            em.detach(userExist);
            userExist.setPassword(null);
            userExist.setActivo(true);
            return userExist;
        } catch (Exception e) {
            System.out.println("Exception caught: " + e.getMessage());
            e.printStackTrace();
        }

        userExist.setActivo(false);
        return userExist;
    }

    @POST
    @Path("reset")
    @Consumes({MediaType.APPLICATION_XML})
    public void resetPassword(User user) {
        try {
            User userExist = em.createNamedQuery("findUserByEmail", User.class)
                            .setParameter("userEmail", user.getEmail())
                            .getSingleResult();
            if (userExist != null) {
                String resetCode = Utils.generateRandomCode();
                String[] credenciales = symetrica.descifrarCredenciales();
                EmailSender.sendEmail(credenciales[0], credenciales[1], user.getEmail(), resetCode, "rest");
                userExist.setVerifcationCode(resetCode);
                em.merge(userExist);
            }
        } catch (Exception e) {

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
                //here to add hash 
                em.merge(dbUser);  // Save the changes
                user.setActivo(true);
                return user;
            }
            System.out.println("Codes don't match, returning false");
            return user;
        } catch (Exception e) {
            System.out.println("Exception caught: " + e.getMessage());
            e.printStackTrace();
            return user;
        }
    }

    @PUT
    @Path("update-password")
    @Consumes({MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_XML})
    public void updatePassword(User user) {
        try {
            User dbUser = em.createNamedQuery("findUserByEmail", User.class)
                            .setParameter("userEmail", user.getEmail())
                            .getSingleResult();
            // hash
            if (dbUser != null) {
                dbUser.setPassword(HashMD5.hashText(user.getPassword()));
                em.merge(dbUser);
                //send email to informe user
                String[] credenciales = symetrica.descifrarCredenciales();
                EmailSender.sendEmail(credenciales[0], credenciales[1], user.getEmail(), "", "changed");

            }
        } catch (Exception e) {
            //
        }
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML})
    public List<User> findAll() throws SelectException {
        return super.findAll();
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

}
