import java.util.Scanner;

/**
 * Handles interactions between Koko and the user.
 */
public class Ui {
    private final Scanner scanner;

    /**
     * Creates a UI using standard input.
     */
    public Ui() {
        scanner = new Scanner(System.in);
    }

    /**
     * Displays Koko's welcome message.
     */
    public void showWelcome() {
        String banner = " _  __     _          \n"
                + "| |/ /___ | | _____   \n"
                + "| ' // _ \\| |/ / _ \\  \n"
                + "| . \\ (_) |   < (_) | \n"
                + "|_|\\_\\___/|_|\\_\\___/  \n";

        System.out.println(banner);
        System.out.println("What can I do for you?");
    }

    /**
     * Reads a command entered by the user.
     *
     * @return the user's command, with leading and trailing whitespace removed
     */
    public String readCommand() {
        if (scanner.hasNextLine()) {
            return scanner.nextLine().trim();
        }

        return "bye";
    }

    /**
     * Displays the goodbye message.
     */
    public void showBye() {
        System.out.println("Bye. Hope to see you again soon!");
    }

    /**
     * Displays all tasks in the task list.
     *
     * @param tasks the task list to display
     */
    public void showTasks(TaskList tasks) {
        showDivider();
        System.out.println("Here are the tasks in your list:");

        for (int index = 0; index < tasks.size(); index++) {
            System.out.println((index + 1) + "." + tasks.get(index));
        }

        showDivider();
    }

    /**
     * Displays a message confirming that a task was marked as done.
     *
     * @param task the task that was marked as done
     */
    public void showMarkDone(Task task) {
        showDivider();
        System.out.println("Nice! I've marked this task as done:");
        System.out.println("  " + task);
        showDivider();
    }

    /**
     * Displays a message confirming that a task was marked as not done.
     *
     * @param task the task that was marked as not done
     */
    public void showMarkNotDone(Task task) {
        showDivider();
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println("  " + task);
        showDivider();
    }

    /**
     * Displays a message confirming that a task was deleted.
     *
     * @param task the task that was removed
     * @param remainingTasks the number of tasks remaining
     */
    public void showDeletedTask(Task task, int remainingTasks) {
        showDivider();
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + remainingTasks + " tasks in the list.");
        showDivider();
    }

    /**
     * Displays a message confirming that a task was added.
     *
     * @param task the task that was added
     * @param numberOfTasks the number of tasks now in the list
     */
    public void showAddedTask(Task task, int numberOfTasks) {
        showDivider();
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + numberOfTasks + " tasks in the list.");
        showDivider();
    }

    /**
     * Displays an error message.
     *
     * @param message the error message
     */
    public void showError(String message) {
        showDivider();
        System.out.println("Oops! " + message);
        showDivider();
    }

    /**
     * Displays the standard divider.
     */
    public void showDivider() {
        System.out.println("____________________________________________________________");
    }
}