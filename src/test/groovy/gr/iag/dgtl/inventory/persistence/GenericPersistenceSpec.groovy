package gr.iag.dgtl.inventory.persistence

import gr.iag.dgtl.inventory.TestItemProvider
import gr.iag.dgtl.inventory.dto.Item
import gr.iag.dgtl.inventory.exception.ResourceNotFoundException
import spock.lang.Specification
import spock.lang.Subject

class GenericPersistenceSpec extends Specification {

    @Subject
    GenericPersistence genericPersistence
    Item expectedItem

    def setup() {
        expectedItem = TestItemProvider.generateRandomItem()
        genericPersistence = new GenericPersistence() {
            @Override
            protected List<Item> loadItems() {
                [expectedItem]
            }
        }
    }

    def "should return item by serial number"() {
        given: "An item list with an existing item"
        def expectedItem = genericPersistence.loadItems()[0]

        when: "Getting item by serial number"
        def returnedItem = genericPersistence.getItemBySerialNumber(expectedItem.serialNumber)

        then: "The item is returned"
        returnedItem.serialNumber == expectedItem.serialNumber
    }

    def "should throw exception when item is not found"() {
        when: "Getting a non existent serial number"
        genericPersistence.getItemBySerialNumber("non-existent-serial-number")

        then: "an exception is thrown"
        thrown(ResourceNotFoundException)
    }
}
