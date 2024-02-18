package fr.istic.taa.jaxrs.rest;

import fr.istic.taa.jaxrs.dao.dao.*;
import fr.istic.taa.jaxrs.domain.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import java.util.*;

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

    @POST
    @Path("/{userId}/tickets")
    @Consumes("application/json")
    public Response createTicketForUser(@PathParam("userId") Long userId, Ticket ticket) {
        UserDao userDao = new UserDao();
        User user = userDao.findOne(userId.toString()); // Convertir l'ID de String à Long

        if (user != null) {
            // Assurez-vous que le ticket a toutes les informations nécessaires
            if (ticket.getTitle() == null || ticket.getDescription() == null ||
                    ticket.getCreatedBy() == null || ticket.getAssignedTo() == null ||
                    ticket.getTags() == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Ticket information incomplete")
                        .build();
            }

            // Créez un nouveau ticket avec les détails fournis
            Ticket newTicket = new Ticket();
            newTicket.setTitle(ticket.getTitle());
            newTicket.setDescription(ticket.getDescription());
            newTicket.setCreatedBy(ticket.getCreatedBy());
            newTicket.setAssignedTo(ticket.getAssignedTo());
            newTicket.setTags(ticket.getTags());
            newTicket.setCreatedDate(new Date());

            // Enregistrez le nouveau ticket dans la base de données
            TicketDao ticketDao = new TicketDao();
            ticketDao.save(newTicket);

            return Response.status(Response.Status.CREATED)
                    .entity("Ticket created successfully")
                    .build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("User with ID " + userId + " not found")
                    .build();
        }
    }

    @GET
    @Path("/{userId}/tickets")
    public List<Ticket> getTicketsCreatedByUser(@PathParam("userId") Long userId) {
        UserDao userDao = new UserDao();
        User user = userDao.findOne(userId.toString());

        if (user != null) {
            TicketDao ticketDao = new TicketDao();
            List<Ticket> ticketsCreatedByUser = ticketDao.findTicketsCreatedByUser(userId);
            return ticketsCreatedByUser;
        } else {
            throw new NotFoundException("User with ID " + userId + " not found");
        }
    }


    @POST
    @Path("/tags")
    @Consumes("application/json")
    public Response createTag(Tag tag) {
        TagDao tagDao = new TagDao();
        tagDao.save(tag);
        return Response.status(Response.Status.CREATED).entity("Tag created successfully").build();
    }

    @POST
    @Path("/{userId}/tickets/{ticketId}/comments")
    @Consumes("application/json")
    public Response addCommentToTicket(
            @PathParam("userId") Long userId,
            @PathParam("ticketId") Long ticketId,
            Comment comment) {
        UserDao userDao = new UserDao();
        TicketDao ticketDao = new TicketDao();

        // Vérifie si l'utilisateur existe
        User user = userDao.findOne(userId.toString());
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("User with ID " + userId + " not found")
                    .build();
        }

        // Vérifie si le ticket existe
        Ticket ticket = ticketDao.findOne(ticketId.toString());
        if (ticket == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Ticket with ID " + ticketId + " not found")
                    .build();
        }

        // Assurez-vous que le commentaire a toutes les informations nécessaires
        if (comment.getContent() == null || comment.getCreatedBy() == null ) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Comment information incomplete")
                    .build();
        }

        // Associer le commentaire au ticket et à l'utilisateur
        comment.setTicket(ticket);
        comment.setCreatedBy(user);
        comment.setCreatedDate(new Date());

        // Enregistrer le commentaire dans la base de données
        CommentDao commentDao = new CommentDao();
        commentDao.save(comment);

        return Response.status(Response.Status.CREATED)
                .entity("Comment added to ticket successfully")
                .build();
    }


}
