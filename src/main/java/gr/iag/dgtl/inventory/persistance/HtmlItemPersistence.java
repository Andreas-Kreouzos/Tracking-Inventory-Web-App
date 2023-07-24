package gr.iag.dgtl.inventory.persistance;

import gr.iag.dgtl.inventory.dto.Item;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
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
public class HtmlItemPersistence implements IItemPersistence {

    private final String filePath;

    public HtmlItemPersistence(String filePath) {
        this.filePath = filePath;
    }

    /**
     * @see IItemPersistence#loadItems()
     */
    @Override
    public List<Item> loadItems() {
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
        } catch (IOException e) {
            throw new RuntimeException("Failed to load items from HTML file", e);
        }
        return items;
    }

    /**
     * @see IItemPersistence#saveItems(List)
     */
    @Override
    public void saveItems(List<Item> items) {
        StringBuilder htmlContent = new StringBuilder("<table>\n" +
                "<tr><th>Name</th><th>Serial Number</th><th>Value</th></tr>\n");

        for (Item item : items) {
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
        } catch (IOException e) {
            throw new RuntimeException("Failed to save items to HTML file", e);
        }
    }
}
