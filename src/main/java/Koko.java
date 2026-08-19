import java.util.Scanner;

/**
 * Starts Koko's command-line interface.
 */
public class Koko {
    /** Maximum number of tasks that can be kept during one program run. */
    private static final int MAX_TASKS = 100;

    /**
     * Displays the welcome message, stores entered tasks, lists them on request, and stops on {@code bye}.
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

        Task[] tasks = new Task[MAX_TASKS];
        int numberOfTasks = 0;
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine().trim();

            try {
                if (command.equals("bye")) {
                    System.out.println("Bye. Hope to see you again soon!");
                    return;
                }

                if (command.equals("list")) {
                    printTasks(tasks, numberOfTasks);
                } else if (command.equals("mark") || command.startsWith("mark ")) {
                    markTask(command, tasks, numberOfTasks);
                } else if (command.equals("unmark") || command.startsWith("unmark ")) {
                    unmarkTask(command, tasks, numberOfTasks);
                } else if (command.equals("delete") || command.startsWith("delete ")) {
                    numberOfTasks = deleteTask(command, tasks, numberOfTasks);
                } else if (command.equals("todo") || command.startsWith("todo ")) {
                    numberOfTasks = addTodo(command, tasks, numberOfTasks);
                } else if (command.equals("deadline") || command.startsWith("deadline ")) {
                    numberOfTasks = addDeadline(command, tasks, numberOfTasks);
                } else if (command.equals("event") || command.startsWith("event ")) {
                    numberOfTasks = addEvent(command, tasks, numberOfTasks);
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
     * @param tasks the array containing the tasks
     * @param numberOfTasks how many positions in {@code tasks} contain a task
     */
    private static void printTasks(Task[] tasks, int numberOfTasks) {
        printDivider();
        System.out.println("Here are the tasks in your list:");
        for (int index = 0; index < numberOfTasks; index++) {
            System.out.println((index + 1) + "." + tasks[index]);
        }
        printDivider();
    }

    /**
     * Marks the task identified by a one-based task number as done.
     *
     * @param command the user's {@code mark} command
     * @param tasks the array containing the tasks
     * @param numberOfTasks how many positions in {@code tasks} contain a task
     * @throws KokoException if the command does not identify a stored task
     */
    private static void markTask(String command, Task[] tasks, int numberOfTasks) throws KokoException {
        int taskIndex = getTaskIndex(command, "mark", numberOfTasks);
        tasks[taskIndex].markAsDone();
        printDivider();
        System.out.println("Nice! I've marked this task as done:");
        System.out.println("  " + tasks[taskIndex]);
        printDivider();
    }

    /**
     * Marks the task identified by a one-based task number as not done.
     *
     * @param command the user's {@code unmark} command
     * @param tasks the array containing the tasks
     * @param numberOfTasks how many positions in {@code tasks} contain a task
     * @throws KokoException if the command does not identify a stored task
     */
    private static void unmarkTask(String command, Task[] tasks, int numberOfTasks) throws KokoException {
        int taskIndex = getTaskIndex(command, "unmark", numberOfTasks);
        tasks[taskIndex].markAsNotDone();
        printDivider();
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println("  " + tasks[taskIndex]);
        printDivider();
    }

