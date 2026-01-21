package de.todoapp.presentation;

import java.util.Scanner;

public class ConsoleInput {

    private final Scanner scanner = new Scanner(System.in);

    public String readLine() {
        return scanner.nextLine();
    }

    public String readLine(String prompt) {
        if (prompt != null && !prompt.isBlank()) {
            System.out.print(prompt);
        }
        return scanner.nextLine();
    }

    public int readInt(String prompt) {
        while (true) {
            try {
                return Integer.parseInt(readLine(prompt).trim());
            } catch (NumberFormatException e) {
                System.out.println("Ungültige Zahl.");
            }
        }
    }
}
