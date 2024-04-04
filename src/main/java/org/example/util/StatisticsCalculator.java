package org.example.util;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class is designed to accumulate statistics based on a specified attribute.
 */
public class StatisticsCalculator {

    /**
     * Updates a statistics map with counts of unique values extracted from a given array of strings.

     * @param values An array of strings containing the values to be analyzed and counted.
     * @param statistics A map where keys represent unique values found in the input array, and the values
     *                   are AtomicIntegers representing the count of occurrences.
     */
    public static void calculateStatistics(String[] values, Map<String, AtomicInteger> statistics) {
        for (String value : values) {
            statistics.computeIfAbsent(value, k -> new AtomicInteger(0)).incrementAndGet();
        }
    }
}
