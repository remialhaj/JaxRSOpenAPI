package fr.istic.taa.jaxrs.rest;

import fr.istic.taa.jaxrs.dao.dao.*;
import fr.istic.taa.jaxrs.domain.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Date;
import java.util.List;

@Path("admin")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AdminRessource {

    @POST
    @Path("/register")
    public Response addAdmin(Admin admin) {
        AdminDao adminDao = new AdminDao();
        Admin existingAdmin = adminDao.findByEmail(admin.getEmail());
        Admin existingAdmin2 = adminDao.findByUsername(admin.getUsername());

        if (existingAdmin != null) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("{\"error\": \"Admin with email " + admin.getEmail() + " already exists\"}")
                    .build();
        }else if (existingAdmin2 != null) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("{\"error\": \"Admin with username " + admin.getUsername() + " already exists\"}")
                    .build();
        }else {
            adminDao.save(admin);
            return Response.status(Response.Status.CREATED)
                    .entity("{\"message\": \"Admin created successfully\"}")
                    .build();
        }
    }

    @GET
    @Path("/all")
    public List<Admin> getAllAdmins() {
        AdminDao adminDao = new AdminDao();
        return adminDao.findAll();
    }

    @POST
    @Path("/tags")
    public Response createTag(Tag tag) {
        TagDao tagDao = new TagDao();
        Tag existingTag = tagDao.findByName(tag.getName());

        if (existingTag != null) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("{\"error\": \"Tag with name " + tag.getName() + " already exists\"}")
                    .build();
        } else {
            tagDao.save(tag);
            return Response.status(Response.Status.CREATED)
                    .entity("{\"message\": \"Tag created successfully\"}")
                    .build();
        }
    }


    @GET
    @Path("/allusers")
    public List<User> getAllUsers() {
        UserDao userDao = new UserDao();
        return userDao.findAll();
    }

    @PUT
    @Path("/ticket/{ticketId}/status")
    public Response updateTicketStatus(@PathParam("ticketId") Long ticketId, Boolean status) {
        TicketDao ticketDao = new TicketDao();
        ticketDao.updateStatus(ticketId, status);
        return Response.status(Response.Status.OK)
                .entity("{\"message\": \"Ticket status updated successfully\"}")
                .build();
    }

    @GET
    @Path("/{userId}/tickets")
    public List<Ticket> getTicketsForAdmin(@PathParam("userId") String userId) {
        AdminDao adminDao = new AdminDao();
        Admin admin = adminDao.findByEmail(userId);

        if (admin != null) {
            TicketDao ticketDao = new TicketDao();
            return ticketDao.findTicketsAssignedToAdmin(admin.getId());
        }else {
            throw new NotFoundException("User with ID " + userId + " not found");
        }
    }

    @GET
    @Path("/{userId}/ticketscomments")
    public List<Ticket> getTicketsCreatedByUser(@PathParam("userId") String userId) {
        AdminDao adminDao = new AdminDao();
        Admin admin = adminDao.findByEmail(userId);

        if (admin != null) {
            TicketDao ticketDao = new TicketDao();
            List<Ticket> ticketsAssignedToAdmin = ticketDao.findTicketsAssignedToAdmin(admin.getId());
            for (Ticket ticket : ticketsAssignedToAdmin) {
                ticket.setComments(loadCommentsForTicket(ticket.getId()));
            }

            return ticketsAssignedToAdmin;
        } else {
            throw new NotFoundException("User with ID " + userId + " not found");
        }
    }

    private List<Comment> loadCommentsForTicket(Long ticketId) {
        CommentDao commentDao = new CommentDao();
        return commentDao.findByTicketId(ticketId);
    }

    @POST
    @Path("/{userId}/tickets/{ticketId}/comments")
    public Response addCommentToTicket(@PathParam("userId") String userEmail, @PathParam("ticketId") Long ticketId, Comment comment) {
        AdminDao adminDao = new AdminDao();
        Admin admin = adminDao.findByEmail(userEmail);

        if (admin != null) {
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
                newComment.setCreatedBy(admin);
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

}
