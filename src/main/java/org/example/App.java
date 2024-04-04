package org.example;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.example.filehandling.FileParser.parseDirectory;
import static org.example.filehandling.XMLGenerator.generateXML;
import static org.example.ui.UserInterface.getAttributeFromUser;
import static org.example.util.MapConvertor.convertToRegularMap;
import static org.example.util.PerformanceMeasurer.measurePerformance;

public class App  {
    private static final String FOLDER_PATH = "src/main/resources/json";
    private static final int[] threads = {1, 2, 4, 8};
    public static void main(String[] args){
        String attribute = getAttributeFromUser();
        if (attribute.isEmpty()) {
            System.out.println("Wrong choice. End of the program.");
            return;
        }

        try {
            System.out.println("\nPerformance comparison:\n");
            measurePerformance(FOLDER_PATH, attribute, threads);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        try {
            System.out.println("\nThe number of processed and ignored records:\n");
            int coreCount = Runtime.getRuntime().availableProcessors();
            Map<String, AtomicInteger> statistics = parseDirectory(Paths.get(FOLDER_PATH), attribute, coreCount);

            Map<String, Integer> regularIntegerMap = convertToRegularMap(statistics);

            generateXML(regularIntegerMap, attribute);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
