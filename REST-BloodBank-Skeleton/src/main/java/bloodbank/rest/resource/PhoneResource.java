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
import bloodbank.entity.Phone;

@Path("phone")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PhoneResource {

	private static final Logger LOG = LogManager.getLogger();
	
	@EJB
	protected BloodBankService service;

	@Inject
	protected SecurityContext sc;
	
	@GET
    @RolesAllowed({ADMIN_ROLE})
	public Response getPhones() {
		LOG.debug("retrieving all phones ...");
		List<Phone> phones = service.getAllPhones();
		Response response = Response.ok(phones).build();
		return response;
	}
	
	@GET
	@RolesAllowed({ADMIN_ROLE, USER_ROLE})
	@Path(RESOURCE_PATH_ID_PATH)
	public Response getPhoneById(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
		LOG.debug("try to retrieve specific phone " + id);
		Phone phone = service.getPhoneById(id);
		if (phone == null)
			return Response.status(Status.NOT_FOUND).build();
		return Response.ok(phone).build();
	}
	
	@POST
    @RolesAllowed({ADMIN_ROLE})
    public Response addPhone(Phone phoneToAdd) {
        LOG.debug( "add a new phone ...");
        Phone phoneAdded = service.persistPhone(phoneToAdd);
        return Response.ok(phoneAdded).build();

    }
	
	@RolesAllowed( { ADMIN_ROLE, USER_ROLE })
    @PUT
    @Path( "/{id}")
    public Response updatePhone(@PathParam( RESOURCE_PATH_ID_ELEMENT) int id, Phone phoneToUpdate) {
        LOG.debug( "update a specific phone ...");
        Phone phoneUpdated = service.updatePhone(id, phoneToUpdate);
        return Response.ok(phoneUpdated).build();
    }
	
	@DELETE
    @RolesAllowed({ADMIN_ROLE})
    @Path("/{id}")
    public Response deletePhone(@PathParam("id") int id){
		LOG.debug("delete a specific phone ..."); 
        Phone phone = service.getPhoneById(id);
        service.deletePhoneById(id);
        return Response.ok(phone).build();
    }
}
