package com.github.behooked.resources;

import java.util.List;
import java.util.stream.Collectors;

import com.github.behooked.api.TriggerJSON;
import com.github.behooked.core.Trigger;
import com.github.behooked.db.TriggerDAO;

import io.dropwizard.hibernate.UnitOfWork;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("triggers")
@Produces(MediaType.APPLICATION_JSON)
public class TriggerResource {
	
	private final TriggerDAO triggerDAO;

	public TriggerResource(final TriggerDAO triggerDAO) {
		this.triggerDAO = triggerDAO;
	}

	@GET
	@UnitOfWork
	public List<TriggerJSON> listTriggers(){
		
		return triggerDAO.findAll().stream()
				.map(t -> TriggerJSON.from(t))
				.collect(Collectors.toList());
	}


	@GET
	@Path("{name}")
	@UnitOfWork
	public TriggerJSON getTriggerByName(@PathParam("name") final String name) { 
		
		final Trigger trigger = triggerDAO.findByName(name); 
		
		return TriggerJSON.from(trigger);  } 
	
	 @POST
	 @UnitOfWork
	    public TriggerJSON createTrigger( @Valid final TriggerJSON triggerJson)
	    {
		 
		 if ((triggerJson.getName() == null))
			{
				throw new ClientErrorException("Bad Request. The field 'name' must not be null", 400);
			}

	        final Trigger trigger = Trigger.convertToTrigger(triggerJson);
	        final Trigger createdTrigger = triggerDAO.create(trigger);
	        return TriggerJSON.from(createdTrigger);
	    }
	 
	 @DELETE
	 @Path("{id}")
	 @UnitOfWork
		public void deleteTriggerByID(@PathParam("id") final long id){
			final Trigger trigger= findSafely(id);
			triggerDAO.delete(trigger);
		}
	 
		private Trigger findSafely(final long triggerId) {
			return triggerDAO.findById(triggerId).orElseThrow(() -> new NotFoundException("No such Trigger")); }


}
