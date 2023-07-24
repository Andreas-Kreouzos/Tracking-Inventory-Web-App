package gr.iag.dgtl.inventory.resource;

import gr.iag.dgtl.inventory.dto.Item;
import gr.iag.dgtl.inventory.dto.ItemResponse;
import gr.iag.dgtl.inventory.service.IInventoryService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

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

    @Operation(summary = "Sample API that returns the value of the inventory property in a DTO")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success",
                content = {@Content(mediaType = "application/json",
                schema = @Schema(implementation = Item.class, type = SchemaType.ARRAY))})
    })
    @GET
    public Response getItems(){
        return null;
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
}
