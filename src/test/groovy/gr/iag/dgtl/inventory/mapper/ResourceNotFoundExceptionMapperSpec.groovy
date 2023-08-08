package gr.iag.dgtl.inventory.mapper

import gr.iag.dgtl.inventory.exception.ResourceNotFoundException
import jakarta.ws.rs.core.Response
import spock.lang.Specification

class ResourceNotFoundExceptionMapperSpec extends Specification {

    def 'Mapper transforms the exception with message correctly'(){
        given: 'the exception and the mapper'
        def msg = 'a message'
        def exception = new ResourceNotFoundException(msg)
        def mapper = new ResourceNotFoundExceptionMapper()

        when: 'the mapper catches the exception'
        def response = mapper.toResponse(exception)

        then: 'the response is correctly populated'
        response.status == Response.Status.NOT_FOUND.statusCode
        response.getEntity().errors.size() == 1
        response.getEntity().errors[0] == msg
    }
}
