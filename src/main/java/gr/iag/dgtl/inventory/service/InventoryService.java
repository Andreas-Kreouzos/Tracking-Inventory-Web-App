package gr.iag.dgtl.inventory.service;

import gr.iag.dgtl.inventory.dto.Item;
import gr.iag.dgtl.inventory.exception.InventoryException;
import gr.iag.dgtl.inventory.mapper.InventoryExceptionMapper;
import gr.iag.dgtl.inventory.persistance.IItemPersistence;
import jakarta.enterprise.context.ApplicationScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class InventoryService implements IInventoryService {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(InventoryService.class);

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
        try {
            if (item == null) {
                throw new InventoryException("Cannot add null item to inventory", null);
            }

            getItemBySerialNumber(item.getSerialNumber())
                    .ifPresent(existingItem -> {
                        throw new InventoryException("Item with this serial number already exists in inventory", null);
                    });

            items.add(item);
            itemPersistence.saveItems(items);
        } catch (Exception ex) {
            LOGGER.error("Error upon adding an Item with serialNumber={}", item.getSerialNumber(), ex);
            throw new InventoryException(InventoryExceptionMapper.DEFAULT_MESSAGE, ex);
        }
    }


    @Override
    public Optional<Item> getItemBySerialNumber(String serialNumber) {
        for (Item item : items) {
            if (item.getSerialNumber().equals(serialNumber)) {
                return Optional.of(item);
            }
        }
        return Optional.empty();
    }

    @Override
    public void deleteItem(String serialNumber) {
        try {
            Item item = getItemBySerialNumber(serialNumber)
                    .orElseThrow(() -> new InventoryException("Item not found"));
            items.remove(item);
            itemPersistence.saveItems(items);
        } catch (Exception ex) {
            LOGGER.error("Error upon deleting an Item with serialNumber={}", serialNumber, ex);
            throw new InventoryException(InventoryExceptionMapper.DEFAULT_MESSAGE, ex);
        }
    }
}