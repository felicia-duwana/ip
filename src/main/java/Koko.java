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

        String[] tasks = new String[MAX_TASKS];
        boolean[] completedTasks = new boolean[MAX_TASKS];
        int numberOfTasks = 0;
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();

            if (command.equals("bye")) {
                System.out.println("Bye. Hope to see you again soon!");
                return;
            }

            if (command.equals("list")) {
                printTasks(tasks, completedTasks, numberOfTasks);
            } else if (command.equals("mark") || command.startsWith("mark ")) {
                markTask(command, tasks, completedTasks, numberOfTasks);
            } else if (command.equals("unmark") || command.startsWith("unmark ")) {
                unmarkTask(command, tasks, completedTasks, numberOfTasks);
            } else if (numberOfTasks < MAX_TASKS) {
                tasks[numberOfTasks] = command;
                numberOfTasks++;
                System.out.println("added: " + command);
            } else {
                System.out.println("Sorry, I can only store up to " + MAX_TASKS + " tasks.");
            }
        }
    }

    /**
     * Prints all currently stored tasks with user-friendly numbering and their completion status.
     *
     * @param tasks the array containing the tasks
     * @param completedTasks whether the corresponding task has been marked as done
     * @param numberOfTasks how many positions in {@code tasks} contain a task
     */
    private static void printTasks(String[] tasks, boolean[] completedTasks, int numberOfTasks) {
        printDivider();
        System.out.println("Here are the tasks in your list:");
        for (int index = 0; index < numberOfTasks; index++) {
            System.out.println((index + 1) + "." + getStatus(completedTasks[index]) + " " + tasks[index]);
        }
        printDivider();
    }

    /**
     * Marks the task identified by a one-based task number as done.
     *
     * @param command the user's {@code mark} command
     * @param tasks the array containing the tasks
     * @param completedTasks whether the corresponding task has been marked as done
     * @param numberOfTasks how many positions in {@code tasks} contain a task
     */
    private static void markTask(String command, String[] tasks, boolean[] completedTasks, int numberOfTasks) {
        String taskNumberText = command.substring("mark".length()).trim();
        try {
            int taskNumber = Integer.parseInt(taskNumberText);
            int taskIndex = taskNumber - 1;
            if (taskIndex < 0 || taskIndex >= numberOfTasks) {
                System.out.println("Please provide a task number from 1 to " + numberOfTasks + ".");
                return;
            }

            completedTasks[taskIndex] = true;
            printDivider();
            System.out.println("Nice! I've marked this task as done:");
            System.out.println("  " + getStatus(true) + " " + tasks[taskIndex]);
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
     * @param completedTasks whether the corresponding task has been marked as done
     * @param numberOfTasks how many positions in {@code tasks} contain a task
     */
    private static void unmarkTask(String command, String[] tasks, boolean[] completedTasks, int numberOfTasks) {
        String taskNumberText = command.substring("unmark".length()).trim();
        try {
            int taskNumber = Integer.parseInt(taskNumberText);
            int taskIndex = taskNumber - 1;
            if (taskIndex < 0 || taskIndex >= numberOfTasks) {
                System.out.println("Please provide a task number from 1 to " + numberOfTasks + ".");
                return;
            }

            completedTasks[taskIndex] = false;
            printDivider();
            System.out.println("OK, I've marked this task as not done yet:");
            System.out.println("  " + getStatus(false) + " " + tasks[taskIndex]);
            printDivider();
        } catch (NumberFormatException exception) {
            System.out.println("Please provide the number of the task to unmark, for example: unmark 2");
        }
    }

    /**
     * Returns the display marker for a task's completion state.
     *
     * @param isCompleted whether the task has been marked as done
     * @return {@code [X]} for completed tasks or {@code [ ]} otherwise
     */
    private static String getStatus(boolean isCompleted) {
        return isCompleted ? "[X]" : "[ ]";
    }

    /**
     * Prints the divider used around command responses.
     */
    private static void printDivider() {
        System.out.println("____________________________________________________________");
    }
}
