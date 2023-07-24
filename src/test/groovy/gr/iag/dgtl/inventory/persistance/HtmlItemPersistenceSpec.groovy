package gr.iag.dgtl.inventory.persistance

import gr.iag.dgtl.inventory.dto.Item
import org.jsoup.Jsoup
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.TempDir

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class HtmlItemPersistenceSpec extends Specification {

    @Subject
    HtmlItemPersistence persistence

    @TempDir
    File tempDir

    def setup() {
        persistence = new HtmlItemPersistence(tempDir.toString() + "/test.html")
    }

    def 'Should persist items in HTML and parse the HTML content correctly'() {
        given: 'a list of items'
        List<Item> itemsToSave = [
                new Item('Item1', '123', 50 as BigDecimal),
                new Item('Item2', '456', 100 as BigDecimal)
        ]

        when: 'we save the items'
        persistence.saveItems(itemsToSave)

        then: 'a file is created'
        Path filePath = Paths.get(tempDir.toString(), 'test.html')
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

        and: 'the HTML content can be parsed correctly'
        String fileContent = new String(Files.readAllBytes(filePath))
        def doc = Jsoup.parse(fileContent)
        def rows = doc.select("table tr")
        rows.size() == 3  // Header row + 2 data rows

        def expectedValues = [
                ['Item1', '123', '$50'],
                ['Item2', '456', '$100']
        ]

        rows.drop(1).eachWithIndex { row, i -> // drop the header row
            row.select("td").eachWithIndex { cell, j ->
                assert cell.text() == expectedValues[i][j]
            }
        }
    }

    def 'Should handle empty list correctly'() {
        given: 'an empty list of items'
        List<Item> emptyList = []

        when: 'we save the empty list'
        persistence.saveItems(emptyList)

        then: 'a HTML file is created'
        Path filePath = Paths.get(tempDir.toString(), 'test.html')
        Files.exists(filePath)

        when: 'we load items from the empty list'
        List<Item> loadedItems = persistence.loadItems()

        then: 'an empty list is returned'
        loadedItems.isEmpty()
    }

    def 'Should handle null list correctly'() {
        given: 'a list containing null'
        List<Item> nullList = [null]

        when: 'we save the list'
        persistence.saveItems(nullList)

        then: 'an exception is thrown'
        def exception = thrown(NullPointerException)
        exception.message.contains('Cannot save null item')
    }

    def 'Should handle non-existent file correctly'() {
        given: 'a non-existent file path'
        persistence = new HtmlItemPersistence(tempDir.toString() + "/non_existent_file.html")

        when: 'we load items from the non-existent file'
        def loadedItems = persistence.loadItems()

        then: 'an empty list is returned'
        loadedItems.isEmpty()
    }
}
