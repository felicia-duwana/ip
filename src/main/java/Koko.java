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
        int numberOfTasks = 0;
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();

            if (command.equals("bye")) {
                System.out.println("Bye. Hope to see you again soon!");
                return;
            }

            if (command.equals("list")) {
                printTasks(tasks, numberOfTasks);
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
     * Prints all currently stored tasks with user-friendly numbering.
     *
     * @param tasks the array containing the tasks
     * @param numberOfTasks how many positions in {@code tasks} contain a task
     */
    private static void printTasks(String[] tasks, int numberOfTasks) {
        for (int index = 0; index < numberOfTasks; index++) {
            System.out.println((index + 1) + ". " + tasks[index]);
        }
    }
}
