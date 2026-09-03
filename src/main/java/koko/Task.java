package koko;

/**
 * Represents one task and whether it has been completed.
 * Subclasses add their task type and any date or time details.
 */
public abstract class Task {
    /** The text that describes what needs to be done. */
    private final String description;

    /** Whether this task has been completed. */
    private boolean isDone;

    /**
     * Creates a new task that is initially not done.
     *
     * @param description the text describing the task
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns the task description.
     *
     * @return the text describing the task
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the character displayed inside a task's status brackets.
     *
     * @return {@code X} when the task is done, or a space otherwise
     */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /**
     * Marks this task as completed.
     */
    public void markAsDone() {
        isDone = true;
    }

    /**
     * Marks this task as not completed.
     */
    public void markAsNotDone() {
        isDone = false;
    }

    /**
     * Returns the shared status and description portion of a task's display format.
     *
     * @return the formatted status and description
     */
    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}
