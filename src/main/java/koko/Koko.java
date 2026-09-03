package koko;

import java.util.List;

/**
 * Controls koko.Koko's application logic.
 */
public class Koko {
    private final Storage storage;
    private TaskList tasks;
    private final Ui ui;

    /**
     * Creates a new Koko chatbot and loads saved tasks.
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
     * Runs the chatbot and processes user commands.
     */
    public void run() {
        ui.showWelcome();
        while (true) {
            String command = ui.readCommand();
            try {
                if (Parser.isBye(command)) {
                    ui.showBye();
                    return;
                }
                if (Parser.isList(command)) {
                    ui.showTasks(tasks);
                } else if (Parser.isCommand(command, "find")) {
                    findTasks(command);
                } else if (Parser.isCommand(command, "mark")) {
                    markTask(command);
                } else if (Parser.isCommand(command, "unmark")) {
                    unmarkTask(command);
                } else if (Parser.isCommand(command, "delete")) {
                    deleteTask(command);
                } else if (Parser.isCommand(command, "todo")) {
                    addTodo(command);
                } else if (Parser.isCommand(command, "deadline")) {
                    addDeadline(command);
                } else if (Parser.isCommand(command, "event")) {
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

    private void markTask(String command) throws KokoException {
        int taskIndex = Parser.getTaskIndex(command, "mark", tasks.size());
        tasks.get(taskIndex).markAsDone();
        storage.save(tasks.getTasks());
        ui.showMarkDone(tasks.get(taskIndex));
    }

    private void unmarkTask(String command) throws KokoException {
        int taskIndex = Parser.getTaskIndex(command, "unmark", tasks.size());
        tasks.get(taskIndex).markAsNotDone();
        storage.save(tasks.getTasks());
        ui.showMarkNotDone(tasks.get(taskIndex));
    }

    private void deleteTask(String command) throws KokoException {
        int taskIndex = Parser.getTaskIndex(command, "delete", tasks.size());
        Task removedTask = tasks.remove(taskIndex);
        storage.save(tasks.getTasks());
        ui.showDeletedTask(removedTask, tasks.size());
    }

    private void addTodo(String command) throws KokoException {
        String description = Parser.parseTodo(command);
        addTask(new Todo(description));
    }

    private void addDeadline(String command) throws KokoException {
        Deadline deadline = Parser.parseDeadline(command);
        addTask(deadline);
    }

    private void addEvent(String command) throws KokoException {
        Event event = Parser.parseEvent(command);
        addTask(event);
    }

    private void addTask(Task task) throws KokoException {
        tasks.add(task);
        storage.save(tasks.getTasks());
        ui.showAddedTask(task, tasks.size());
    }

    private void findTasks(String command) throws KokoException {
        String keyword = command.substring("find".length()).trim();
        if (keyword.isEmpty()) {
            throw new KokoException(
                    "I need a keyword to search for. Try: find book.");
        }
        ui.showMatchingTasks(tasks.find(keyword));
    }

    /**
     * Processes one line of user input and returns koko.Koko's reply as a String.
     * Used by the GUI (does not touch Ui, which prints to the console for the CLI).
     *
     * @param input the user's input line
     * @return koko.Koko's response text
     */
    public String getResponse(String input) {
        try {
            if (Parser.isBye(input)) {
                return "Bye. Hope to see you again soon!";
            }
            if (Parser.isList(input)) {
                return formatTaskList(tasks.getTasks(), "Here are the tasks in your list:");
            } else if (Parser.isCommand(input, "find")) {
                String keyword = input.substring("find".length()).trim();
                if (keyword.isEmpty()) {
                    throw new KokoException(
                            "I need a keyword to search for. Try: find book.");
                }
                return formatTaskList(
                        tasks.find(keyword),
                        "Here are the matching tasks in your list:");
            } else if (Parser.isCommand(input, "mark")) {
                int taskIndex = Parser.getTaskIndex(input, "mark", tasks.size());
                tasks.get(taskIndex).markAsDone();
                storage.save(tasks.getTasks());
                return "Nice! I've marked this task as done:\n  "
                        + tasks.get(taskIndex);
            } else if (Parser.isCommand(input, "unmark")) {
                int taskIndex = Parser.getTaskIndex(input, "unmark", tasks.size());
                tasks.get(taskIndex).markAsNotDone();
                storage.save(tasks.getTasks());
                return "OK, I've marked this task as not done yet:\n  "
                        + tasks.get(taskIndex);
            } else if (Parser.isCommand(input, "delete")) {
                int taskIndex = Parser.getTaskIndex(input, "delete", tasks.size());
                Task removedTask = tasks.remove(taskIndex);
                storage.save(tasks.getTasks());
                return "Noted. I've removed this task:\n  " + removedTask
                        + "\nNow you have " + tasks.size() + " tasks in the list.";
            } else if (Parser.isCommand(input, "todo")) {
                return addTaskForGui(new Todo(Parser.parseTodo(input)));
            } else if (Parser.isCommand(input, "deadline")) {
                return addTaskForGui(Parser.parseDeadline(input));
            } else if (Parser.isCommand(input, "event")) {
                return addTaskForGui(Parser.parseEvent(input));
            } else {
                throw new KokoException("I don't recognise that command. "
                        + "Try todo, deadline, event, list, mark, unmark, or delete.");
            }
        } catch (KokoException exception) {
            return exception.getMessage();
        }
    }

    private String addTaskForGui(Task task) throws KokoException {
        tasks.add(task);
        storage.save(tasks.getTasks());
        return "Got it. I've added this task:\n  " + task
                + "\nNow you have " + tasks.size() + " tasks in the list.";
    }

    private String formatTaskList(List<Task> taskList, String header) {
        StringBuilder sb = new StringBuilder(header);
        for (int i = 0; i < taskList.size(); i++) {
            sb.append("\n").append(i + 1).append(". ").append(taskList.get(i));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        new Koko().run();
    }
}
