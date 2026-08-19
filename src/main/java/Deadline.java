/**
 * Represents a task that must be completed by a specified time.
 */
public class Deadline extends Task {
    /** The user-provided due date or time. */
    private final String by;

    /**
     * Creates a deadline task.
     *
     * @param description the text describing the task
     * @param by the user-provided due date or time
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    /**
     * Returns this deadline in the task-list format.
     *
     * @return the formatted deadline
     */
    @Override
    public String toString() {
        return "[D][" + getStatusIcon() + "] " + getDescription() + " (by: " + by + ")";
    }
}
