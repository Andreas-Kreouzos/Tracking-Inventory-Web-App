package gr.iag.dgtl.inventory.service

import gr.iag.dgtl.inventory.TestItemProvider
import gr.iag.dgtl.inventory.dto.Item
import gr.iag.dgtl.inventory.exception.InventoryException
import gr.iag.dgtl.inventory.mapper.InventoryExceptionMapper
import gr.iag.dgtl.inventory.persistance.IItemPersistence
import spock.lang.Specification
import spock.lang.Subject

class InventoryServiceSpec extends Specification {

    @Subject
    InventoryService service
    IItemPersistence itemPersistence

    def item = TestItemProvider.createItem()

    def setup() {
        itemPersistence = Mock(IItemPersistence)
        service = new InventoryService(itemPersistence)
    }

    def 'Successfully adding an item with correct fields'() {
        when: 'calling the method to add the item in a list'
        service.addItem(item)

        then: 'it gets saved in this list'
        1 * itemPersistence.saveItems([item])
    }

    def 'Persistence throws exception when trying to save an item'() {
        given: 'some error parameters'
        def errorMsg = 'An exception happened'
        def cause = new RuntimeException()

        when: 'calling the method to add the item in a list'
        service.addItem(item)

        then: 'the persistence will throw an exception'
        1 * itemPersistence.saveItems(_) >> {
            throw new InventoryException(errorMsg, cause)
        }

        and: 'check the exception'
        def exception = thrown(InventoryException)
        exception.cause.cause == cause
        exception.message == InventoryExceptionMapper.DEFAULT_MESSAGE
    }

    def 'Successfully deleting an item'() {
        given: 'the item is present in the inventory'
        itemPersistence.loadItems() >> [item]

        when: 'the item is added'
        service.addItem(item)

        and: 'calling the method to delete the item from the list'
        service.deleteItem(item.serialNumber)

        then: 'an empty list gets saved'
        1 * itemPersistence.saveItems([])
    }

    def 'Delete item that does not exist throws exception'() {
        given: 'an empty inventory'
        itemPersistence.loadItems() >> []

        and: 'a serial number that does not exist in the inventory'
        String serialNumber = 'non-existent serial number'

        when: 'trying to delete the item with the non-existent serial number'
        service.deleteItem(serialNumber)

        then: 'an exception is thrown'
        def exception = thrown(InventoryException)
        exception.message == "An exception occurred while processing the request."
    }


    def 'Getting an item by serial number, correctly fetches it'() {
        when: 'the item is in the inventory'
        itemPersistence.loadItems() >> [item]
        service = new InventoryService(itemPersistence)

        then: 'the service returns the correct item'
        service.getItemBySerialNumber(item.serialNumber).get() == item
    }

    def 'getItemBySerialNumber returns empty if item is not in inventory'() {
        given: 'an item serial number'
        String serialNumber = 'non-existent serial number'

        and: 'the inventory is empty'
        itemPersistence.loadItems() >> []

        expect: 'getItemBySerialNumber returns empty'
        !service.getItemBySerialNumber(serialNumber).isPresent()
    }

    def 'getItems returns all items in inventory'() {
        given: 'several items in the inventory'
        def item1 = new Item('Xbox One','AXB124AXY', 500 as BigDecimal)
        def item2 = new Item('Playstation 5','PS1234XYZ', 499 as BigDecimal)
        itemPersistence.loadItems() >> [item1, item2]
        service = new InventoryService(itemPersistence)

        expect: 'getItems returns all items'
        service.getItems() == [item1, item2]
    }

    def 'getItems returns an empty list if the inventory is empty'() {
        given: 'an empty inventory'
        itemPersistence.loadItems() >> []

        expect: 'getItems returns an empty list'
        service.getItems().isEmpty()
    }
}
