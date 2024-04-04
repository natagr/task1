package org.example.util;

import java.io.IOException;
import java.nio.file.Paths;

import static org.example.filehandling.FileParser.parseDirectory;

/**
 * This class is designed to measure and compare the performance of parsing directories based on varying numbers of threads.
 */
public class PerformanceMeasurer {

    /**
     * Measures the execution time of parsing a directory for each specified number of threads and prints the results.
     *
     * @param folderPath The path to the folder containing JSON files to be parsed.
     * @param attribute The specific attribute to be searched for in the JSON files.
     * @param threads An array of integers where each integer represents a number of threads to be used for parsing.
     */
    public static void measurePerformance(String folderPath, String attribute, int[] threads) throws IOException, InterruptedException {
        for (int threadCount : threads) {
            long startTime = System.nanoTime();

            parseDirectory(Paths.get(folderPath), attribute, threadCount);

            long endTime = System.nanoTime();
            long duration = (endTime - startTime) / 1_000_000;

            System.out.println("Threads: " + threadCount + " Time: " + duration + " ms\n");

        }
    }
}
