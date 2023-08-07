package gr.iag.dgtl.inventory.mapper;

import gr.iag.dgtl.inventory.dto.ErrorResponse;
import gr.iag.dgtl.inventory.exception.ResourceNotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.List;

/**
 * Provides a consistent way of handling ResourceNotFoundException instances
 */
@Provider
public class ResourceNotFoundExceptionMapper implements ExceptionMapper<ResourceNotFoundException> {

    public final static String DEFAULT_MESSAGE =
            "The requested resource could not be found!";

    /**
     * Handles ResourceNotFoundException by converting it into HTTP 404 Not Found response
     * @param exception the exception to map to a response
     * @return a response of type NOT FOUND along with the exception message
     */
    @Override
    public Response toResponse(ResourceNotFoundException exception) {
        ErrorResponse errResponse;
        if(exception.getMessage() != null){
            errResponse = new ErrorResponse(List.of(exception.getMessage()));
        } else {
            errResponse = new ErrorResponse(List.of(DEFAULT_MESSAGE));
        }
        return Response.status(Response.Status.NOT_FOUND).entity(errResponse).build();
    }
}
