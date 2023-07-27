package gr.iag.dgtl.inventory.mapper;

import gr.iag.dgtl.inventory.dto.ErrorResponse;
import gr.iag.dgtl.inventory.exception.InventoryException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.List;

@Provider
public class InventoryExceptionMapper implements ExceptionMapper<InventoryException> {

    public final static String DEFAULT_MESSAGE =
            "An exception occurred while processing the request.";

    @Override
    public Response toResponse(InventoryException exception) {
        ErrorResponse errResponse;
        if(exception.getMessage() != null){
            errResponse = new ErrorResponse(List.of(exception.getMessage()));
        } else {
            errResponse = new ErrorResponse(List.of(DEFAULT_MESSAGE));
        }
        return Response.serverError().entity(errResponse).build();
    }
}
