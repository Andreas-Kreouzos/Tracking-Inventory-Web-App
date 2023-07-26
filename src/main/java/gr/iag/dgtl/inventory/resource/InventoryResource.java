package gr.iag.dgtl.inventory.resource;

import gr.iag.dgtl.inventory.dto.Item;
import gr.iag.dgtl.inventory.dto.ItemResponse;
import gr.iag.dgtl.inventory.service.IInventoryService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

import java.util.Optional;

/**
 * Contains the API for creating a request to create a Json file.
 */
@Path("/inventory")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class InventoryResource {

    private IInventoryService service;

    @Inject
    public InventoryResource(IInventoryService service) {
        this.service = service;
    }

    @POST
    public Response create(@RequestBody @Valid Item request) {
        service.addItem(request);
        ItemResponse itemResponse = new ItemResponse();
        itemResponse.setStatus("SUCCESS");
        itemResponse.setMessage("Item created successfully");
        itemResponse.setItemId(request.getSerialNumber());
        return Response.ok(itemResponse).build();
    }

    @GET
    @Path("/{serialNumber}")
    public Response getItemBySerialNumber(@PathParam("serialNumber") String serialNumber) {
        Optional<Item> item = service.getItemBySerialNumber(serialNumber);
        if (item.isPresent()) {
            return Response.ok(item.get()).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @Path("{serialNumber}")
    @DELETE
    public Response delete(@PathParam("serialNumber") String serialNumber) {
        Optional<Item> item = service.getItemBySerialNumber(serialNumber);
        if (item.isPresent()) {
            service.deleteItem(serialNumber);
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
