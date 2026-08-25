package koko;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

/**
 * Tests the command parsing logic in Parser.
 */
public class ParserTest {

    /**
     * Tests that a valid deadline is parsed into the correct description and date/time.
     */
    @Test
    void parseDeadline_validDeadline_returnsCorrectDeadline() throws KokoException {
        Deadline deadline = Parser.parseDeadline(
                "deadline return book /by 2026-09-01 1800");

        assertEquals("return book", deadline.getDescription());
        assertEquals(
                LocalDateTime.of(2026, 9, 1, 18, 0),
                deadline.getBy());
    }

    /**
     * Tests that a deadline without a /by section is rejected.
     */
    @Test
    void parseDeadline_missingBy_throwsException() {
        assertThrows(
                KokoException.class,
                () -> Parser.parseDeadline("deadline return book"));
    }

    /**
     * Tests that an invalid date format is rejected.
     */
    @Test
    void parseDeadline_invalidDate_throwsException() {
        assertThrows(
                KokoException.class,
                () -> Parser.parseDeadline(
                        "deadline return book /by 01-09-2026 1800"));
    }

    /**
     * Tests that an invalid time is rejected.
     */
    @Test
    void parseDeadline_invalidTime_throwsException() {
        assertThrows(
                KokoException.class,
                () -> Parser.parseDeadline(
                        "deadline return book /by 2026-09-01 2500"));
    }

    /**
     * Tests that an empty deadline description is rejected.
     */
    @Test
    void parseDeadline_emptyDescription_throwsException() {
        assertThrows(
                KokoException.class,
                () -> Parser.parseDeadline(
                        "deadline /by 2026-09-01 1800"));
    }
}