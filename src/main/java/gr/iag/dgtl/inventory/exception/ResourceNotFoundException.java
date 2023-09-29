package gr.iag.dgtl.inventory.exception;

/**
 * An exception that is thrown when a specific resource is not found.
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Simple constructor that receives a {@link String} message to pass
     * to the corresponding {@link RuntimeException} constructor
     *
     * @param message an error message
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}