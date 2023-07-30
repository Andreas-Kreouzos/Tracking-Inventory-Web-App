package gr.iag.dgtl.inventory

import gr.iag.dgtl.inventory.mapper.ConstraintViolationExceptionMapper
import gr.iag.dgtl.inventory.mapper.InventoryExceptionMapper
import jakarta.json.bind.Jsonb
import jakarta.json.bind.JsonbBuilder
import jakarta.ws.rs.client.Entity
import jakarta.ws.rs.core.Application
import jakarta.ws.rs.core.Response
import org.glassfish.jersey.server.ResourceConfig
import org.glassfish.jersey.test.JerseyTest
import spock.lang.Specification

abstract class ResourceSpecification extends Specification {

    JerseyTest jerseyTest
    Jsonb jsonb

    protected abstract createResource();

    def setup() {
        jerseyTest = new JerseyTest() {
            @Override
            protected Application configure() {
                new ResourceConfig()
                        .register(createResource())
                        .register(ConstraintViolationExceptionMapper)
                        .register(InventoryExceptionMapper)
            }
        }
        jerseyTest.setUp()

        jsonb = JsonbBuilder.create()
    }

    def cleanup() {
        jerseyTest.tearDown()
    }

    protected Response jerseyPost(String jsonReq, String url) {
        jerseyTest
                .target(url)
                .request()
                .post(Entity.json(jsonReq))
    }

    protected Response jerseyGet(String serialNumber, String url) {
        jerseyTest
                .target(url + "/" + serialNumber)
                .request()
                .get()
    }

    protected Response jerseyDelete(String serialNumber, String url) {
        jerseyTest
                .target(url + "/" + serialNumber)
                .request()
                .delete()
    }
}
