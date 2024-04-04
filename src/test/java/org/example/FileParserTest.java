package org.example;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.example.filehandling.FileParser.parseDirectory;
import static org.junit.Assert.assertEquals;

public class FileParserTest {

    private Path tempDir;

    @Before
    public void setUp() throws IOException {
        tempDir = createTestDirectoryWithFile();
    }

    @After
    public void tearDown() throws IOException {
        cleanUpTestDirectory(tempDir);
    }

    private Path createTestDirectoryWithFile() throws IOException {
        Path testFilePath = Paths.get("src/test/resources/json/test.json");
        Path tempDir = Files.createTempDirectory("testDir");
        Files.copy(testFilePath, tempDir.resolve(testFilePath.getFileName()));
        return tempDir;
    }

    private void cleanUpTestDirectory(Path tempDir) throws IOException {
        Files.walk(tempDir)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
    }

    @Test
    public void testProcessedRecords() throws IOException {
        Path tempDir = createTestDirectoryWithFile();

        Map<String, AtomicInteger> result = parseDirectory(tempDir, "department", 2);

        AtomicInteger csDeptCount = result.get("Department of Computer Science");
        AtomicInteger electronicsDeptCount = result.get("Department of Electronics");

        assertEquals(2, csDeptCount == null ? 0 : csDeptCount.get());
        assertEquals(2, electronicsDeptCount == null ? 0 : electronicsDeptCount.get());

        cleanUpTestDirectory(tempDir);
    }
}