package gr.iag.dgtl.inventory.persistence;

import gr.iag.dgtl.inventory.dto.Item;
import gr.iag.dgtl.inventory.exception.ResourceNotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Persists items to a JSON file
 *
 * @author kreouzosa
 */
@ApplicationScoped
@Named("JsonItemPersistence")
public class JsonItemPersistence implements IItemPersistence {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonItemPersistence.class);

    private static String DATA_FILE;
    private final Jsonb jsonb;

    @Inject
    public JsonItemPersistence(
            @ConfigProperty(name = "json.data.file.path") String dataFile) {
        DATA_FILE = dataFile;
        LOGGER.info("JSON file path: {}", DATA_FILE);
        JsonbConfig config = new JsonbConfig().withFormatting(true);
        this.jsonb = JsonbBuilder.create(config);
    }

    /**
     * @see IItemPersistence#loadItems()
     */
    @Override
    public List<Item> loadItems() {
        LOGGER.info("Attempting to load items from JSON");
        File file = new File(DATA_FILE);
        List<Item> items = new ArrayList<>();
        if (file.exists()) {
            try (FileReader reader = new FileReader(file)) {
                Item[] loadedItems = jsonb.fromJson(reader, Item[].class);
                items.addAll(Arrays.asList(loadedItems));
                LOGGER.info("Successfully loaded {} items from JSON", items.size());
            } catch (IOException e) {
                LOGGER.error("Failed to load items from JSON file", e);
                throw new RuntimeException("Failed to load items from file", e);
            }
        } else {
            LOGGER.info("JSON file does not exist, returning empty list");
        }
        return items;
    }

    /**
     * @see IItemPersistence#getItemBySerialNumber
     */
    @Override
    public Item getItemBySerialNumber(String serialNumber) {
        List<Item> items = loadItems();
        return items.stream()
                .filter(item -> item.getSerialNumber().equals(serialNumber))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Item with serial number " + serialNumber + " not found"));
    }

    /**
     * @see IItemPersistence#saveItems(List)
     */
    @Override
    public void saveItems(List<Item> items) {
        LOGGER.info("Attempting to save {} items to JSON", items.size());
        checkNullItems(items);
        try (FileWriter writer = new FileWriter(DATA_FILE)) {
            jsonb.toJson(items, writer);
            LOGGER.info("Successfully saved {} items to JSON", items.size());
        } catch (IOException e) {
            LOGGER.error("Failed to save items to JSON file", e);
            throw new RuntimeException("Failed to save items to file", e);
        }
    }

    private void checkNullItems(List<Item> items) {
        for (Item item : items) {
            if (item == null) {
                throw new NullPointerException("Cannot save null item");
            }
        }
    }
}