    /**
     * Removes the task identified by a one-based task number and closes the gap in the task list.
     *
     * @param command the user's {@code delete} command
     * @param tasks the array containing the tasks
     * @param numberOfTasks how many positions in {@code tasks} contain a task
     * @return the updated task count
     * @throws KokoException if the command does not identify a stored task
     */
    private static int deleteTask(String command, Task[] tasks, int numberOfTasks) throws KokoException {
        int taskIndex = getTaskIndex(command, "delete", numberOfTasks);
        Task removedTask = tasks[taskIndex];
        for (int index = taskIndex; index < numberOfTasks - 1; index++) {
            tasks[index] = tasks[index + 1];
        }

        int updatedNumberOfTasks = numberOfTasks - 1;
        tasks[updatedNumberOfTasks] = null;
        printDivider();
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + removedTask);
        System.out.println("Now you have " + updatedNumberOfTasks + " tasks in the list.");
        printDivider();
        return updatedNumberOfTasks;
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
    private static int getTaskIndex(String command, String action, int numberOfTasks) throws KokoException {
        String taskNumberText = command.substring(action.length()).trim();
        try {
            int taskNumber = Integer.parseInt(taskNumberText);
            int taskIndex = taskNumber - 1;
            if (numberOfTasks == 0) {
                throw new KokoException("There are no tasks to " + action + " yet. Add one first.");
            }
            if (taskIndex < 0 || taskIndex >= numberOfTasks) {
                throw new KokoException("Choose a task number from 1 to " + numberOfTasks + ".");
            }
            return taskIndex;
        } catch (NumberFormatException exception) {
            throw new KokoException("I need a task number to " + action + ". Try: " + action + " 2.");
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
     * @param tasks the array containing the tasks
     * @param numberOfTasks how many positions in {@code tasks} contain a task
     * @return the updated task count
     * @throws KokoException if the description is missing or there is no room for another task
     */
    private static int addTodo(String command, Task[] tasks, int numberOfTasks) throws KokoException {
        String description = command.substring("todo".length()).trim();
        if (description.isEmpty()) {
            throw new KokoException("A to-do needs a description. Try: todo borrow book.");
        }
        return addTask(new Todo(description), tasks, numberOfTasks);
    }

    /**
     * Adds a deadline task from a {@code deadline DESCRIPTION /by TIME} command.
     * Date and time text is deliberately kept as entered by the user.
     *
     * @param command the user's command
     * @param tasks the array containing the tasks
     * @param numberOfTasks how many positions in {@code tasks} contain a task
     * @return the updated task count
     * @throws KokoException if the description or due time is missing, or the list is full
     */
    private static int addDeadline(String command, Task[] tasks, int numberOfTasks) throws KokoException {
        String details = command.substring("deadline".length()).trim();
        int byMarker = details.indexOf(" /by ");
        if (byMarker < 1 || byMarker + " /by ".length() >= details.length()) {
            throw new KokoException("A deadline needs a description and a /by time. "
                    + "Try: deadline return book /by Friday.");
        }
        String description = details.substring(0, byMarker).trim();
        String by = details.substring(byMarker + " /by ".length()).trim();
        return addTask(new Deadline(description, by), tasks, numberOfTasks);
    }

    /**
     * Adds an event task from an {@code event DESCRIPTION /from START /to END} command.
     * Date and time text is deliberately kept as entered by the user.
     *
     * @param command the user's command
     * @param tasks the array containing the tasks
     * @param numberOfTasks how many positions in {@code tasks} contain a task
     * @return the updated task count
     * @throws KokoException if the description, start time, or end time is missing, or the list is full
     */
    private static int addEvent(String command, Task[] tasks, int numberOfTasks) throws KokoException {
        String details = command.substring("event".length()).trim();
        int fromMarker = details.indexOf(" /from ");
        int toMarker = details.indexOf(" /to ");
        if (fromMarker < 1 || toMarker < fromMarker + " /from ".length()
                || toMarker + " /to ".length() >= details.length()) {
            throw new KokoException("An event needs a description, /from time, and /to time. "
                    + "Try: event lecture /from Monday 2pm /to Monday 4pm.");
        }
        String description = details.substring(0, fromMarker).trim();
        String from = details.substring(fromMarker + " /from ".length(), toMarker).trim();
        String to = details.substring(toMarker + " /to ".length()).trim();
        if (from.isEmpty()) {
            throw new KokoException("An event needs a description, /from time, and /to time. "
                    + "Try: event lecture /from Monday 2pm /to Monday 4pm.");
        }
        return addTask(new Event(description, from, to), tasks, numberOfTasks);
    }

    /**
     * Stores a task when there is room and displays the standard confirmation.
     *
     * @param task the task to store
     * @param tasks the array containing the tasks
     * @param numberOfTasks how many positions in {@code tasks} contain a task
     * @return the updated task count
     * @throws KokoException if the task list has reached its maximum size
     */
    private static int addTask(Task task, Task[] tasks, int numberOfTasks) throws KokoException {
        if (numberOfTasks >= MAX_TASKS) {
            throw new KokoException("I can only store up to " + MAX_TASKS + " tasks.");
        }

        tasks[numberOfTasks] = task;
        int updatedNumberOfTasks = numberOfTasks + 1;
        printDivider();
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + updatedNumberOfTasks + " tasks in the list.");
        printDivider();
        return updatedNumberOfTasks;
    }
}
