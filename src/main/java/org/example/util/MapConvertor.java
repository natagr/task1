package org.example.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The MapConvertor class provides a utility method to convert maps with values
 * of type AtomicInteger into maps with values of type Integer.
 */
public class MapConvertor {

    /**
     * Converts a map with AtomicInteger values to a map with Integer values.
     *
     * @param atomicMap The source map with String keys and AtomicInteger values.
     * @return A new Map with String keys and Integer values, where each value
     *         is the integer value of the corresponding AtomicInteger from the
     *         source map.
     */
    public static Map<String, Integer> convertToRegularMap(Map<String, AtomicInteger> atomicMap) {
        Map<String, Integer> regularMap = new HashMap<>();
        atomicMap.forEach((key, atomicValue) -> regularMap.put(key, atomicValue.get()));
        return regularMap;
    }
}
