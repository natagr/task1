package org.example;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.example.filehandling.FileParser.parseDirectory;
import static org.example.filehandling.XMLGenerator.generateXML;
import static org.example.util.MapConvertor.convertToRegularMap;
import static org.example.util.ParameterValidator.validateParameters;
import static org.example.util.PerformanceMeasurer.measurePerformance;

public class App  {
    private static final int[] threads = {1, 2, 4, 8};
    public static void main(String[] args){
        if (!validateParameters(args)) {
            return;
        }

        String folderPath = args[0];
        String attribute = args[1];

        try {
            System.out.println("\nPerformance comparison:\n");
            measurePerformance(folderPath, attribute, threads);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        try {
            System.out.println("\nThe number of processed and ignored records:\n");
            int coreCount = Runtime.getRuntime().availableProcessors();
            Map<String, AtomicInteger> statistics = parseDirectory(Paths.get(folderPath), attribute, coreCount);

            Map<String, Integer> regularIntegerMap = convertToRegularMap(statistics);

            generateXML(regularIntegerMap, attribute);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
