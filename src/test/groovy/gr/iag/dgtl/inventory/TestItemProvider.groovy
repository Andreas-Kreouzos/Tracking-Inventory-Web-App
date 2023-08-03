package gr.iag.dgtl.inventory

import gr.iag.dgtl.inventory.dto.Item

import java.math.RoundingMode

class TestItemProvider {

    private static final Random RANDOM = new Random()

    def static generateRandomItem() {
        def name = UUID.randomUUID().toString()
        def serialNumber = UUID.randomUUID().toString()
        def value =
                BigDecimal.valueOf(0.1 + (999.9 * RANDOM.nextDouble())).setScale(2, RoundingMode.HALF_UP)

        return new Item(name, serialNumber, value);
    }

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
