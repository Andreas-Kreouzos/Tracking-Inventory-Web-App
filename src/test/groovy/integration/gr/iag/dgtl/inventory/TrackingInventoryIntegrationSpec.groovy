package integration.gr.iag.dgtl.inventory

import gr.iag.dgtl.inventory.TestItemProvider
import gr.iag.dgtl.inventory.dto.ErrorResponse
import jakarta.json.bind.Jsonb
import jakarta.json.bind.JsonbBuilder
import jakarta.ws.rs.client.Client
import jakarta.ws.rs.client.ClientBuilder
import jakarta.ws.rs.client.Entity
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.testcontainers.spock.Testcontainers
import spock.lang.Shared
import util.TestContainersSpec

import java.net.http.HttpClient

@Testcontainers
class TrackingInventoryIntegrationSpec extends TestContainersSpec {

    @Shared
    Jsonb jsonb

    @Shared
    def requestBody

    @Shared
    HttpClient client

    @Shared
    String appUrl

    def setupSpec() {
        openLiberty.start()
        appUrl = "http://${openLiberty.getHost()}:${openLiberty.getMappedPort(9080)}/inventory"

        client = HttpClient.newHttpClient()
        jsonb = JsonbBuilder.create()
        requestBody = TestItemProvider.generateRandomItem()
    }

    def cleanup() {
        doDelete(requestBody.serialNumber)
    }

    def 'Successful create item and persist it into JSON, HTML and CSV'() {
        when: 'the call is succeeded'
        def response = doPost(jsonb.toJson(requestBody))

        then: 'empty response body means successful request'
        response.status == Response.Status.CREATED.statusCode
    }

    def 'Failed to create the item'() {
        given: 'an item with null name'
        def requestBody = TestItemProvider.createItemWithNullName()

        when: 'calling the application with this item'
        def response = doPost(jsonb.toJson(requestBody))

        then: 'a response with error message is returned'
        response.status != Response.Status.CREATED.statusCode
        response.readEntity(ErrorResponse.class).errors.size() > 0
    }

    def 'Successfully get an item'() {
        given: 'an item is already created'
        doPost(jsonb.toJson(requestBody))

        when: 'the call is made to the get api'
        def response = doGet()

        then: 'the response contains an OK status'
        response.status == Response.Status.OK.statusCode

        and: 'the response body contains the correct information'
        def returnedItemMap = response.readEntity(List.class)[0]

        returnedItemMap.name == requestBody.name
        returnedItemMap.serialNumber == requestBody.serialNumber
        returnedItemMap.value == requestBody.value
    }

    def 'Successfully delete an item'() {
        given: 'an item is already created'
        doPost(jsonb.toJson(requestBody))

        when: 'the call is made to the delete api'
        def response = doDelete(requestBody.serialNumber)

        then: 'the response contains an OK status'
        response.status == Response.Status.NO_CONTENT.statusCode
    }

    def doPost(Object requestPayload) {
        Client client = ClientBuilder.newClient()
        return client.target(appUrl)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(requestPayload))
    }

    def doGet() {
        Client client = ClientBuilder.newClient()
        return client.target(appUrl)
                .request(MediaType.APPLICATION_JSON)
                .get()
    }

    def doDelete(String serialNumber) {
        Client client = ClientBuilder.newClient()
        client.target(appUrl + '/' + serialNumber)
                .request(MediaType.APPLICATION_JSON)
                .delete()
    }
}