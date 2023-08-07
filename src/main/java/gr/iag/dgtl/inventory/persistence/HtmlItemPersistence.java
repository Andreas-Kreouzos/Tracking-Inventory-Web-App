package gr.iag.dgtl.inventory.persistence;

import gr.iag.dgtl.inventory.dto.Item;
import gr.iag.dgtl.inventory.exception.ResourceNotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Persists items to an HTML file
 *
 * @author kreouzosa
 */
@ApplicationScoped
@Named("HtmlItemPersistence")
public class HtmlItemPersistence implements IItemPersistence {

    private static final Logger LOGGER = LoggerFactory.getLogger(HtmlItemPersistence.class);

    private final String filePath;

    @Inject
    public HtmlItemPersistence(
            @ConfigProperty(name = "html.file.path") String filePath) {
        this.filePath = filePath;
        LOGGER.info("HTML file path: {}", filePath);
    }

    /**
     * @see IItemPersistence#loadItems()
     */
    @Override
    public List<Item> loadItems() {
        LOGGER.info("Attempting to load items from HTML");
        List<Item> items = new ArrayList<>();
        try {
            Document document = Jsoup.parse(new File(filePath), "UTF-8");
            Element table = document.select("table").first();
            Elements rows = table.select("tr");

            for (int i = 1; i < rows.size(); i++) { // first row is the header, skip it
                Element row = rows.get(i);
                Elements columns = row.select("td");

                String name = columns.get(0).text();
                String serialNumber = columns.get(1).text();
                BigDecimal value = new BigDecimal(columns.get(2).text().replace("$", ""));

                items.add(new Item(name, serialNumber, value));
            }
            LOGGER.info("Successfully loaded {} items from HTML", items.size());
        } catch (FileNotFoundException e) {
            LOGGER.info("HTML file does not exist, returning empty list");
            return items;
        } catch (IOException e) {
            LOGGER.error("Failed to load items from HTML file", e);
            throw new RuntimeException("Failed to load items from HTML file", e);
        }
        return items;
    }

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
        LOGGER.info("Attempting to save {} items to HTML", items.size());
        StringBuilder htmlContent = new StringBuilder("<table>\n" +
                "<tr><th>Name</th><th>Serial Number</th><th>Value</th></tr>\n");

        for (Item item : items) {
            if(item == null) {
                throw new NullPointerException("Cannot save null item");
            }

            htmlContent.append("<tr><td>")
                    .append(item.getName())
                    .append("</td><td>")
                    .append(item.getSerialNumber())
                    .append("</td><td>$")
                    .append(item.getValue().toString())
                    .append("</td></tr>\n");
        }

        htmlContent.append("</table>");

        try {
            Files.write(Paths.get(filePath), htmlContent.toString().getBytes());
            LOGGER.info("Successfully saved {} items to HTML", items.size());
        } catch (IOException e) {
            LOGGER.error("Failed to save items to HTML file", e);
            throw new RuntimeException("Failed to save items to HTML file", e);
        }
    }
}
