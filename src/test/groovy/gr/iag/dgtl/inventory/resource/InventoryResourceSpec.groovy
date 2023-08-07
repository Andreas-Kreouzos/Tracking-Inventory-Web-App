package gr.iag.dgtl.inventory.resource

import gr.iag.dgtl.inventory.ResourceSpecification
import gr.iag.dgtl.inventory.TestItemProvider
import gr.iag.dgtl.inventory.dto.ErrorResponse
import gr.iag.dgtl.inventory.dto.Item
import gr.iag.dgtl.inventory.exception.InventoryException
import gr.iag.dgtl.inventory.service.IInventoryService
import groovy.json.JsonSlurper
import jakarta.ws.rs.core.Response
import spock.lang.Shared
import spock.lang.Unroll

class InventoryResourceSpec extends ResourceSpecification {

    static final String BASE_URL = '/inventory'

    @Shared
    IInventoryService service

    @Override
    protected createResource() {
        service = Mock()
        new InventoryResource(service)
    }

    def 'Successful item creation request'() {
        given: 'a valid item request'
        def jsonReq = jsonb.toJson(TestItemProvider.createItem())

        when: 'calling the create method of the resource'
        def response = jerseyPost(jsonReq, BASE_URL)

        then: 'the response is OK'
        1 * service.addItem(_)
        response.status == Response.Status.OK.statusCode

        and: 'the response contains the item'
        def jsonResponse = new JsonSlurper().parseText(response.readEntity(String))
        jsonResponse.status == "SUCCESS"
    }

    def 'The POST service does not interfere with thrown exceptions'() {
        given: 'a valid item'
        def jsonReq = jsonb.toJson(TestItemProvider.createItem())

        and: 'some error parameters'
        def cause = new Exception()
        def errorMsg = 'An exception happened'

        when: 'the POST service handles the request'
        def response = jerseyPost(jsonReq, BASE_URL)

        then: 'the service is called and throws an exception'
        1 * service.addItem(_) >> {
            throw new InventoryException(errorMsg, cause)
        }

        and: 'a 500 response is received'
        response.status == Response.Status.INTERNAL_SERVER_ERROR.statusCode

        and: 'the error response contains the expected message'
        def errorResponse = response.readEntity(ErrorResponse.class)
        errorResponse.errors.size() == 1
        errorResponse.errors[0] == errorMsg
    }

    @Unroll
    def '400 response for a request when creating an Item with invalid parameters'() {
        given: 'a request with invalid data'
        def jsonReq = jsonb.toJson(invalidItemRequest)

        when: ' the API is called with invalid input'
        def response = jerseyPost(jsonReq, BASE_URL)

        then: 'the service is not called'
        0 * _

        and: ' the response is 400 and contains a message for the invalid fields'
        response.status == Response.Status.BAD_REQUEST.statusCode
        def jsonResponse = new JsonSlurper().parseText(response.readEntity(String))
        jsonResponse.errors == errors

        where:
        invalidItemRequest                                              || errors
        TestItemProvider.createItemWithNullName()                       || TestItemProvider.invalidItemName()
        TestItemProvider.createItemWithEmptyName()                      || TestItemProvider.invalidItemName()
        TestItemProvider.createItemWithNullSerialNumber()               || TestItemProvider.invalidItemSerialNumber()
        TestItemProvider.createItemWithEmptySerialNumber()              || TestItemProvider.invalidItemSerialNumber()
        TestItemProvider.createItemWithNullValue()                      || TestItemProvider.invalidItemValue()
        TestItemProvider.createItemWithLessThanMinValue()               || TestItemProvider.invalidItemMinValue()
    }

    def 'Successful get item request'() {
        given: 'a valid item request'
        def item = TestItemProvider.createItem()

        and: 'mocking the service get method with this item'
        service.getItemBySerialNumber(item.serialNumber) >> item

        when: 'calling the get method of the resource'
        def response = jerseyGet(item.serialNumber, BASE_URL)

        then: 'the response is OK and returns the item'
        response.status == Response.Status.OK.statusCode

        and: 'the response contains the item'
        def jsonResponse = response.readEntity(Item.class)
        jsonResponse.name == item.name
        jsonResponse.serialNumber == item.serialNumber
        jsonResponse.value == item.value
    }

    def 'The GET service does not interfere with thrown exceptions'() {
        given: 'a valid item'
        def item = TestItemProvider.createItem()

        and: 'some error parameters'
        def cause = new Exception()
        def errorMsg = 'An exception happened'

        and: 'the service is called and throws an exception'
        1 * service.getItemBySerialNumber(item.serialNumber) >> {
            throw new InventoryException(errorMsg, cause)
        }

        when: 'the GET service handles a request'
        def response = jerseyGet(item.serialNumber, BASE_URL)

        then: 'a 500 response is received'
        response.status == Response.Status.INTERNAL_SERVER_ERROR.statusCode

        and: 'the error response contains the expected message'
        def errorResponse = response.readEntity(ErrorResponse.class)
        errorResponse.errors.size() == 1
        errorResponse.errors[0] == errorMsg
    }

    def 'Successful delete item request'() {
        given: 'a valid item'
        def item = TestItemProvider.createItem()

        and: 'mocking the service get method with this item'
        service.getItemBySerialNumber(item.serialNumber) >> item

        when: 'calling the delete method of the resource'
        def response = jerseyDelete(item.serialNumber, BASE_URL)

        then: 'the response is No Content'
        1 * service.deleteItem(item.serialNumber)
        response.status == Response.Status.NO_CONTENT.statusCode
    }

    def 'The DELETE service does not interfere with thrown exceptions'() {
        given: 'a valid item'
        def item = TestItemProvider.createItem()

        and: 'some error parameters'
        def cause = new Exception()
        def errorMsg = 'An exception happened'

        and: 'the service is called and throws an exception'
        1 * service.deleteItem(item.serialNumber) >> {
            throw new InventoryException(errorMsg, cause)
        }

        when: 'the DELETE service handles a request'
        def response = jerseyDelete(item.serialNumber, BASE_URL)

        then: 'a 500 response is received'
        response.status == Response.Status.INTERNAL_SERVER_ERROR.statusCode

        and: 'the error response contains the expected message'
        def errorResponse = response.readEntity(ErrorResponse.class)
        errorResponse.errors.size() == 1
        errorResponse.errors[0] == errorMsg
    }
}
