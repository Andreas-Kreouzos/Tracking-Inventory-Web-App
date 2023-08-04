package gr.iag.dgtl.inventory.service

import gr.iag.dgtl.inventory.TestItemProvider
import gr.iag.dgtl.inventory.dto.Item
import gr.iag.dgtl.inventory.exception.InventoryException
import gr.iag.dgtl.inventory.mapper.InventoryExceptionMapper
import gr.iag.dgtl.inventory.persistence.IItemPersistence
import spock.lang.Specification
import spock.lang.Subject

class InventoryServiceSpec extends Specification {

    @Subject
    InventoryService service
    IItemPersistence htmlPersistence;
    IItemPersistence jsonPersistence;
    IItemPersistence csvPersistence;

    def item = TestItemProvider.createItem()

    def setup() {
        htmlPersistence = Mock(IItemPersistence)
        jsonPersistence = Mock(IItemPersistence)
        csvPersistence = Mock(IItemPersistence)
        service = new InventoryService(htmlPersistence, jsonPersistence, csvPersistence)
    }

    def 'Successfully adding an item with correct fields'() {
        when: 'calling the method to add the item in a list'
        service.addItem(item)

        then: 'it gets saved in this list'
        1 * htmlPersistence.saveItems([item])
        1 * jsonPersistence.saveItems([item])
        1 * csvPersistence.saveItems([item])
    }

    def 'Persistence throws exception when trying to save an item'() {
        given: 'some error parameters'
        def errorMsg = 'An exception happened'
        def cause = new RuntimeException()

        when: 'calling the method to add the item in a list'
        service.addItem(item)

        then: 'the persistence will throw an exception'
        1 * htmlPersistence.saveItems(_) >> {
            throw new InventoryException(errorMsg, cause)
        }
        0 * jsonPersistence.saveItems(_)
        0 * csvPersistence.saveItems(_)

        and: 'check the exception'
        def exception = thrown(InventoryException)
        exception.cause.cause == cause
        exception.message == InventoryExceptionMapper.DEFAULT_MESSAGE
    }

    def 'Successfully deleting an item'() {
        given: 'the item is present in the inventory'
        htmlPersistence.loadItems() >> [item]
        jsonPersistence.loadItems() >> [item]
        csvPersistence.loadItems() >> [item]

        when: 'the item is added'
        service.addItem(item)

        and: 'calling the method to delete the item from the list'
        service.deleteItem(item.serialNumber)

        then: 'an empty list gets saved'
        1 * htmlPersistence.saveItems([])
        1 * jsonPersistence.saveItems([])
        1 * csvPersistence.saveItems([])
    }

    def 'Delete item that does not exist throws exception'() {
        given: 'an empty inventory'
        htmlPersistence.loadItems() >> []
        jsonPersistence.loadItems() >> []
        csvPersistence.loadItems() >> []

        and: 'a serial number that does not exist in the inventory'
        String serialNumber = 'non-existent serial number'

        when: 'trying to delete the item with the non-existent serial number'
        service.deleteItem(serialNumber)

        then: 'an exception is thrown'
        def exception = thrown(InventoryException)
        exception.message == InventoryExceptionMapper.DEFAULT_MESSAGE
    }

    def 'Getting an item by serial number, correctly fetches it'() {
        when: 'the item is in the inventory'
        service.addItem(item)

        then: 'the service returns the correct item'
        service.getItemBySerialNumber(item.serialNumber).get() == item
    }

    def 'Empty optional returned if item is not in the inventory'() {
        given: 'an item serial number that does not exist'
        String serialNumber = 'non-existent serial number'

        expect: 'an empty optional'
        !service.getItemBySerialNumber(serialNumber).isPresent()
    }

    def 'getItems returns all items in inventory'() {
        given: 'several items in the inventory'
        def item2 = new Item('Playstation 5','PS1234XYZ', 499 as BigDecimal)
        service.addItem(item)
        service.addItem(item2)

        expect: 'getItems returns all items'
        service.getItems() == [item, item2]
    }

    def 'getItems returns an empty list if the inventory is empty'() {
        expect: 'getItems returns an empty list'
        service.getItems().isEmpty()
    }
}
