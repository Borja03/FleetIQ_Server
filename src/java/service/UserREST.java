package service;

import encryption.HashMD5;
import encryption.SymmetricDecrypt;
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
    User userTemp = null;
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
    public User signUp(User entity) throws CreateException {
        try {
            //decrypt asymetric 
            //hash
            entity.setPassword(HashMD5.hashText(entity.getPassword()));
            super.create(entity);
           
            
            em.detach(entity);
            entity.setPassword(null);
            return entity;
        } catch (Exception e) {
            //
            return null;
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
                            //.setParameter("userPassword",user.getPassword() )
                            //decrypr asy
                            .setParameter("userPassword", HashMD5.hashText(user.getPassword()) )
                            .getSingleResult();
            if (userLogged == null) {
                throw new SelectException("Invalid credentials");
            }
            
            
            em.detach(userLogged);
            userLogged.setPassword(null);
            return userLogged;

        } catch (Exception ex) {
            throw new SelectException("Login failed: " + ex.getMessage());
        }
    }

    @POST
    @Path("check-exist")
    @Consumes({MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_XML})
    public User checkExist(User user) {
        User userExist = new User();
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
