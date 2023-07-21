package gr.iag.dgtl.inventory.persistance

import gr.iag.dgtl.inventory.dto.Item
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.TempDir

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class JsonItemPersistenceSpec extends Specification {

    @Subject
    JsonItemPersistence persistence

    @TempDir
    File tempDir

    def setup() {
        persistence = new JsonItemPersistence(tempDir.toString() + "/test.json")
    }

    def 'Should persist items in Json and then load them correctly'() {
        given: 'a list of items'
        List<Item> itemsToSave = [
                new Item('Item1', '123', 50 as BigDecimal),
                new Item('Item2', '456', 100 as BigDecimal)
        ]

        when: 'we save the items'
        persistence.saveItems(itemsToSave)

        then: 'a file is created'
        Path filePath = Paths.get(tempDir.toString(), 'test.json')
        Files.exists(filePath)

        and: 'can load the items correctly'
        List<Item> loadedItems = persistence.loadItems()

        and: 'the loaded items are the same as the saved items'
        loadedItems.size() == 2
        loadedItems[0].name() == 'Item1'
        loadedItems[0].serialNumber() == '123'
        loadedItems[0].value() == 50 as BigDecimal
        loadedItems[1].name() == 'Item2'
        loadedItems[1].serialNumber() == '456'
        loadedItems[1].value() == 100 as BigDecimal
    }
}