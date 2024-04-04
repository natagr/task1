package org.example;

import org.example.util.StatisticsCalculator;
import org.junit.Test;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;

public class StatisticsCalculatorTest {

    @Test
    public void testWithNonEmptyArray() {
        String[] values = {"apple", "banana", "apple", "apple", "banana"};
        Map<String, AtomicInteger> statistics = new ConcurrentHashMap<>();
        StatisticsCalculator.calculateStatistics(values, statistics);

        assertEquals(3, statistics.get("apple").get());
        assertEquals(2, statistics.get("banana").get());
    }

    @Test
    public void testWithConcurrentAccess() throws InterruptedException {
        String[] values = {"concurrency", "test", "concurrency", "test", "concurrency"};
        Map<String, AtomicInteger> statistics = new ConcurrentHashMap<>();

        Thread t1 = new Thread(() -> StatisticsCalculator.calculateStatistics(values, statistics));
        Thread t2 = new Thread(() -> StatisticsCalculator.calculateStatistics(values, statistics));

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        assertEquals(6, statistics.get("concurrency").get());
        assertEquals(4, statistics.get("test").get());
    }
}