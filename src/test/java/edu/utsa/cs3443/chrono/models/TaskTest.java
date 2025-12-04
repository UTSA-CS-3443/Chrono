package edu.utsa.cs3443.chrono.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class TaskTest {

    @Test
    public void testTaskPropertiesAndSetters() {
        Task t = new Task("Write tests", 5);

        assertEquals("Write tests", t.getDescription());
        assertFalse(t.isComplete());

        t.setIsComplete(true);
        assertTrue(t.isComplete());

        t.setDescription("Updated");
        assertEquals("Updated", t.getDescription());
    }
}
