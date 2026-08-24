import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a task that must be completed by a specified time.
 */
public class Deadline extends Task {
    /** The deadline date and time. */
    private final LocalDateTime by;

    /**
     * Creates a deadline task.
     *
     * @param description the text describing the task
     * @param by the deadline date and time
     */
    public Deadline(String description, LocalDateTime by) {
        super(description);
        this.by = by;
    }

    /**
     * Returns the deadline date and time.
     *
     * @return the deadline date and time
     */
    public LocalDateTime getBy() {
        return by;
    }

    /**
     * Returns this deadline in the task-list format.
     *
     * @return the formatted deadline
     */
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d yyyy, h:mm a");
        return "[D]" + super.toString() + " (by: " + by.format(formatter) + ")";
    }
}