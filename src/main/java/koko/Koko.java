package koko;

/**
 * Controls koko.Koko's application logic.
 */
public class Koko {
    private final Storage storage;
    private TaskList tasks;
    private final Ui ui;

    /**
     * Creates a koko.Koko application.
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
     * Runs koko.Koko's command-line interface.
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

    /**
     * Marks a task as done.
     *
     * @param command the user's mark command
     * @throws KokoException if the task number is invalid or saving fails
     */
    private void markTask(String command) throws KokoException {
        int taskIndex = Parser.getTaskIndex(command, "mark", tasks.size());

        tasks.get(taskIndex).markAsDone();
        storage.save(tasks.getTasks());

        ui.showMarkDone(tasks.get(taskIndex));
    }

    /**
     * Marks a task as not done.
     *
     * @param command the user's unmark command
     * @throws KokoException if the task number is invalid or saving fails
     */
    private void unmarkTask(String command) throws KokoException {
        int taskIndex = Parser.getTaskIndex(command, "unmark", tasks.size());

        tasks.get(taskIndex).markAsNotDone();
        storage.save(tasks.getTasks());

        ui.showMarkNotDone(tasks.get(taskIndex));
    }

    /**
     * Deletes a task.
     *
     * @param command the user's delete command
     * @throws KokoException if the task number is invalid or saving fails
     */
    private void deleteTask(String command) throws KokoException {
        int taskIndex = Parser.getTaskIndex(command, "delete", tasks.size());

        Task removedTask = tasks.remove(taskIndex);
        storage.save(tasks.getTasks());

        ui.showDeletedTask(removedTask, tasks.size());
    }

    /**
     * Adds a todo task.
     *
     * @param command the user's todo command
     * @throws KokoException if the description is missing or saving fails
     */
    private void addTodo(String command) throws KokoException {
        String description = Parser.parseTodo(command);
        addTask(new Todo(description));
    }

    /**
     * Adds a deadline task.
     *
     * @param command the user's deadline command
     * @throws KokoException if the command is invalid or saving fails
     */
    private void addDeadline(String command) throws KokoException {
        Deadline deadline = Parser.parseDeadline(command);
        addTask(deadline);
    }

    /**
     * Adds an event task.
     *
     * @param command the user's event command
     * @throws KokoException if the command is invalid or saving fails
     */
    private void addEvent(String command) throws KokoException {
        Event event = Parser.parseEvent(command);
        addTask(event);
    }

    /**
     * Adds a task and saves the updated task list.
     *
     * @param task the task to add
     * @throws KokoException if saving fails
     */
    private void addTask(Task task) throws KokoException {
        tasks.add(task);
        storage.save(tasks.getTasks());

        ui.showAddedTask(task, tasks.size());
    }

    /**
     * Starts koko.Koko.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        new Koko().run();
    }
}