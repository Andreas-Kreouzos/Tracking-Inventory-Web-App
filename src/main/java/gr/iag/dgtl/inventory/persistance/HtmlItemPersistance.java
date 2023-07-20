package gr.iag.dgtl.inventory.persistance;

import gr.iag.dgtl.inventory.dto.Item;

import java.util.List;

/**
 * Persists items to an HTML file
 *
 * @author kreouzosa
 */
public class HtmlItemPersistance implements IItemPersistence {

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
