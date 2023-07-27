package gr.iag.dgtl.inventory

import gr.iag.dgtl.inventory.mapper.ConstraintViolationExceptionMapper
import gr.iag.dgtl.inventory.mapper.InventoryExceptionMapper
import jakarta.json.bind.Jsonb
import jakarta.json.bind.JsonbBuilder
import jakarta.ws.rs.core.Application
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
}

