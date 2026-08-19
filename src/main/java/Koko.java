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
            } else if (command.equals("todo") || command.startsWith("todo ")) {
                numberOfTasks = addTodo(command, tasks, numberOfTasks);
            } else if (command.equals("deadline") || command.startsWith("deadline ")) {
                numberOfTasks = addDeadline(command, tasks, numberOfTasks);
            } else if (command.equals("event") || command.startsWith("event ")) {
                numberOfTasks = addEvent(command, tasks, numberOfTasks);
            } else {
                System.out.println("I don't understand that command. Try todo, deadline, event, list, mark, or unmark.");
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
     */
    private static void markTask(String command, Task[] tasks, int numberOfTasks) {
        String taskNumberText = command.substring("mark".length()).trim();
        try {
            int taskNumber = Integer.parseInt(taskNumberText);
            int taskIndex = taskNumber - 1;
            if (taskIndex < 0 || taskIndex >= numberOfTasks) {
                System.out.println("Please provide a task number from 1 to " + numberOfTasks + ".");
                return;
            }

            tasks[taskIndex].markAsDone();
            printDivider();
            System.out.println("Nice! I've marked this task as done:");
            System.out.println("  " + tasks[taskIndex]);
            printDivider();
        } catch (NumberFormatException exception) {
            System.out.println("Please provide the number of the task to mark, for example: mark 2");
        }
    }

    /**
     * Marks the task identified by a one-based task number as not done.
     *
     * @param command the user's {@code unmark} command
     * @param tasks the array containing the tasks
     * @param numberOfTasks how many positions in {@code tasks} contain a task
     */
    private static void unmarkTask(String command, Task[] tasks, int numberOfTasks) {
        String taskNumberText = command.substring("unmark".length()).trim();
        try {
            int taskNumber = Integer.parseInt(taskNumberText);
            int taskIndex = taskNumber - 1;
            if (taskIndex < 0 || taskIndex >= numberOfTasks) {
                System.out.println("Please provide a task number from 1 to " + numberOfTasks + ".");
                return;
            }

            tasks[taskIndex].markAsNotDone();
            printDivider();
            System.out.println("OK, I've marked this task as not done yet:");
            System.out.println("  " + tasks[taskIndex]);
            printDivider();
        } catch (NumberFormatException exception) {
            System.out.println("Please provide the number of the task to unmark, for example: unmark 2");
        }
    }

    /**
     * Prints the divider used around command responses.
     */
    private static void printDivider() {
        System.out.println("____________________________________________________________");
    }

    /**
     * Adds a to-do task from a {@code todo DESCRIPTION} command.
     *
     * @param command the user's command
     * @param tasks the array containing the tasks
     * @param numberOfTasks how many positions in {@code tasks} contain a task
     * @return the updated task count
     */
    private static int addTodo(String command, Task[] tasks, int numberOfTasks) {
        String description = command.substring("todo".length()).trim();
        if (description.isEmpty()) {
            System.out.println("Please provide a description, for example: todo borrow book");
            return numberOfTasks;
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
     */
    private static int addDeadline(String command, Task[] tasks, int numberOfTasks) {
        String details = command.substring("deadline".length()).trim();
        int byMarker = details.indexOf(" /by ");
        if (byMarker < 1 || byMarker + " /by ".length() >= details.length()) {
            System.out.println("Please use: deadline DESCRIPTION /by DATE_OR_TIME");
            return numberOfTasks;
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
     */
    private static int addEvent(String command, Task[] tasks, int numberOfTasks) {
        String details = command.substring("event".length()).trim();
        int fromMarker = details.indexOf(" /from ");
        int toMarker = details.indexOf(" /to ");
        if (fromMarker < 1 || toMarker < fromMarker + " /from ".length()
                || toMarker + " /to ".length() >= details.length()) {
            System.out.println("Please use: event DESCRIPTION /from START /to END");
            return numberOfTasks;
        }
        String description = details.substring(0, fromMarker).trim();
        String from = details.substring(fromMarker + " /from ".length(), toMarker).trim();
        String to = details.substring(toMarker + " /to ".length()).trim();
        if (from.isEmpty()) {
            System.out.println("Please use: event DESCRIPTION /from START /to END");
            return numberOfTasks;
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
     */
    private static int addTask(Task task, Task[] tasks, int numberOfTasks) {
        if (numberOfTasks >= MAX_TASKS) {
            System.out.println("Sorry, I can only store up to " + MAX_TASKS + " tasks.");
            return numberOfTasks;
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
