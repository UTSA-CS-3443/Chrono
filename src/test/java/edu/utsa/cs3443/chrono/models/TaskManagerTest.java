package edu.utsa.cs3443.chrono.models;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class TaskManagerTest {

    @Test
    public void testAddAndRemoveTaskCreatesFileAndCleansUp() throws Exception {
        Path dataDir = Paths.get("data");
        Path dataFile = dataDir.resolve("tasks.csv");
        Path backup = dataDir.resolve("tasks.csv.bak");

        // backup ofexisting file if present
        if (Files.exists(dataFile)) {
            Files.move(dataFile, backup, StandardCopyOption.REPLACE_EXISTING);
        }

        try {
            TaskManager tm = new TaskManager();
            int before = tm.getTasks().size();

            tm.addTask("Unit test task", 7);
            assertEquals(before + 1, tm.getTasks().size());

            // ensure the file exists and contains our sanitised description
            assertTrue(Files.exists(dataFile));
            String contents = Files.readString(dataFile);
            assertTrue(contents.contains("Unit test task") || contents.contains("Unit test task".replace(",", ";")));

            // remove the last added task
            Task last = tm.getTasks().get(tm.getTasks().size() - 1);
            tm.removeTask(last);
            assertEquals(before, tm.getTasks().size());

        } finally {
            // remove test artifacts
            if (Files.exists(dataFile)) {
                Files.delete(dataFile);
            }
            // restore backup if it existed
            if (Files.exists(backup)) {
                Files.move(backup, dataFile, StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }
}
