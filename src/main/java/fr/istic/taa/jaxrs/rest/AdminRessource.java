package fr.istic.taa.jaxrs.rest;

import fr.istic.taa.jaxrs.dao.dao.AdminDao;
import fr.istic.taa.jaxrs.domain.Admin;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("admin")
public class AdminRessource {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addAdmin(Admin admin) {
        AdminDao adminDao = new AdminDao();
        adminDao.save(admin);
        return Response.status(Response.Status.CREATED).entity("Admin saved").build();
    }
}
