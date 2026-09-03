package koko;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

/**
 * Tests the task management operations in TaskList.
 */
public class TaskListTest {

    /**
     * Tests that adding a task increases the size of the task list
     * and stores the task correctly.
     */
    @Test
    void add_newTask_taskIsStored() {
        TaskList taskList = new TaskList();
        Todo todo = new Todo("read book");

        taskList.add(todo);

        assertEquals(1, taskList.size());
        assertSame(todo, taskList.get(0));
    }

    /**
     * Tests that removing a task decreases the size and returns
     * the correct task.
     */
    @Test
    void remove_existingTask_taskIsReturnedAndRemoved() {
        TaskList taskList = new TaskList();
        Todo todo = new Todo("read book");

        taskList.add(todo);

        Task removedTask = taskList.remove(0);

        assertSame(todo, removedTask);
        assertEquals(0, taskList.size());
    }

    /**
     * Tests that multiple tasks retain their insertion order.
     */
    @Test
    void add_multipleTasks_tasksRemainInInsertionOrder() {
        TaskList taskList = new TaskList();
        Todo first = new Todo("first task");
        Todo second = new Todo("second task");

        taskList.add(first);
        taskList.add(second);

        assertEquals(2, taskList.size());
        assertSame(first, taskList.get(0));
        assertSame(second, taskList.get(1));
    }
}
