package bloodbank.rest.resource;

import static bloodbank.utility.MyConstants.*;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.security.enterprise.SecurityContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import bloodbank.ejb.BloodBankService;
import bloodbank.entity.BloodType;

@Path("BloodType")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class BloodTypeResource {

	private static final Logger LOG = LogManager.getLogger();
	
	@EJB
	protected BloodBankService service;

	@Inject
	protected SecurityContext sc;
	
	@GET
    @RolesAllowed({ADMIN_ROLE})
	public Response getPhones() {
		LOG.debug("retrieving all blood types ...");
		List<BloodType> bloodTypes = service.getAllBloodTypes();
		Response response = Response.ok(bloodTypes).build();
		return response;
	}
	
	@GET
	@RolesAllowed({ADMIN_ROLE, USER_ROLE})
	@Path(RESOURCE_PATH_ID_PATH)
	public Response getBloodTypeById(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
		LOG.debug("try to retrieve specific blood type " + id);
		BloodType bloodType = service.getBloodTypeById(id);
		if (bloodType == null)
			return Response.status(Status.NOT_FOUND).build();
		return Response.ok(bloodType).build();
	}
	
	@POST
    @RolesAllowed({ADMIN_ROLE})
    public Response addBloodType(BloodType bloodTypeToAdd) {
        LOG.debug( "add a new blood type ...");
        BloodType bloodTypeAdded = service.persistBloodType(bloodTypeToAdd);
        return Response.ok(bloodTypeAdded).build();

    }
	
	@RolesAllowed( { ADMIN_ROLE, USER_ROLE })
    @PUT
    @Path( "/{id}")
    public Response updateBloodType(@PathParam( RESOURCE_PATH_ID_ELEMENT) int id, BloodType bloodTypeToUpdate) {
        LOG.debug( "update a specific blood type ...");
        BloodType bloodTypeUpdated = service.updateBloodType(id, bloodTypeToUpdate);
        return Response.ok(bloodTypeUpdated).build();
    }
	
	@DELETE
    @RolesAllowed({ADMIN_ROLE})
    @Path("/{id}")
    public Response deleteBloodType(@PathParam("id") int id){
		LOG.debug("delete a specific blood type ..."); 
        BloodType bloodType = service.getBloodTypeById(id);
        service.deleteBloodTypeById(id);
        return Response.ok(bloodType).build();
    }
}
