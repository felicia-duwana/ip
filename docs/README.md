# koko.Koko User Guide

koko.Koko is a command-line task manager. Tasks are kept while the program is running.

## Adding to-dos

Use `todo` for a task without a date or time.

```
todo borrow book
```

koko.Koko adds it as `[T][ ] borrow book`.

## Adding deadlines

Use `deadline` followed by the task description and `/by` followed by any due-date text. koko.Koko stores the date text exactly as entered; it does not need to be a real date.

```
deadline return book /by Sunday
deadline do homework /by no idea :-p
```

koko.Koko displays a deadline with a `[D]` marker, for example: `[D][ ] return book (by: Sunday)`.

## Adding events

Use `event` followed by the event description, `/from` and a start time, then `/to` and an end time. koko.Koko keeps both time values as text.

```
event project meeting /from Mon 2pm /to 4pm
```

koko.Koko displays an event with an `[E]` marker, for example: `[E][ ] project meeting (from: Mon 2pm to: 4pm)`.

## Listing tasks

Use `list` to show every task. `[ ]` means not done, while `[X]` means done.

```
list
```

## Updating task status

Use a task's number from the `list` output with `mark` or `unmark`.

```
mark 2
unmark 2
```

## Exiting koko.Koko

Use `bye` to close the program.

```
bye
```
