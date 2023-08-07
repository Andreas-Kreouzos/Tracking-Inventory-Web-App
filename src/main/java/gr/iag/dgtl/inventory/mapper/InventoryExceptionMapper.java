package gr.iag.dgtl.inventory.mapper;

import gr.iag.dgtl.inventory.dto.ErrorResponse;
import gr.iag.dgtl.inventory.exception.InventoryException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.List;

/**
 * Provides a consistent way of handling InventoryException instances
 */
@Provider
public class InventoryExceptionMapper implements ExceptionMapper<InventoryException> {

    public final static String DEFAULT_MESSAGE =
            "An exception occurred while processing the request.";

    /**
     * Handles InventoryException by converting it into HTTP 500 Internal Server Error response
     * @param exception the exception to map to a response
     * @return a response of type INTERNAL SERVER ERROR along with the exception message
     */
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
