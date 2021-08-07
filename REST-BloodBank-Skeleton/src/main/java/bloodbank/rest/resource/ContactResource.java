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
import bloodbank.entity.Contact;

@Path("contact")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ContactResource {

	private static final Logger LOG = LogManager.getLogger();
	
	@EJB
	protected BloodBankService service;

	@Inject
	protected SecurityContext sc;
	
	@GET
    @RolesAllowed({ADMIN_ROLE})
	public Response getPhones() {
		LOG.debug("retrieving all contacts ...");
		List<Contact> contacts = service.getAllContacts();
		Response response = Response.ok(contacts).build();
		return response;
	}
	
	@GET
	@RolesAllowed({ADMIN_ROLE, USER_ROLE})
	@Path(RESOURCE_PATH_ID_PATH)
	public Response getContactById(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
		LOG.debug("try to retrieve specific contact " + id);
		Contact contact = service.getContactById(id);
		if (contact == null)
			return Response.status(Status.NOT_FOUND).build();
		return Response.ok(contact).build();
	}
	
	@POST
    @RolesAllowed({ADMIN_ROLE})
    public Response addContact(Contact contactToAdd) {
        LOG.debug( "add a new contact ...");
        Contact contactAdded = service.persistContact(contactToAdd);
        return Response.ok(contactAdded).build();

    }
	
	@RolesAllowed( { ADMIN_ROLE, USER_ROLE })
    @PUT
    @Path( "/{id}")
    public Response updateContact(@PathParam( RESOURCE_PATH_ID_ELEMENT) int id, Contact contactToUpdate) {
        LOG.debug( "update a specific contact ...");
        Contact contactUpdated = service.updateContact(id, contactToUpdate);
        return Response.ok(contactUpdated).build();
    }
	
	@DELETE
    @RolesAllowed({ADMIN_ROLE})
    @Path("/{id}")
    public Response deleteContact(@PathParam("id") int id){
		LOG.debug("delete a specific contact ..."); 
        Contact contact = service.getContactById(id);
        service.deleteContactById(id);
        return Response.ok(contact).build();
    }
}
