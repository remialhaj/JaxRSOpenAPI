package fr.istic.taa.jaxrs.rest;

import fr.istic.taa.jaxrs.dao.dao.AdminDao;
import fr.istic.taa.jaxrs.domain.Admin;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("admin")
public class AdminRessource {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addAdmin(Admin admin) {
        AdminDao adminDao = new AdminDao();
        adminDao.save(admin);
        return Response.status(Response.Status.CREATED).entity("Admin saved").build();
    }

    @GET
    @Path("/all")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<Admin> getAllAdmins() {
        AdminDao adminDao = new AdminDao();
        return adminDao.findAll();
    }
}
