import java.util.Scanner;

/**
 * Starts Koko's command-line interface.
 */
public class Koko {
    /**
     * Displays the welcome message, echoes commands, and stops when the user enters {@code bye}.
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

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();

            if (command.equals("bye")) {
                System.out.println("Bye. Hope to see you again soon!");
                return;
            }

            System.out.println(command);
        }
    }
}
