import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Starts Koko's command-line interface.
 */
public class Koko {
    /** The format accepted for dates and times entered by the user. */
    private static final DateTimeFormatter INPUT_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

    /**
     * Displays the welcome message, loads saved tasks, stores entered tasks,
     * lists them on request, and stops on {@code bye}.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        String banner = " _  __     _          \n"
                + "| |/ /___ | | _____   \n"
                + "| ' // _ \\| |/ / _ \\  \n"
                + "| . \\ (_) |   < (_) | \n"
                + "|_|\\_\\___/|_|\\_\\___/  \n";

        System.out.println(banner);
        System.out.println("What can I do for you?");

        Storage storage = new Storage();
        List<Task> tasks;

        try {
            tasks = storage.load();
        } catch (KokoException exception) {
            printError(exception.getMessage());
            tasks = new ArrayList<>();
        }

        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()) {
            String command = scanner.nextLine().trim();

            try {
                if (command.equals("bye")) {
                    System.out.println("Bye. Hope to see you again soon!");
                    return;
                }

                if (command.equals("list")) {
                    printTasks(tasks);
                } else if (command.equals("mark") || command.startsWith("mark ")) {
                    markTask(command, tasks, storage);
                } else if (command.equals("unmark") || command.startsWith("unmark ")) {
                    unmarkTask(command, tasks, storage);
                } else if (command.equals("delete") || command.startsWith("delete ")) {
                    deleteTask(command, tasks, storage);
                } else if (command.equals("todo") || command.startsWith("todo ")) {
                    addTodo(command, tasks, storage);
                } else if (command.equals("deadline") || command.startsWith("deadline ")) {
                    addDeadline(command, tasks, storage);
                } else if (command.equals("event") || command.startsWith("event ")) {
                    addEvent(command, tasks, storage);
                } else {
                    throw new KokoException("I don't recognise that command. "
                            + "Try todo, deadline, event, list, mark, unmark, or delete.");
                }
            } catch (KokoException exception) {
                printError(exception.getMessage());
            }
        }
    }

    /**
     * Prints all currently stored tasks with user-friendly numbering and their completion status.
     *
     * @param tasks the list containing the tasks
     */
    private static void printTasks(List<Task> tasks) {
        printDivider();
        System.out.println("Here are the tasks in your list:");
        for (int index = 0; index < tasks.size(); index++) {
            System.out.println((index + 1) + "." + tasks.get(index));
        }
        printDivider();
    }

    /**
     * Marks the task identified by a one-based task number as done.
     *
     * @param command the user's {@code mark} command
     * @param tasks the list containing the tasks
     * @param storage the storage used to save the task list
     * @throws KokoException if the command does not identify a stored task
     *                       or if the tasks cannot be saved
     */
    private static void markTask(String command, List<Task> tasks, Storage storage)
            throws KokoException {
        int taskIndex = getTaskIndex(command, "mark", tasks.size());

        tasks.get(taskIndex).markAsDone();
        storage.save(tasks);

        printDivider();
        System.out.println("Nice! I've marked this task as done:");
        System.out.println("  " + tasks.get(taskIndex));
        printDivider();
    }

    /**
     * Marks the task identified by a one-based task number as not done.
     *
     * @param command the user's {@code unmark} command
     * @param tasks the list containing the tasks
     * @param storage the storage used to save the task list
     * @throws KokoException if the command does not identify a stored task
     *                       or if the tasks cannot be saved
     */
    private static void unmarkTask(String command, List<Task> tasks, Storage storage)
            throws KokoException {
        int taskIndex = getTaskIndex(command, "unmark", tasks.size());

        tasks.get(taskIndex).markAsNotDone();
        storage.save(tasks);

        printDivider();
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println("  " + tasks.get(taskIndex));
        printDivider();
    }

    /**
     * Removes the task identified by a one-based task number from the task list.
     *
     * @param command the user's {@code delete} command
     * @param tasks the list containing the tasks
     * @param storage the storage used to save the task list
     * @throws KokoException if the command does not identify a stored task
     *                       or if the tasks cannot be saved
     */
    private static void deleteTask(String command, List<Task> tasks, Storage storage)
            throws KokoException {
        int taskIndex = getTaskIndex(command, "delete", tasks.size());

        Task removedTask = tasks.remove(taskIndex);
        storage.save(tasks);

        printDivider();
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + removedTask);
        System.out.println("Now you have " + tasks.size() + " tasks in the list.");
        printDivider();
    }

    /**
     * Converts the task number in a task-changing command into a valid zero-based array index.
     *
     * @param command the user's command that includes a task number
     * @param action the action named in the command
     * @param numberOfTasks how many tasks are currently stored
     * @return the zero-based index of the requested task
     * @throws KokoException if the task number is missing, invalid, or unavailable
     */
    private static int getTaskIndex(String command, String action, int numberOfTasks)
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
     * Prints the divider used around command responses.
     */
    private static void printDivider() {
        System.out.println("____________________________________________________________");
    }

    /**
     * Displays a user-input error between dividers so it is easy to distinguish from normal responses.
     *
     * @param message the explanation and correction for the invalid command
     */
    private static void printError(String message) {
        printDivider();
        System.out.println("Oops! " + message);
        printDivider();
    }

    /**
     * Adds a to-do task from a {@code todo DESCRIPTION} command.
     *
     * @param command the user's command
     * @param tasks the list containing the tasks
     * @param storage the storage used to save the task list
     * @throws KokoException if the description is missing or the task cannot be saved
     */
    private static void addTodo(String command, List<Task> tasks, Storage storage)
            throws KokoException {
        String description = command.substring("todo".length()).trim();

        if (description.isEmpty()) {
            throw new KokoException(
                    "A to-do needs a description. Try: todo borrow book.");
        }

        addTask(new Todo(description), tasks, storage);
    }

    /**
     * Adds a deadline task from a {@code deadline DESCRIPTION /by DATE TIME} command.
     *
     * @param command the user's command
     * @param tasks the list containing the tasks
     * @param storage the storage used to save the task list
     * @throws KokoException if the description or due date/time is missing or invalid
     */
    private static void addDeadline(String command, List<Task> tasks, Storage storage)
            throws KokoException {
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
            addTask(new Deadline(description, by), tasks, storage);
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
     * @param tasks the list containing the tasks
     * @param storage the storage used to save the task list
     * @throws KokoException if the description or date/time is missing or invalid
     */
    private static void addEvent(String command, List<Task> tasks, Storage storage)
            throws KokoException {
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

            addTask(new Event(description, from, to), tasks, storage);
        } catch (DateTimeParseException exception) {
            throw new KokoException(
                    "I couldn't understand the event date and time. "
                            + "Use yyyy-MM-dd HHmm, e.g. 2019-12-02 1400.");
        }
    }

    /**
     * Stores a task, saves the updated task list, and displays the standard confirmation.
     *
     * @param task the task to store
     * @param tasks the list containing the tasks
     * @param storage the storage used to save the task list
     * @throws KokoException if the task list cannot be saved
     */
    private static void addTask(Task task, List<Task> tasks, Storage storage)
            throws KokoException {
        tasks.add(task);
        storage.save(tasks);

        printDivider();
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + tasks.size() + " tasks in the list.");
        printDivider();
    }
}