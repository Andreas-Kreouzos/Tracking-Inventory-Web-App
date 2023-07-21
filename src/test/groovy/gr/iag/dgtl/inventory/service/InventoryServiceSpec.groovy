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

    def cleanup() {
        File file = new File('inventory.json')
        if (file.exists()) {
            file.delete()
        }
    }

    def 'Successfully insert an item into list'() {
        given: 'an item'
        def item = new Item('Xbox One','AXB124AXY', 500 as BigDecimal)

        and: 'persistence returns empty list initially and then adds the item'
        itemPersistence.loadItems() >> []
        itemPersistence.saveItems(_) >> { args -> args[0] }

        when: 'trying to add the item in the list'
        service.addItem(item)

        then: 'the item is indeed being added'
        def items = service.getItems()
        items.size() == 1
        items.get(0).name == item.name
        items.get(0).serialNumber == item.serialNumber
        items.get(0).value == item.value
    }

    def 'Successfully delete an item from the list'() {
        given: 'an item'
        def item = new Item('Xbox One','AXB124AXY', 500 as BigDecimal)

        and: 'persistence initially returns list with item and then removes the item'
        itemPersistence.loadItems() >> [item]
        itemPersistence.saveItems(_) >> { args -> args[0] }

        when: 'trying to delete the item from the list'
        service.deleteItem(item.serialNumber)

        then: 'the item is indeed being removed'
        service.getItems().isEmpty()
    }
}
