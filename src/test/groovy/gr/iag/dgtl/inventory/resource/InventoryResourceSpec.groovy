package gr.iag.dgtl.inventory.resource

import gr.iag.dgtl.inventory.ResourceSpecification
import gr.iag.dgtl.inventory.dto.Item
import gr.iag.dgtl.inventory.service.IInventoryService
import groovy.json.JsonSlurper
import jakarta.ws.rs.client.Entity
import jakarta.ws.rs.core.Response
import spock.lang.Shared

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
        def jsonReq = jsonb.toJson(createItem())

        when: 'calling the create method of the resource'
        def response = jerseyPost(jsonReq)

        then: 'the response is OK'
        1 * service.addItem(_)
        response.status == Response.Status.OK.statusCode

        and: 'the claim response contains the claimId'
        def jsonResponse = new JsonSlurper().parseText(response.readEntity(String))
        jsonResponse.status == "SUCCESS"
    }

    def createItem() {
        new Item('Xbox One','AXB124AXY', 500 as BigDecimal)
    }

    private Response jerseyPost(String jsonReq) {
        jerseyTest
                .target(BASE_URL)
                .request()
                .post(Entity.json(jsonReq))
    }
}
