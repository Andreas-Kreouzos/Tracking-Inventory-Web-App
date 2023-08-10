package gr.iag.dgtl.inventory.service;

import gr.iag.dgtl.inventory.dto.Item;
import gr.iag.dgtl.inventory.exception.InventoryException;
import gr.iag.dgtl.inventory.exception.ResourceNotFoundException;
import gr.iag.dgtl.inventory.persistence.IItemPersistence;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.inject.Named;

import java.util.ArrayList;
import java.util.List;

import static gr.iag.dgtl.inventory.mapper.ResourceNotFoundExceptionMapper.DEFAULT_MESSAGE;

@ApplicationScoped
public class InventoryService implements IInventoryService {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(InventoryService.class);

    private final IItemPersistence htmlPersistence;
    private final IItemPersistence jsonPersistence;
    private final IItemPersistence csvPersistence;
    private final List<Item> items;

    @Inject
    public InventoryService(@Named("HtmlItemPersistence") IItemPersistence htmlPersistence,
                            @Named("JsonItemPersistence") IItemPersistence jsonPersistence,
                            @Named("CsvItemPersistence") IItemPersistence csvPersistence) {
        this.htmlPersistence = htmlPersistence;
        this.jsonPersistence = jsonPersistence;
        this.csvPersistence = csvPersistence;
        this.items = new ArrayList<>();
        this.items.addAll(htmlPersistence.loadItems());
        this.items.addAll(jsonPersistence.loadItems());
        this.items.addAll(csvPersistence.loadItems());
    }

    /**
     * @see IInventoryService#getItems
     */
    @Override
    public List<Item> getItems() {
        return new ArrayList<>(items);
    }

    /**
     * @see IInventoryService#addItem(Item)
     */
    @Override
    public void addItem(Item item) {
        if (items.stream().anyMatch(existingItem -> existingItem.getSerialNumber().equals(item.getSerialNumber()))) {
            throw new InventoryException("Item with this serial number already exists in inventory");
        }
        items.add(item);
        jsonPersistence.saveItems(items);
        htmlPersistence.saveItems(items);
        csvPersistence.saveItems(items);
    }

    /**
     * @see IInventoryService#deleteItem(String)
     */
    @Override
    public void deleteItem(String serialNumber) {
        Item item = items.stream()
                .filter(existingItem -> existingItem.getSerialNumber().equals(serialNumber))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(DEFAULT_MESSAGE));

        items.remove(item);
        htmlPersistence.saveItems(items);
        jsonPersistence.saveItems(items);
        csvPersistence.saveItems(items);
    }
}
