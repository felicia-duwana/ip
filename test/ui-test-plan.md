# Console UI test plan

## Execution details

- Run these cases from the repository root with Java 25. The command uses Java source-file mode, so it compiles and starts the current program for each test case.
- The `Console input` block shows text sent to standard input. It will not appear in captured output because the program is run with piped input.
- Compare the combined standard output and standard error to `Expected output` exactly after converting CRLF line endings to LF. A blank line in an output block is significant.
- Run cases in order. Stop the test session at the first failed or unlaunchable case.

## Test case 1 — Welcome and exit

**Aim:** Verify that the application greets the user and exits politely.

**Console input:**

```text
bye
```

**Run:**

```sh
printf 'bye\n' | java src/main/java/koko.Koko.java 2>&1
```

**Expected output:**

```text
 _  __     _          
| |/ /___ | | _____   
| ' // _ \| |/ / _ \  
| . \ (_) |   < (_) | 
|_|\_\___/|_|\_\___/  

What can I do for you?
Bye. Hope to see you again soon!
```

## Test case 2 — Add and list each task type

**Aim:** Verify that to-dos, deadlines, and events are stored and displayed with their details.

**Console input:**

```text
todo read book
deadline submit report /by Friday
event project meeting /from Mon 2pm /to Mon 3pm
list
bye
```

**Run:**

```sh
printf 'todo read book\ndeadline submit report /by Friday\nevent project meeting /from Mon 2pm /to Mon 3pm\nlist\nbye\n' | java src/main/java/koko.Koko.java 2>&1
```

**Expected output:**

```text
 _  __     _          
| |/ /___ | | _____   
| ' // _ \| |/ / _ \  
| . \ (_) |   < (_) | 
|_|\_\___/|_|\_\___/  

What can I do for you?
____________________________________________________________
Got it. I've added this task:
  [T][ ] read book
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [D][ ] submit report (by: Friday)
Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [E][ ] project meeting (from: Mon 2pm to: Mon 3pm)
Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] read book
2.[D][ ] submit report (by: Friday)
3.[E][ ] project meeting (from: Mon 2pm to: Mon 3pm)
____________________________________________________________
Bye. Hope to see you again soon!
```

## Test case 3 — Change a task's status

**Aim:** Verify that marking and unmarking a task changes the displayed status.

**Console input:**

```text
todo borrow book
mark 1
unmark 1
list
bye
```

**Run:**

```sh
printf 'todo borrow book\nmark 1\nunmark 1\nlist\nbye\n' | java src/main/java/koko.Koko.java 2>&1
```

**Expected output:**

```text
 _  __     _          
| |/ /___ | | _____   
| ' // _ \| |/ / _ \  
| . \ (_) |   < (_) | 
|_|\_\___/|_|\_\___/  

What can I do for you?
____________________________________________________________
Got it. I've added this task:
  [T][ ] borrow book
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
  [T][X] borrow book
____________________________________________________________
____________________________________________________________
OK, I've marked this task as not done yet:
  [T][ ] borrow book
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] borrow book
____________________________________________________________
Bye. Hope to see you again soon!
```

## Test case 4 — Reject malformed commands

**Aim:** Verify that invalid or incomplete commands produce clear guidance and do not stop the application.

**Console input:**

```text
todo
deadline write essay
event gym /from 7pm /to
mark potato
mark 1
unmark
delete
todo plan homework
mark 2
unmark 0
delete 2
something else
bye
```

**Run:**

```sh
printf 'todo\ndeadline write essay\nevent gym /from 7pm /to\nmark potato\nmark 1\nunmark\ndelete\ntodo plan homework\nmark 2\nunmark 0\ndelete 2\nsomething else\nbye\n' | java src/main/java/koko.Koko.java 2>&1
```

**Expected output:**

```text
 _  __     _          
| |/ /___ | | _____   
| ' // _ \| |/ / _ \  
| . \ (_) |   < (_) | 
|_|\_\___/|_|\_\___/  

What can I do for you?
____________________________________________________________
Oops! A to-do needs a description. Try: todo borrow book.
____________________________________________________________
____________________________________________________________
Oops! A deadline needs a description and a /by time. Try: deadline return book /by Friday.
____________________________________________________________
____________________________________________________________
Oops! An event needs a description, /from time, and /to time. Try: event lecture /from Monday 2pm /to Monday 4pm.
____________________________________________________________
____________________________________________________________
Oops! I need a task number to mark. Try: mark 2.
____________________________________________________________
____________________________________________________________
Oops! There are no tasks to mark yet. Add one first.
____________________________________________________________
____________________________________________________________
Oops! I need a task number to unmark. Try: unmark 2.
____________________________________________________________
____________________________________________________________
Oops! I need a task number to delete. Try: delete 2.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [T][ ] plan homework
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Oops! Choose a task number from 1 to 1.
____________________________________________________________
____________________________________________________________
Oops! Choose a task number from 1 to 1.
____________________________________________________________
____________________________________________________________
Oops! Choose a task number from 1 to 1.
____________________________________________________________
____________________________________________________________
Oops! I don't recognise that command. Try todo, deadline, event, list, mark, unmark, or delete.
____________________________________________________________
Bye. Hope to see you again soon!
```

## Test case 5 — Delete a task

**Aim:** Verify that deleting a task reports the removed task and renumbers the remaining list.

**Console input:**

```text
todo read book
deadline return book /by June 6th
event project meeting /from Aug 6th 2pm /to 4pm
todo join sports club
todo borrow book
mark 1
mark 2
mark 4
delete 3
list
bye
```

**Run:**

```sh
printf 'todo read book\ndeadline return book /by June 6th\nevent project meeting /from Aug 6th 2pm /to 4pm\ntodo join sports club\ntodo borrow book\nmark 1\nmark 2\nmark 4\ndelete 3\nlist\nbye\n' | java src/main/java/koko.Koko.java 2>&1
```

**Expected output:**

```text
 _  __     _          
| |/ /___ | | _____   
| ' // _ \| |/ / _ \  
| . \ (_) |   < (_) | 
|_|\_\___/|_|\_\___/  

What can I do for you?
____________________________________________________________
Got it. I've added this task:
  [T][ ] read book
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [D][ ] return book (by: June 6th)
Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [T][ ] join sports club
Now you have 4 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [T][ ] borrow book
Now you have 5 tasks in the list.
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
  [T][X] read book
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
  [D][X] return book (by: June 6th)
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
  [T][X] join sports club
____________________________________________________________
____________________________________________________________
Noted. I've removed this task:
  [E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
Now you have 4 tasks in the list.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][X] read book
2.[D][X] return book (by: June 6th)
3.[T][X] join sports club
4.[T][ ] borrow book
____________________________________________________________
Bye. Hope to see you again soon!
```
