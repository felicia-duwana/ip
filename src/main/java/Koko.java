import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Starts Koko's command-line interface.
 */
public class Koko {
    /** The format accepted for dates and times entered by the user. */
    private static final DateTimeFormatter INPUT_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

    /**
     * The storage used to save and load tasks.
     */
    private final Storage storage;

    /**
     * The task list managed by Koko.
     */
    private TaskList tasks;

    /**
     * The user interface used to communicate with the user.
     */
    private final Ui ui;

    /**
     * Creates a Koko application.
     */
    public Koko() {
        storage = new Storage();
        ui = new Ui();

        try {
            tasks = new TaskList(storage.load());
        } catch (KokoException exception) {
            ui.showError(exception.getMessage());
            tasks = new TaskList();
        }
    }

    /**
     * Runs the Koko command-line interface.
     */
    public void run() {
        ui.showWelcome();

        while (true) {
            String command = ui.readCommand();

            try {
                if (command.equals("bye")) {
                    ui.showBye();
                    return;
                }

                if (command.equals("list")) {
                    printTasks();
                } else if (command.equals("mark") || command.startsWith("mark ")) {
                    markTask(command);
                } else if (command.equals("unmark") || command.startsWith("unmark ")) {
                    unmarkTask(command);
                } else if (command.equals("delete") || command.startsWith("delete ")) {
                    deleteTask(command);
                } else if (command.equals("todo") || command.startsWith("todo ")) {
                    addTodo(command);
                } else if (command.equals("deadline") || command.startsWith("deadline ")) {
                    addDeadline(command);
                } else if (command.equals("event") || command.startsWith("event ")) {
                    addEvent(command);
                } else {
                    throw new KokoException("I don't recognise that command. "
                            + "Try todo, deadline, event, list, mark, unmark, or delete.");
                }
            } catch (KokoException exception) {
                ui.showError(exception.getMessage());
            }
        }
    }

    /**
     * Prints all currently stored tasks.
     */
    private void printTasks() {
        ui.showTasks(tasks);
    }

    /**
     * Marks the task identified by a one-based task number as done.
     *
     * @param command the user's mark command
     * @throws KokoException if the command does not identify a stored task
     *                       or if the tasks cannot be saved
     */
    private void markTask(String command) throws KokoException {
        int taskIndex = getTaskIndex(command, "mark", tasks.size());

        tasks.get(taskIndex).markAsDone();
        storage.save(tasks.getTasks());

        ui.showMarkDone(tasks.get(taskIndex));
    }

    /**
     * Marks the task identified by a one-based task number as not done.
     *
     * @param command the user's unmark command
     * @throws KokoException if the command does not identify a stored task
     *                       or if the tasks cannot be saved
     */
    private void unmarkTask(String command) throws KokoException {
        int taskIndex = getTaskIndex(command, "unmark", tasks.size());

        tasks.get(taskIndex).markAsNotDone();
        storage.save(tasks.getTasks());

        ui.showMarkNotDone(tasks.get(taskIndex));
    }

    /**
     * Removes the task identified by a one-based task number.
     *
     * @param command the user's delete command
     * @throws KokoException if the command does not identify a stored task
     *                       or if the tasks cannot be saved
     */
    private void deleteTask(String command) throws KokoException {
        int taskIndex = getTaskIndex(command, "delete", tasks.size());

        Task removedTask = tasks.remove(taskIndex);
        storage.save(tasks.getTasks());

        ui.showDeletedTask(removedTask, tasks.size());
    }

    /**
     * Converts the task number in a task-changing command into a valid zero-based index.
     *
     * @param command the user's command that includes a task number
     * @param action the action named in the command
     * @param numberOfTasks how many tasks are currently stored
     * @return the zero-based index of the requested task
     * @throws KokoException if the task number is missing, invalid, or unavailable
     */
    private int getTaskIndex(String command, String action, int numberOfTasks)
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
     * Adds a to-do task from a {@code todo DESCRIPTION} command.
     *
     * @param command the user's command
     * @throws KokoException if the description is missing or the task cannot be saved
     */
    private void addTodo(String command) throws KokoException {
        String description = command.substring("todo".length()).trim();

        if (description.isEmpty()) {
            throw new KokoException(
                    "A to-do needs a description. Try: todo borrow book.");
        }

        addTask(new Todo(description));
    }

    /**
     * Adds a deadline task from a {@code deadline DESCRIPTION /by DATE TIME} command.
     *
     * @param command the user's command
     * @throws KokoException if the description or due date/time is missing or invalid
     */
    private void addDeadline(String command) throws KokoException {
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
            addTask(new Deadline(description, by));
        } catch (DateTimeParseException exception) {
            throw new KokoException(
                    "I couldn't understand that date and time. "
                            + "Use yyyy-MM-dd HHmm, e.g. 2019-12-02 1800.");
        }
    }

    /**
     * Adds an event task from an
     * {@code event DESCRIPTION /from DATE TIME /to DATE TIME} command.
     *
     * @param command the user's command
     * @throws KokoException if the description or date/time is missing or invalid
     */
    private void addEvent(String command) throws KokoException {
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

            addTask(new Event(description, from, to));
        } catch (DateTimeParseException exception) {
            throw new KokoException(
                    "I couldn't understand the event date and time. "
                            + "Use yyyy-MM-dd HHmm, e.g. 2019-12-02 1400.");
        }
    }

    /**
     * Stores a task, saves the updated task list, and displays the confirmation.
     *
     * @param task the task to store
     * @throws KokoException if the task list cannot be saved
     */
    private void addTask(Task task) throws KokoException {
        tasks.add(task);
        storage.save(tasks.getTasks());

        ui.showAddedTask(task, tasks.size());
    }

    /**
     * Starts the Koko application.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        new Koko().run();
    }
}