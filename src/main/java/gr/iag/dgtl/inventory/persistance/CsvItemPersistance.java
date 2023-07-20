package gr.iag.dgtl.inventory.persistance;

import gr.iag.dgtl.inventory.dto.Item;

import java.util.List;

/**
 * Persists items to a CSV file
 *
 * @author kreouzosa
 */
public class CsvItemPersistance implements IItemPersistence {

    /**
     * @see IItemPersistence#loadItems()
     */
    @Override
    public List<Item> loadItems() {
        return null;
    }

    /**
     * @see IItemPersistence#saveItems(List)
     */
    @Override
    public void saveItems(List<Item> items) {

    }
}
