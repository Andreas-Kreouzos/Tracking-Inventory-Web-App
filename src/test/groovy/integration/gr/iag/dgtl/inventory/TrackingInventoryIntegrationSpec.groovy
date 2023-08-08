package integration.gr.iag.dgtl.inventory

import gr.iag.dgtl.inventory.PropertyReader
import gr.iag.dgtl.inventory.TestItemProvider
import gr.iag.dgtl.inventory.dto.ErrorResponse
import jakarta.json.bind.Jsonb
import jakarta.json.bind.JsonbBuilder
import jakarta.ws.rs.client.Client
import jakarta.ws.rs.client.ClientBuilder
import jakarta.ws.rs.client.Entity
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import spock.lang.Specification

class TrackingInventoryIntegrationSpec extends Specification {

    private static final String SERVICE_URL =
            "http://localhost:${PropertyReader.getProperty('app.port')}/tracking-inventory/inventory"

    Jsonb jsonb

    def setup() {
        jsonb = JsonbBuilder.create()
    }

    def 'Successful create item and persist it into JSON, HTML and CSV'() {
        given: 'setup the item'
        def requestBody = TestItemProvider.generateRandomItem()

        when: 'the call is succeeded'
        def response = doPost(jsonb.toJson(requestBody))

        then: 'empty response body means successful request'
        response.status == Response.Status.OK.statusCode
    }

    def 'Failed to create the item'() {
        given: 'an item with null name'
        def requestBody = TestItemProvider.createItemWithNullName()

        when: 'calling the application with this item'
        def response = doPost(jsonb.toJson(requestBody))

        then: 'a response with error message is returned'
        response.status != Response.Status.OK.statusCode
        response.readEntity(ErrorResponse.class).errors.size() > 0
    }

    def 'Successfully get an item'() {
        given: 'an item is already created'
        def requestBody = TestItemProvider.generateRandomItem()
        doPost(jsonb.toJson(requestBody))

        when: 'the call is made to the get api'
        def response = doGet(requestBody.serialNumber)

        then: 'the response contains an OK status'
        response.status == Response.Status.OK.statusCode

        and: 'the response body contains the correct information'
        def expectedItem = jsonb.toJson(requestBody)
        response.readEntity(String.class) == expectedItem
    }

    def 'Getting an Item that is not persisted'() {
        when: 'the call is made to the get api'
        def response = doGet('e3d60c63-95e2-4d3f-83ad-5daff215c718')

        then: 'the response contains an OK status'
        response.status == Response.Status.NOT_FOUND.statusCode

        and: 'the response body contains the correct information'
        response.readEntity(String.class) == '{"errors":["Item with serial number: e3d60c63-95e2-4d3f-83ad-5daff215c718 not found"]}'
    }

    def 'Successfully delete an item'() {
        given: 'an item is already created'
        def requestBody = TestItemProvider.generateRandomItem()
        doPost(jsonb.toJson(requestBody))

        when: 'the call is made to the delete api'
        def response = doDelete(requestBody.serialNumber)

        then: 'the response contains an OK status'
        response.status == Response.Status.NO_CONTENT.statusCode
    }

    def doPost(Object requestPayload) {

        Client client = ClientBuilder.newClient()
        client.target(SERVICE_URL)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(requestPayload))
    }

    def doGet(String serialNumber) {

        Client client = ClientBuilder.newClient()
        client.target(SERVICE_URL + '/' + serialNumber)
                .request(MediaType.APPLICATION_JSON)
                .get()
    }

    def doDelete(String serialNumber) {

        Client client = ClientBuilder.newClient()
        client.target(SERVICE_URL + '/' + serialNumber)
                .request(MediaType.APPLICATION_JSON)
                .delete()
    }
}
