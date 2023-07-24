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

}
