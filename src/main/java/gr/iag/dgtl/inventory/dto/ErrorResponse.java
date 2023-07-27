package gr.iag.dgtl.inventory.dto;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * General use DTO for returning a list of error messages
 */
public class ErrorResponse {

    private final List<String> errors;

    /**
     * No-args constructor for (de)serialization purposes
     */
    public ErrorResponse() {
        errors = Collections.emptyList();
    }

    /**
     * All-args constructor that initializes the final field
     *
     * @param errors a {@link List} of {@link String} members containing error messages
     */
    public ErrorResponse(List<String> errors) {
        this.errors = errors;
    }

    /**
     * Getter method for field {@link ErrorResponse#errors}
     * @return the field "errors"
     */
    public List<String> getErrors() {
        return errors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ErrorResponse that = (ErrorResponse) o;
        return Objects.equals(errors, that.errors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(errors);
    }

    @Override
    public String toString() {
        return "ErrorResponse{" +
                "errors=" + errors +
                '}';
    }
}
