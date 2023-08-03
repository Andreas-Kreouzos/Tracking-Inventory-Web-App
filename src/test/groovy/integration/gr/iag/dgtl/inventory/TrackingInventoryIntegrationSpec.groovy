package integration.gr.iag.dgtl.inventory

import gr.iag.dgtl.inventory.PropertyReader
import gr.iag.dgtl.inventory.TestItemProvider
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
        def response = doPostCreate(jsonb.toJson(requestBody))

        then: 'empty response body means successful request'
        response.status == Response.Status.OK.statusCode
    }

    def doPostCreate(Object requestPayload) {

        Client client = ClientBuilder.newClient()
        client.target(SERVICE_URL)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(requestPayload))
    }
}
