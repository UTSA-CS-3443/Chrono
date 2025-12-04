package edu.utsa.cs3443.chrono.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class TimerModelTest {

    @Test
    public void testCountdownToZero() {
        TimerModel timer = new TimerModel(0, 0, 3);

        assertEquals("00:00:03", timer.getCurrentTime());
        assertEquals(3, timer.getTotalSeconds());

        timer.oneSecondPassed();
        assertEquals("00:00:02", timer.getCurrentTime());

        timer.oneSecondPassed();
        timer.oneSecondPassed();
        assertEquals("00:00:00", timer.getCurrentTime());

        // further calls should not go negative
        timer.oneSecondPassed();
        assertEquals("00:00:00", timer.getCurrentTime());
    }
}

