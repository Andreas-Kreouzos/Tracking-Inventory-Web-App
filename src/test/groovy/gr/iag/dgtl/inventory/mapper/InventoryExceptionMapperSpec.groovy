package gr.iag.dgtl.inventory.mapper

import gr.iag.dgtl.inventory.exception.InventoryException
import jakarta.ws.rs.core.Response
import spock.lang.Specification

class InventoryExceptionMapperSpec extends Specification {

    def 'Mapper transforms the exception with message correctly'(){
        given: 'the exception and the mapper'
        def msg = 'a message'
        def exception = new InventoryException(msg)
        def mapper = new InventoryExceptionMapper()

        when: 'the mapper catches the exception'
        def response = mapper.toResponse(exception)

        then: 'the response is correctly populated'
        response.status == Response.Status.INTERNAL_SERVER_ERROR.statusCode
        response.getEntity().errors.size() == 1
        response.getEntity().errors[0] == msg
    }

    def 'Mapper transforms an empty exception without issues'(){
        given: 'the exception and the mapper'
        def exception = new InventoryException()
        def mapper = new InventoryExceptionMapper()

        when: 'the mapper catches the exception'
        def response = mapper.toResponse(exception)

        then: 'the response is correctly populated'
        response.status == Response.Status.INTERNAL_SERVER_ERROR.statusCode
        response.getEntity().errors.size() > 0
    }
}