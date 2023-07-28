package gr.iag.dgtl.inventory

import gr.iag.dgtl.inventory.dto.Item

class TestItemProvider {

    def static createItem() {
        new Item('Xbox One','AXB124AXY', 500 as BigDecimal)
    }

    def static createItemWithNullName() {
        new Item(null,'AXB124AXY', 500 as BigDecimal)
    }

    def static createItemWithEmptyName() {
        new Item('','AXB124AXY', 500 as BigDecimal)
    }

    def static createItemWithNullSerialNumber() {
        new Item('Xbox One',null, 500 as BigDecimal)
    }

    def static createItemWithEmptySerialNumber() {
        new Item('Xbox One','', 500 as BigDecimal)
    }

    def static createItemWithNullValue() {
        new Item('Xbox One','AXB124AXY', null)
    }

    def static createItemWithLessThanMinValue() {
        new Item('Xbox One','AXB124AXY', new BigDecimal('0.05'))
    }

    def static invalidItemName(){
        return ['name must not be blank']
    }

    def static invalidItemSerialNumber(){
        return ['serialNumber must not be blank']
    }

    def static invalidItemValue(){
        return ['value must not be null']
    }

    def static invalidItemMinValue(){
        return ['value must be greater than 0.1']
    }
}
