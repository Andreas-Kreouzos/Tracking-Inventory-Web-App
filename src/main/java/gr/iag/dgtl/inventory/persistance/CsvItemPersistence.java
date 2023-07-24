package gr.iag.dgtl.inventory.persistance;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import gr.iag.dgtl.inventory.dto.Item;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Persists items to a CSV file
 *
 * @author kreouzosa
 */
public class CsvItemPersistence implements IItemPersistence {

    private final String filePath;

    public CsvItemPersistence(String filePath) {
        this.filePath = filePath;
    }

    /**
     * @see IItemPersistence#loadItems()
     */
    @Override
    public List<Item> loadItems() {
        List<Item> items = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) {
            return items;
        }
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] line;
            while ((line = reader.readNext()) != null) {
                try {
                    String name = line[0];
                    String serialNumber = line[1];
                    BigDecimal value = new BigDecimal(line[2]);
                    items.add(new Item(name, serialNumber, value));
                } catch (ArrayIndexOutOfBoundsException e) {
                    throw new RuntimeException("Failed to load items from CSV file", e);
                }
            }
        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException("Failed to load items from CSV file", e);
        }
        return items;
    }


    /**
     * @see IItemPersistence#saveItems(List)
     */
    @Override
    public void saveItems(List<Item> items) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {
            for (Item item : items) {
                String[] line = {item.getName(), item.getSerialNumber(), item.getValue().toString()};
                writer.writeNext(line);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to save items to CSV file", e);
        }
    }
}
