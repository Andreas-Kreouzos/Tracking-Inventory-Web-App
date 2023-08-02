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

    def 'successful create item'() {
        given: 'setup the item'
        def requestBody = TestItemProvider.createItem()

        when: 'the call is succeeded'
        def response = doPostCreate(jsonb.toJson(requestBody))

        then: 'empty response body means successful request'
        if (response.status != Response.Status.OK.statusCode) {
            println response.readEntity(String.class)  // Print the response body
        }
        response.status == Response.Status.OK.statusCode

        cleanup:
        File htmlfile = new File('C:/Users/kreouzosa/Desktop/file.html')
        htmlfile.delete()
        File jsonfile = new File('C:/Users/kreouzosa/Desktop/file.json')
        jsonfile.delete()
        File csvfile = new File('C:/Users/kreouzosa/Desktop/file.csv')
        csvfile.delete()
    }

    def doPostCreate(Object requestPayload) {

        Client client = ClientBuilder.newClient()
        client.target(SERVICE_URL)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(requestPayload))
    }
}
