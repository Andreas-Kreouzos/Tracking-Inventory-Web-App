package gr.iag.dgtl.inventory.exception;

/**
 * General use InventoryException subclass that is to be used a general unchecked exception
 * or the super class of all custom unchecked exceptions
 */
public class InventoryException extends RuntimeException {

    /**
     * Simple no-args constructor
     */
    public InventoryException() {
        super();
    }

    /**
     * Simple constructor that receives a {@link String} message to pass
     * to the corresponding {@link RuntimeException} constructor
     *
     * @param message an error message
     */
    public InventoryException(String message){
        super(message);
    }

    /**
     * Simple constructor that receives a {@link String} message and a {@link Throwable}
     * to pass to the corresponding {@link RuntimeException} constructor
     *
     * @param message a {@link String} error message
     * @param e a {@link Throwable} cause of the exception
     */
    public InventoryException(String message, Throwable e){
        super(message, e);
    }
}
