package koko;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Handles parsing and validation of koko.Koko commands.
 */
public class Parser {
    /** The format accepted for dates and times entered by the user. */
    private static final DateTimeFormatter INPUT_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

    /**
     * Checks whether a command is the {@code bye} command.
     *
     * @param command the user's command
     * @return true if the command is bye
     */
    public static boolean isBye(String command) {
        return command.equals("bye");
    }

    /**
     * Checks whether a command is the {@code list} command.
     *
     * @param command the user's command
     * @return true if the command is list
     */
    public static boolean isList(String command) {
        return command.equals("list");
    }

    /**
     * Checks whether a command starts with the given action.
     *
     * @param command the user's command
     * @param action the command action
     * @return true if the command is the action or starts with the action followed by a space
     */
    public static boolean isCommand(String command, String action) {
        return command.equals(action) || command.startsWith(action + " ");
    }

    /**
     * Extracts a task number from a command.
     *
     * @param command the user's command
     * @param action the action named in the command
     * @param numberOfTasks how many tasks are currently stored
     * @return the zero-based task index
     * @throws KokoException if the task number is missing, invalid, or unavailable
     */
    public static int getTaskIndex(String command, String action, int numberOfTasks)
            throws KokoException {
        String taskNumberText = command.substring(action.length()).trim();

        try {
            int taskNumber = Integer.parseInt(taskNumberText);
            int taskIndex = taskNumber - 1;

            if (numberOfTasks == 0) {
                throw new KokoException(
                        "There are no tasks to " + action + " yet. Add one first.");
            }

            if (taskIndex < 0 || taskIndex >= numberOfTasks) {
                throw new KokoException(
                        "Choose a task number from 1 to " + numberOfTasks + ".");
            }

            return taskIndex;
        } catch (NumberFormatException exception) {
            throw new KokoException(
                    "I need a task number to " + action + ". Try: " + action + " 2.");
        }
    }

    /**
     * Extracts the description from a todo command.
     *
     * @param command the user's todo command
     * @return the task description
     * @throws KokoException if the description is missing
     */
    public static String parseTodo(String command) throws KokoException {
        String description = command.substring("todo".length()).trim();

        if (description.isEmpty()) {
            throw new KokoException(
                    "A to-do needs a description. Try: todo borrow book.");
        }

        return description;
    }

    /**
     * Parses a deadline command and creates a koko.Deadline.
     *
     * @param command the user's deadline command
     * @return the parsed koko.Deadline
     * @throws KokoException if the command or date/time is invalid
     */
    public static Deadline parseDeadline(String command) throws KokoException {
        String details = command.substring("deadline".length()).trim();
        int byMarker = details.indexOf(" /by ");

        if (byMarker < 1 || byMarker + " /by ".length() >= details.length()) {
            throw new KokoException(
                    "A deadline needs a description and a /by date and time. "
                            + "Try: deadline return book /by 2019-12-02 1800.");
        }

        String description = details.substring(0, byMarker).trim();
        String byText = details.substring(byMarker + " /by ".length()).trim();

        if (description.isEmpty() || byText.isEmpty()) {
            throw new KokoException(
                    "A deadline needs a description and a /by date and time. "
                            + "Try: deadline return book /by 2019-12-02 1800.");
        }

        try {
            LocalDateTime by = LocalDateTime.parse(byText, INPUT_FORMAT);
            return new Deadline(description, by);
        } catch (DateTimeParseException exception) {
            throw new KokoException(
                    "I couldn't understand that date and time. "
                            + "Use yyyy-MM-dd HHmm, e.g. 2019-12-02 1800.");
        }
    }

    /**
     * Parses an event command and creates an koko.Event.
     *
     * @param command the user's event command
     * @return the parsed koko.Event
     * @throws KokoException if the command or date/time is invalid
     */
    public static Event parseEvent(String command) throws KokoException {
        String details = command.substring("event".length()).trim();
        int fromMarker = details.indexOf(" /from ");
        int toMarker = details.indexOf(" /to ");

        if (fromMarker < 1
                || toMarker < fromMarker + " /from ".length()
                || toMarker + " /to ".length() >= details.length()) {
            throw new KokoException(
                    "An event needs a description, /from date and time, and /to date and time. "
                            + "Try: event lecture /from 2019-12-02 1400 /to 2019-12-02 1600.");
        }

        String description = details.substring(0, fromMarker).trim();
        String fromText = details.substring(
                fromMarker + " /from ".length(), toMarker).trim();
        String toText = details.substring(toMarker + " /to ".length()).trim();

        if (description.isEmpty() || fromText.isEmpty() || toText.isEmpty()) {
            throw new KokoException(
                    "An event needs a description, /from date and time, and /to date and time. "
                            + "Try: event lecture /from 2019-12-02 1400 /to 2019-12-02 1600.");
        }

        try {
            LocalDateTime from = LocalDateTime.parse(fromText, INPUT_FORMAT);
            LocalDateTime to = LocalDateTime.parse(toText, INPUT_FORMAT);

            if (to.isBefore(from)) {
                throw new KokoException(
                        "An event cannot end before it starts.");
            }

            return new Event(description, from, to);
        } catch (DateTimeParseException exception) {
            throw new KokoException(
                    "I couldn't understand the event date and time. "
                            + "Use yyyy-MM-dd HHmm, e.g. 2019-12-02 1400.");
        }
    }
}
