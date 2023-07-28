package gr.iag.dgtl.inventory.resource

import gr.iag.dgtl.inventory.ResourceSpecification
import gr.iag.dgtl.inventory.TestItemProvider
import gr.iag.dgtl.inventory.exception.InventoryException
import gr.iag.dgtl.inventory.service.IInventoryService
import groovy.json.JsonSlurper
import jakarta.ws.rs.client.Entity
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
        def response = jerseyPost(jsonReq)

        then: 'the response is OK'
        1 * service.addItem(_)
        response.status == Response.Status.OK.statusCode

        and: 'the claim response contains the claimId'
        def jsonResponse = new JsonSlurper().parseText(response.readEntity(String))
        jsonResponse.status == "SUCCESS"
    }

    def '500 response with a related message if a RuntimeException occurs'() {
        given: 'a valid create claim request'
        def jsonReq = jsonb.toJson(TestItemProvider.createItem())

        and: 'the error data'
        def errMsg = 'An exception happened'

        when: 'the API is called with valid input'
        def response = jerseyPost(jsonReq)

        then: 'the service is called and throws an exception'
        1 * service.addItem(_) >> { throw new InventoryException(errMsg) }

        and: 'the response is 500 with the error message we await'
        response.status == Response.Status.INTERNAL_SERVER_ERROR.statusCode
        def jsonResponse = new JsonSlurper().parseText(response.readEntity(String))
        jsonResponse.errors == [errMsg]
    }

    @Unroll
    def '400 response for a request when creating an Item Request with invalid parameters'() {
        given: 'a request with invalid data'
        def jsonReq = jsonb.toJson(invalidItemRequest)

        when: ' the API is called with invalid input'
        def response = jerseyPost(jsonReq)

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
        given: 'a valid item'
        def item = createItem()
        service.getItemBySerialNumber(item.serialNumber) >> Optional.of(item)

        when: 'trying to get the item'
        def response = jerseyGet(item.serialNumber)

        then: 'the response is OK and returns the item'
        response.status == Response.Status.OK.statusCode
        def jsonResponse = new JsonSlurper().parseText(response.readEntity(String))
        jsonResponse.name == item.name
        jsonResponse.serialNumber == item.serialNumber
        jsonResponse.value == item.value
    }

    def 'Successful delete item request'() {
        given: 'a valid item'
        def item = createItem()
        service.getItemBySerialNumber(item.serialNumber) >> Optional.of(item)

        when: 'trying to delete the item'
        def response = jerseyDelete(item.serialNumber)

        then: 'the response is No Content'
        1 * service.deleteItem(item.serialNumber)
        response.status == Response.Status.NO_CONTENT.statusCode
    }


    def 'Item not found'() {
        given: 'a non-existing serialNumber'
        String serialNumber = 'NONEXISTENT'
        service.getItemBySerialNumber(serialNumber) >> Optional.empty()

        when: 'trying to get the item'
        def response = jerseyGet(serialNumber)

        then: 'the item is not found'
        response.status == Response.Status.NOT_FOUND.statusCode
    }

    def 'Unsuccessful delete item request'() {
        given: 'a non-existing serialNumber'
        String serialNumber = 'NONEXISTENT'
        service.getItemBySerialNumber(serialNumber) >> Optional.empty()

        when: 'trying to delete the item'
        def response = jerseyDelete(serialNumber)

        then: 'the item is not found'
        response.status == Response.Status.NOT_FOUND.statusCode
    }

    private Response jerseyPost(String jsonReq) {
        jerseyTest
                .target(BASE_URL)
                .request()
                .post(Entity.json(jsonReq))
    }

    private Response jerseyGet(String serialNumber) {
        jerseyTest
                .target(BASE_URL + "/" + serialNumber)
                .request()
                .get()
    }

    private Response jerseyDelete(String serialNumber) {
        jerseyTest
                .target(BASE_URL + "/" + serialNumber)
                .request()
                .delete()
    }
}
