package gr.iag.dgtl.inventory.service

import gr.iag.dgtl.inventory.dto.Item
import spock.lang.Specification
import spock.lang.Subject

class InventoryServiceSpec extends Specification {

    @Subject
    InventoryService service

    def setup() {
        service = new InventoryService()
    }

    def 'Successfully insert an Item into list'() {
        given: 'an item'
        def item = new Item('Xbox One','AXB124AXY', 500 as BigDecimal)

        when: 'calling the service'
        service.addItem(item)

        then: ''
        def items = service.getItems()
        items.size() == 1
        items.get(0).name() == item.name()
        items.get(0).serialNumber() == item.serialNumber()
        items.get(0).value() == item.value()
    }
}
