package fr.istic.taa.jaxrs.rest;

import fr.istic.taa.jaxrs.dao.dao.*;
import fr.istic.taa.jaxrs.domain.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.util.*;

@Path("user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    // Se connecter, supprimer, ajouter un USER
    @POST
    @Path("/login")
    public Response loginUser(User user) {
        UserDao userDao = new UserDao();
        User existingUser = userDao.findByEmail(user.getEmail());

        if (existingUser != null) {
            if (existingUser.getPassword().equals(user.getPassword())) {
                return Response.status(Response.Status.OK)
                        .entity("{\"email\": \"" + existingUser.getEmail() + "\"}")
                        .build();
            } else {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity("{\"error\": \"Invalid email or password\"}")
                        .build();
            }
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"User with email " + user.getEmail() + " not found\"}")
                    .build();
        }


    }

    @DELETE
    @Path("/{userId}")
    public Response deleteUser(@PathParam("userId") String userId) {
        UserDao userDao = new UserDao();
        User userToDelete = userDao.findByEmail(userId);

        if (userToDelete != null) {
            userDao.delete(userToDelete);
            return Response.status(Response.Status.OK)
                    .entity("{\"message\": \"User with ID " + userId + " deleted successfully\"}")
                    .build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"User with ID " + userId + " not found\"}")
                    .build();
        }
    }

    @POST
    @Path("/register")
    public Response addUser(User user) {
        UserDao userDao = new UserDao();
        User existingUser = userDao.findByEmail(user.getEmail());
        User existingUser2 = userDao.findByUsername(user.getUsername());

        if (existingUser != null) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("{\"error\": \"User with email " + user.getEmail() + " already exists\"}")
                    .build();
        }else if (existingUser2 != null) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("{\"error\": \"User with username " + user.getUsername() + " already exists\"}")
                    .build();
        }else {
            userDao.save(user);
            return Response.status(Response.Status.CREATED)
                    .entity("{\"message\": \"User created successfully\"}")
                    .build();
        }

    }
////////////////////////////////////////////////////////////////////////////////////////////


   // Récupérer les tickets, les tickets avec commentaires, ajouter un ticket, ajouter un commentaire
    @GET
    @Path("/{userId}/tickets")
    public List<Ticket> getTicketsForUser(@PathParam("userId") String userId) {
        UserDao userDao = new UserDao();
        User user = userDao.findByEmail(userId);

        if (user != null) {
            TicketDao ticketDao = new TicketDao();
            return ticketDao.findTicketsCreatedByUser(user.getId());
        }else {
            throw new NotFoundException("User with ID " + userId + " not found");
        }
    }

    @GET
    @Path("/{userId}/ticketscomments")
    public List<Ticket> getTicketsCreatedByUser(@PathParam("userId") String userId) {
        UserDao userDao = new UserDao();
        User user = userDao.findByEmail(userId);

        if (user != null) {
            TicketDao ticketDao = new TicketDao();
            List<Ticket> ticketsCreatedByUser = ticketDao.findTicketsCreatedByUser(user.getId());
            for (Ticket ticket : ticketsCreatedByUser) {
                ticket.setComments(loadCommentsForTicket(ticket.getId()));
            }

            return ticketsCreatedByUser;
        } else {
            throw new NotFoundException("User with ID " + userId + " not found");
        }
    }

    private List<Comment> loadCommentsForTicket(Long ticketId) {
        CommentDao commentDao = new CommentDao();
        return commentDao.findByTicketId(ticketId);
    }

    @POST
    @Path("/{userId}/tickets")
    public Response createTicketForUser(@PathParam("userId") String userId, Ticket ticket) {
        UserDao userDao = new UserDao();
        User user = userDao.findByEmail(userId);

        if (user != null) {
            if (ticket.getTitle() == null || ticket.getDescription() == null || ticket.getAssignedTo() == null ) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\": \"Ticket information incomplete\"}")
                        .build();
            }

            // Créez un nouveau ticket avec les détails fournis
            Ticket newTicket = new Ticket();
            newTicket.setTitle(ticket.getTitle());
            newTicket.setDescription(ticket.getDescription());
            newTicket.setCreatedBy(user);
            newTicket.setAssignedTo(ticket.getAssignedTo());
            newTicket.setTags(ticket.getTags());
            newTicket.setCreatedDate(new Date());
            newTicket.setResolved(false);

            // Enregistrez le nouveau ticket dans la base de données
            TicketDao ticketDao = new TicketDao();
            ticketDao.save(newTicket);

            return Response.status(Response.Status.CREATED)
                    .entity("{\"message\": \"Ticket created successfully\"}")
                    .build();

        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"User with ID " + userId + " not found\"}")
                    .build();
        }
    }

    @POST
    @Path("/{userId}/tickets/{ticketId}/comments")
    public Response addCommentToTicket(@PathParam("userId") String userEmail, @PathParam("ticketId") Long ticketId, Comment comment) {
        UserDao userDao = new UserDao();
        User user = userDao.findByEmail(userEmail);

        if (user != null) {
            TicketDao ticketDao = new TicketDao();
            Ticket ticket = ticketDao.findOne(ticketId.toString());

            if (ticket != null) {
                if (comment.getContent() == null) {
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity("{\"error\": \"Comment content missing\"}")
                            .build();
                }

                Comment newComment = new Comment();
                newComment.setContent(comment.getContent());
                newComment.setCreatedBy(user);
                newComment.setTicket(ticket);
                newComment.setCreatedDate(new Date());

                CommentDao commentDao = new CommentDao();
                commentDao.save(newComment);

                return Response.status(Response.Status.CREATED)
                        .entity("{\"message\": \"Comment added successfully\"}")
                        .build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\": \"Ticket with ID " + ticketId + " not found\"}")
                        .build();
            }
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"User with email " + userEmail + " not found\"}")
                    .build();
        }
    }
////////////////////////////////////////////////////////////////////////////////////////////


    // Récupérer les tags
    @GET
    @Path("/tags")
    public List<Tag> getAllTags() {
        TagDao tagDao = new TagDao();
        return tagDao.findAll();
    }

////////////////////////////////////////////////////////////////////////////////////////////


    @GET
    @Path("/{userId}")
    public User getUserById(@PathParam("userId") String userId) {
        UserDao userDao = new UserDao();
        return userDao.findOne(userId);
    }

    @PUT
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
    @Path("/tickets/{ticketId}")
    public Response deleteTicket(@PathParam("ticketId") Long ticketId) {
        TicketDao ticketDao = new TicketDao();
        Ticket ticketToDelete = ticketDao.findOne(ticketId.toString());

        if (ticketToDelete != null) {
            ticketDao.delete(ticketToDelete);
            return Response.ok("Ticket with ID " + ticketId + " deleted successfully").build();
        } else {
            throw new NotFoundException("Ticket with ID " + ticketId + " not found");
        }
    }

}