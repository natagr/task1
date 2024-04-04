package org.example.filehandling;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import lombok.SneakyThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static org.example.util.StatisticsCalculator.calculateStatistics;

/**
 * The FileParser class is designed to parse directories for JSON files asynchronously using a specified number of threads.
 * It processes JSON files to gather statistics based on a specific attribute within those files. The class tracks the number
 * of processed and ignored records (JSON objects) based on the presence of the specified attribute.
 */
public class FileParser {

    /**
     * Parses the given directory for JSON files asynchronously using a fixed number of threads, collecting statistics for a specified attribute.
     *
     * @param directoryPath The path to the directory containing JSON files to be parsed.
     * @param attribute The JSON attribute to collect statistics on.
     * @param threads The number of threads to use for asynchronous processing.
     * @return A Map with attribute values as keys and their occurrence counts as values.
     */
    public static Map<String, AtomicInteger> parseDirectory(Path directoryPath, String attribute, Integer threads) throws IOException {
        ExecutorService executorService = Executors.newFixedThreadPool(threads);
        Map<String, AtomicInteger> statistics = new ConcurrentHashMap<>();
        AtomicInteger processedRecords = new AtomicInteger(0);
        AtomicInteger ignoredRecords = new AtomicInteger(0);

        try (Stream<Path> paths = Files.walk(directoryPath)) {
            paths.filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".json"))
                    .forEach(path -> executorService.submit(() -> processFile(path, attribute, statistics, processedRecords, ignoredRecords)));
        } finally {
            shutdownAndAwaitTermination(executorService);
        }

        printStatistics(processedRecords, ignoredRecords);
        return statistics;
    }

    /**
     * Processes a single file, updating the provided statistics and record counts.
     *
     * @param path The path to the JSON file to process.
     * @param attribute The JSON attribute to collect statistics on.
     * @param statistics The map of attribute values and their occurrence counts.
     * @param processedRecords The count of records processed that contain the specified attribute.
     * @param ignoredRecords The count of records ignored because they do not contain the specified attribute.
     */
    private static void processFile(Path path, String attribute, Map<String, AtomicInteger> statistics, AtomicInteger processedRecords, AtomicInteger ignoredRecords) {
        JsonFactory jsonFactory = new JsonFactory();
        try (JsonParser jsonParser = jsonFactory.createParser(path.toFile())) {
            parseJson(jsonParser, attribute, statistics, processedRecords, ignoredRecords);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Parses the content of a JSON file, updating statistics and record counts based on the presence of the specified attribute.
     *
     * @param jsonParser The JSON parser for the file.
     * @param attribute The attribute to collect statistics on.
     * @param statistics The map of attribute values and their occurrence counts.
     * @param processedRecords The count of processed records.
     * @param ignoredRecords The count of ignored records.
     */
    private static void parseJson(JsonParser jsonParser, String attribute, Map<String, AtomicInteger> statistics, AtomicInteger processedRecords, AtomicInteger ignoredRecords) throws IOException {
        while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
            if (jsonParser.getCurrentToken() == JsonToken.START_OBJECT) {
                boolean hasRelevantField = false;
                while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
                    String fieldName = jsonParser.currentName();
                    if (fieldName != null && fieldName.equals(attribute)) {
                        hasRelevantField = true;
                        jsonParser.nextToken();
                        String[] values = Arrays.stream(jsonParser.getText().split(","))
                                .map(String::trim)
                                .toArray(String[]::new);
                        calculateStatistics(values, statistics);
                    }
                }
                if (hasRelevantField) {
                    processedRecords.incrementAndGet();
                } else {
                    ignoredRecords.incrementAndGet();
                }
            }
        }
    }

    /**
     * Shuts down the ExecutorService and awaits termination of all tasks.
     *
     * @param pool The ExecutorService to shut down.
     */
    @SneakyThrows
    private static void shutdownAndAwaitTermination(ExecutorService pool) {
        pool.shutdown();
        if (!pool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS)) {
            pool.shutdownNow();
        }
    }

    /**
     * Prints the statistics of processed and ignored records.
     *
     * @param processedRecords The count of processed records.
     * @param ignoredRecords The count of ignored records.
     */
    private static void printStatistics(AtomicInteger processedRecords, AtomicInteger ignoredRecords) {
        System.out.println("ProcessedRecords: " + processedRecords.get());
        System.out.println("IgnoredRecords: " + ignoredRecords.get());
    }
}
