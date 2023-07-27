package gr.iag.dgtl.inventory

import gr.iag.dgtl.inventory.dto.Item
import jakarta.json.bind.Jsonb
import jakarta.json.bind.JsonbBuilder

class TestItemProvider {

    static Jsonb jsonb = JsonbBuilder.create()

    def static createItem() {
        new Item('Xbox One','AXB124AXY', 500 as BigDecimal)
    }

    def static createItemWithNullName() {
        new Item(null,'AXB124AXY', 500 as BigDecimal)
    }

    def static invalidItemName(){
        return ['name must not be blank']
    }

    def static jsonPhotoFlowMessage() {
        jsonb.toJson(createItem())
    }
}
