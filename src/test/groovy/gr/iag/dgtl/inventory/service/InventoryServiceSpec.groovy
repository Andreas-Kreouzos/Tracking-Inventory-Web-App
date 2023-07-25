package gr.iag.dgtl.inventory.service

import gr.iag.dgtl.inventory.dto.Item
import gr.iag.dgtl.inventory.persistance.IItemPersistence
import spock.lang.Specification
import spock.lang.Subject

class InventoryServiceSpec extends Specification {

    @Subject
    InventoryService service
    IItemPersistence itemPersistence

    def setup() {
        itemPersistence = Mock(IItemPersistence)
        service = new InventoryService(itemPersistence)
    }

    def 'Adding an Item, saves it with correct fields'() {
        given: 'an item'
        def item = new Item('Xbox One','AXB124AXY', 500 as BigDecimal)

        when: 'calling the method to add the item in a list'
        service.addItem(item)

        then: 'it gets saved in this list'
        1 * itemPersistence.saveItems([item])
    }

    def 'Successfully deleting an Item'() {
        given: 'an item'
        def item = new Item('Xbox One','AXB124AXY', 500 as BigDecimal)

        and: 'the item is in the inventory'
        itemPersistence.loadItems() >> [item]

        when: 'the item is added'
        service.addItem(item)

        and: 'calling the method to delete the item from the list'
        service.deleteItem(item.serialNumber)

        then: 'an empty list gets saved'
        1 * itemPersistence.saveItems([])
    }

    def 'getItemBySerialNumber correctly fetches an item'() {
        given: 'an item'
        def item = new Item('Xbox One','AXB124AXY', 500 as BigDecimal)

        and: 'the item is in the inventory'
        itemPersistence.loadItems() >> [item]
        service = new InventoryService(itemPersistence)

        expect: 'getItemBySerialNumber returns the correct item'
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

    def 'InventoryService properly handles exception thrown by IItemPersistence'() {
        given: 'an item'
        def item = new Item('Xbox One','AXB124AXY', 500 as BigDecimal)

        and: 'IItemPersistence throws an exception when trying to save items'
        itemPersistence.saveItems(_) >> { throw new RuntimeException('Test exception') }

        when: 'addItem is called'
        service.addItem(item)

        then: 'a RuntimeException is thrown'
        thrown(RuntimeException)
    }
}
