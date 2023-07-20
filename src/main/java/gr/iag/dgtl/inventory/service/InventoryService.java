package gr.iag.dgtl.inventory.service;

import gr.iag.dgtl.inventory.dto.Item;
import gr.iag.dgtl.inventory.persistance.IItemPersistence;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class InventoryService implements IInventoryService {

    private final List<Item> items;
    private final IItemPersistence itemPersistence;

    public InventoryService(IItemPersistence itemPersistence) {
        this.itemPersistence = itemPersistence;
        List<Item> loadedItems = itemPersistence.loadItems();
        this.items = loadedItems != null ? loadedItems : new ArrayList<>();
    }

    @Override
    public List<Item> getItems() {
        return new ArrayList<>(items);
    }

    @Override
    public void addItem(Item item) {
        items.add(item);
        itemPersistence.saveItems(items);
    }

    @Override
    public Optional<Item> getItemBySerialNumber(String serialNumber) {
        return items.stream()
                .filter(item -> item.serialNumber().equals(serialNumber))
                .findFirst();
    }

    @Override
    public void deleteItem(String serialNumber) {
        getItemBySerialNumber(serialNumber).ifPresent(item -> {
            items.remove(item);
            itemPersistence.saveItems(items);
        });
    }
}