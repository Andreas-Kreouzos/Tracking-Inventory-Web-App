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
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

/**
 * Contains the API for creating a request to create a Json file.
 */
@OpenAPIDefinition(
        info = @Info(
                title = "Inventory API",
                version = "1.0.0",
                description = "API for managing inventory items"
        )
)
@Tag(name = "inventory", description = "Inventory operations")
@Path("/inventory")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class InventoryResource {

    private IInventoryService service;

    @Inject
    public InventoryResource(IInventoryService service) {
        this.service = service;
    }

    @APIResponse(
            responseCode = "201",
            description = "Item successfully created",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ItemResponse.class)
            )
    )
    @POST
    public Response create(@RequestBody @Valid Item request) {
        service.addItem(request);
        ItemResponse itemResponse = new ItemResponse();
        itemResponse.setStatus("SUCCESS");
        itemResponse.setMessage("Item created successfully");
        itemResponse.setItemId(request.getSerialNumber());
        return Response.status(Response.Status.CREATED).entity(itemResponse).build();
    }

    @APIResponse(
            responseCode = "200",
            description = "List of items",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(type = SchemaType.ARRAY, implementation = Item.class)
            )
    )
    @GET
    public Response getItemBySerialNumber() {
        List<Item> items = service.getItems();
        return Response.ok(items).build();
    }

    @APIResponse(
            responseCode = "204",
            description = "Item successfully deleted"
    )
    @DELETE
    @Path("{serialNumber}")
    public Response delete(
            @PathParam("serialNumber") String serialNumber) {
        service.deleteItem(serialNumber);
        return Response.noContent().build();
    }
}