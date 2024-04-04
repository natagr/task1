package org.example.ui;

import java.util.Scanner;

/**
 * This class is designed for interacting with the user, specifically for selecting an attribute to be used in statistics generation.
 */
public class UserInterface {

    /**
     * Prompts the user to select an attribute for generating statistics and reads the user's choice.
     *
     * @return A String representing the attribute chosen by the user. If the user selects an invalid option, an empty string is returned.
     */
    public static String getAttributeFromUser() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Select an attribute to generate statistics:");
        System.out.println("1. department");
        System.out.println("2. credits");
        System.out.println("3. instructor");
        System.out.print("Enter the attribute number: \n");

        int choice = scanner.nextInt();
        return switch (choice) {
            case 1 -> "department";
            case 2 -> "credits";
            case 3 -> "instructor";
            default -> "";
        };
    }
}
