package gr.iag.dgtl.inventory.persistance

import gr.iag.dgtl.inventory.dto.Item
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.TempDir

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class CsvItemPersistenceSpec extends Specification {

    @Subject
    CsvItemPersistence persistence

    @TempDir
    File tempDir

    def setup() {
        persistence = new CsvItemPersistence(tempDir.toString() + "/test.csv")
    }

    def 'Should persist items in CSV and then load them correctly'() {
        given: 'a list of items'
        List<Item> itemsToSave = [
                new Item('Item1', '123', 50 as BigDecimal),
                new Item('Item2', '456', 100 as BigDecimal)
        ]

        when: 'we save the items'
        persistence.saveItems(itemsToSave)

        then: 'a file is created'
        Path filePath = Paths.get(tempDir.toString(), 'test.csv')
        Files.exists(filePath)

        and: 'can load the items correctly'
        List<Item> loadedItems = persistence.loadItems()

        and: 'the loaded items are the same as the saved items'
        loadedItems.size() == 2
        loadedItems[0].name == 'Item1'
        loadedItems[0].serialNumber == '123'
        loadedItems[0].value == 50 as BigDecimal
        loadedItems[1].name == 'Item2'
        loadedItems[1].serialNumber == '456'
        loadedItems[1].value == 100 as BigDecimal
    }

    def 'Should handle empty list correctly'() {
        given: 'an empty list of items'
        List<Item> emptyList = []

        when: 'we save the empty list'
        persistence.saveItems(emptyList)

        then: 'a file is created'
        Path filePath = Paths.get(tempDir.toString(), 'test.csv')
        Files.exists(filePath)

        when: 'we load items from the empty file'
        List<Item> loadedItems = persistence.loadItems()

        then: 'an empty list is returned'
        loadedItems.isEmpty()
    }

    def 'Should handle null list correctly'() {
        given: 'an empty list of items'
        List<Item> nullList = [null]

        when: 'we save the empty list'
        persistence.saveItems(nullList)

        then: 'a file is created'
        thrown(NullPointerException)
    }

    def 'Should handle file write error correctly'() {
        given: 'a list of items'
        List<Item> items = [
                new Item('Item1', '123', 50 as BigDecimal),
                new Item('Item2', '456', 100 as BigDecimal)
        ]

        and: 'the file is read-only'
        File file = new File(tempDir.toString(), 'test.csv')
        file.createNewFile()
        file.setReadOnly()

        when: 'we save the list'
        persistence.saveItems(items)

        then: 'an exception is thrown'
        def exception = thrown(RuntimeException)
        exception.message.contains('Failed to save items to CSV file')
    }

    def 'Should handle non-existent file correctly'() {
        given: 'a non-existent file path'
        persistence = new CsvItemPersistence(tempDir.toString() + "/non_existent_file.csv")

        when: 'we load items from the non-existent file'
        def loadedItems = persistence.loadItems()

        then: 'an empty list is returned'
        loadedItems.isEmpty()
    }

    def 'Should handle invalid file format correctly'() {
        given: 'an invalid CSV file'
        File file = new File(tempDir.toString(), 'test.csv')
        file.write('This is not a CSV file')

        and: 'a persistence instance that uses the invalid CSV file'
        persistence = new CsvItemPersistence(file.getAbsolutePath())

        when: 'we load items from the invalid file'
        persistence.loadItems()

        then: 'an exception is thrown'
        def exception = thrown(RuntimeException)
        exception.message.contains('Failed to load items from CSV file')
    }
}
