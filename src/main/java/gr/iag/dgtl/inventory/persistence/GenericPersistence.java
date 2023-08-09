package gr.iag.dgtl.inventory.persistence;

import gr.iag.dgtl.inventory.dto.Item;
import gr.iag.dgtl.inventory.exception.ResourceNotFoundException;

import java.util.List;

/**
 * Provides a generic implementation for retrieving an {@link Item} by its serial number.
 * Subclasses are expected to define the actual mechanism for loading items from the respective data sources.
 */
public abstract class GenericPersistence {

    protected abstract List<Item> loadItems();

    /**
     * @see IItemPersistence#getItemBySerialNumber
     */
    public Item getItemBySerialNumber(String serialNumber) {
        List<Item> items = loadItems();
        return items.stream()
                .filter(item -> item.getSerialNumber().equals(serialNumber))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Item with serial number " + serialNumber + " not found"));
    }
}
