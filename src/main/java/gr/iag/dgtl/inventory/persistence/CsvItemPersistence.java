package gr.iag.dgtl.inventory.persistence;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import gr.iag.dgtl.inventory.dto.Item;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
@ApplicationScoped
@Named("CsvItemPersistence")
public class CsvItemPersistence implements IItemPersistence {

    private static final Logger LOGGER = LoggerFactory.getLogger(CsvItemPersistence.class);

    private final String filePath;

    @Inject
    public CsvItemPersistence(
            @ConfigProperty(name = "csv.file.path") String filePath) {
        this.filePath = filePath;
    }

    /**
     * @see IItemPersistence#loadItems()
     */
    @Override
    public List<Item> loadItems() {
        LOGGER.info("Attempting to load items from CSV");
        List<Item> items = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) {
            LOGGER.info("CSV file does not exist, returning empty list");
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
                    LOGGER.error("Error parsing CSV line", e);
                    throw new RuntimeException("Failed to load items from CSV file", e);
                }
            }
            LOGGER.info("Successfully loaded {} items from CSV", items.size());
        } catch (IOException | CsvValidationException e) {
            LOGGER.error("Failed to load items from CSV file", e);
            throw new RuntimeException("Failed to load items from CSV file", e);
        }
        return items;
    }


    /**
     * @see IItemPersistence#saveItems(List)
     */
    @Override
    public void saveItems(List<Item> items) {
        LOGGER.info("Attempting to save {} items to CSV", items.size());
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {
            for (Item item : items) {
                String[] line = {item.getName(), item.getSerialNumber(), item.getValue().toString()};
                writer.writeNext(line);
            }
            LOGGER.info("Successfully saved {} items to CSV", items.size());
        } catch (IOException e) {
            LOGGER.error("Failed to save items to CSV file", e);
            throw new RuntimeException("Failed to save items to CSV file", e);
        }
    }
}
