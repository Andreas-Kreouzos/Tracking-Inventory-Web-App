package gr.iag.dgtl.inventory.persistance;

import gr.iag.dgtl.inventory.dto.Item;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;

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
public class JsonItemPersistence implements IItemPersistence {

    private static final String DATA_FILE = "inventory.json";
    private final Jsonb jsonb;

    public JsonItemPersistence() {
        JsonbConfig config = new JsonbConfig().withFormatting(true);
        this.jsonb = JsonbBuilder.create(config);
    }

    /**
     * @see IItemPersistence#loadItems()
     */
    @Override
    public List<Item> loadItems() {
        File file = new File(DATA_FILE);
        List<Item> items = new ArrayList<>();
        if (file.exists()) {
            try (FileReader reader = new FileReader(file)) {
                Item[] loadedItems = jsonb.fromJson(reader, Item[].class);
                items.addAll(Arrays.asList(loadedItems));
            } catch (IOException e) {
                throw new RuntimeException("Failed to load items from file", e);
            }
        }
        return items;
    }

    /**
     * @see IItemPersistence#saveItems(List)
     */
    @Override
    public void saveItems(List<Item> items) {
        try (FileWriter writer = new FileWriter(DATA_FILE)) {
            jsonb.toJson(items, writer);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save items to file", e);
        }
    }
}
