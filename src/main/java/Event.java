/**
 * Represents a task that occurs between a user-provided start and end time.
 */
public class Event extends Task {
    /** The user-provided start date or time. */
    private final String from;

    /** The user-provided end date or time. */
    private final String to;

    /**
     * Creates an event task.
     *
     * @param description the text describing the event
     * @param from the user-provided start date or time
     * @param to the user-provided end date or time
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns this event in the task-list format.
     *
     * @return the formatted event
     */
    @Override
    public String toString() {
        return "[E]" + super.toString()
                + " (from: " + from + " to: " + to + ")";
    }
}
