package gr.iag.dgtl.inventory.dto;

/**
 *  Contains the fields of the Item process response.
 */
public class ItemResponse {
    private String status;
    private String message;
    private String itemId;

    public ItemResponse() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
}
