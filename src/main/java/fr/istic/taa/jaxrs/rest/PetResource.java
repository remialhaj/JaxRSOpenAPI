package fr.istic.taa.jaxrs.rest;

import fr.istic.taa.jaxrs.dao.dao.PetDao;
import fr.istic.taa.jaxrs.domain.Pet;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("pet")
@Produces({"application/json", "application/xml"})
public class PetResource {

  @GET
  @Path("/{petId}")
  public Pet getPetById(@PathParam("petId") String petId) {
    PetDao petDao = new PetDao();
    return petDao.findOne(petId);
  }

  @GET
  @Path("/")
  public List<Pet> getAllPets() {
    PetDao petDao = new PetDao();
    return petDao.findAll();
  }


  @POST
  @Consumes("application/json")
  public Response addPet(Pet pet) {
    PetDao petDao = new PetDao();
    petDao.save(pet);
    return Response.status(Response.Status.CREATED).entity("Pet saved").build();
  }

  @PUT
  @Consumes("application/json")
  @Path("/{petId}")
  public Pet updatePet(@PathParam("petId") String petId, Pet updatedPet) {
    PetDao petDao = new PetDao();
    Pet existingPet = petDao.findOne(petId);

    if (existingPet != null) {
      updatedPet.setId(Long.parseLong(petId)); // Assurez-vous que l'ID est correctement défini pour l'animal mis à jour
      petDao.update(updatedPet);
      return updatedPet;
    } else {
      throw new NotFoundException("Pet with ID " + petId + " not found");
    }
  }

  @DELETE
  @Path("/{petId}")
  public Response deletePet(@PathParam("petId") String petId) {
    PetDao petDao = new PetDao();
    Pet petToDelete = petDao.findOne(petId);

    if (petToDelete != null) {
      petDao.deleteById(petId);
      return Response.ok("Pet with ID " + petId + " deleted successfully").build();
    } else {
      throw new NotFoundException("Pet with ID " + petId + " not found");
    }
  }
}