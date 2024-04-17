package org.example.util;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * The ParameterValidator class provides static methods for validating application input parameters.
 */
public class ParameterValidator {

    private static final Set<String> allowedAttributes = new HashSet<>(Arrays.asList("department", "credits", "instructor"));

    /**
     * Validates the length of the arguments array to ensure it contains at least two elements.
     *
     * @param args The array of command-line arguments.
     * @return true if the length of args is at least 2; otherwise, false and prints an error message.
     */
    public static boolean validateArgsLength(String[] args) {
        if (args.length < 2) {
            System.out.println("Wrong choice. End of the program.");
            return false;
        }
        return true;
    }

    /**
     * Validates the given folder path to ensure it exists and is a directory.
     *
     * @param folderPath The path of the folder to validate.
     * @return true if the specified folder path exists and is a directory; otherwise, false and prints an error message.
     */
    public static boolean validateFolderPath(String folderPath) {
        File folder = new File(folderPath);
        if (!folder.exists() || !folder.isDirectory()) {
            System.out.println("The specified folder path does not exist or is not a directory.");
            return false;
        }
        return true;
    }

    /**
     * Validates if the provided attribute is one of the allowed attributes.
     *
     * @param attribute The attribute to validate.
     * @return true if the attribute is one of the allowed ones; otherwise, false and prints an error message.
     */
    public static boolean validateAttribute(String attribute) {
        if (!allowedAttributes.contains(attribute)) {
            System.out.println("Invalid attribute. Please use one of the following: department, credits, instructor");
            return false;
        }
        return true;
    }

    /**
     * Validates a combination of command-line arguments including folder path and attribute validity.
     *
     * @param args An array of command-line arguments where args[0] is expected to be a folder path and args[1] an attribute.
     * @return true if all validations pass; otherwise, false.
     */
    public static boolean validateParameters(String[] args) {
        return validateArgsLength(args) &&
                validateFolderPath(args[0]) &&
                validateAttribute(args[1]);
    }
}
