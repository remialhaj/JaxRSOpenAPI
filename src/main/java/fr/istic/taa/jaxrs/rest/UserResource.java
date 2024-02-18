package fr.istic.taa.jaxrs.rest;

import fr.istic.taa.jaxrs.dao.dao.UserDao;
import fr.istic.taa.jaxrs.domain.User;
import jakarta.ws.rs.*;

import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("user")
@Produces({"application/json", "application/xml"})
public class UserResource {

    @GET
    @Path("/{userId}")
    public User getUserById(@PathParam("userId") String userId) {
        UserDao userDao = new UserDao();
        return userDao.findOne(userId);
    }

    @GET
    @Path("/")
    public List<User> getAllUsers() {
        UserDao userDao = new UserDao();
        return userDao.findAll();
    }

    @POST
    @Consumes("application/json")
    public Response addUser(User user) {
        UserDao userDao = new UserDao();
        userDao.save(user);
        return Response.status(Response.Status.CREATED).entity("User saved").build();
    }

    @PUT
    @Consumes("application/json")
    @Path("/{userId}")
    public User updateUser(@PathParam("userId") String userId, User updatedUser) {
        UserDao userDao = new UserDao();
        User existingUser = userDao.findOne(userId);

        if (existingUser != null) {
            updatedUser.setId(Long.parseLong(userId)); // Assurez-vous que l'ID est correctement défini pour l'utilisateur mis à jour
            userDao.update(updatedUser);
            return updatedUser;
        } else {
            throw new NotFoundException("User with ID " + userId + " not found");
        }
    }

    @DELETE
    @Path("/{userId}")
    public Response deleteUser(@PathParam("userId") String userId) {
        UserDao userDao = new UserDao();
        User userToDelete = userDao.findOne(userId);

        if (userToDelete != null) {
            userDao.deleteById(userId);
            return Response.ok("User with ID " + userId + " deleted successfully").build();
        } else {
            throw new NotFoundException("User with ID " + userId + " not found");
        }
    }
}
