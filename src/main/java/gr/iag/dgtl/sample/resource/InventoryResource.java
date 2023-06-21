package gr.iag.dgtl.sample.resource;

import gr.iag.dgtl.sample.dto.Item;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

@Path("/sample")
public class InventoryResource {

    @Operation(summary = "Sample API that returns the value of the sample property in a DTO")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success",
                content = {@Content(mediaType = "application/json",
                schema = @Schema(implementation = Item.class, type = SchemaType.ARRAY))})
    })
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getItems(){
        return null;
    }
}
