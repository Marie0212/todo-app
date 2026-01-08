package de.todoapp.presentation;

import de.todoapp.domain.Task;
import de.todoapp.service.TaskCommandService;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class ConsoleApp {

    private final TaskCommandService taskCommandService;

    public ConsoleApp(TaskCommandService taskCommandService) {
        this.taskCommandService = taskCommandService;
    }

    public void run() {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println();
            System.out.println("=== ToDo App ===");
            System.out.println("1) Aufgabe anlegen");
            System.out.println("0) Beenden");
            System.out.print("> ");

            String choice = sc.nextLine().trim();

            if ("0".equals(choice)) {
                System.out.println("Bye!");
                return;
            }

            if ("1".equals(choice)) {
                createTaskFlow(sc);
            } else {
                System.out.println("Unbekannte Eingabe.");
            }
        }
    }

    private void createTaskFlow(Scanner sc) {
        System.out.print("Titel (Pflicht): ");
        String title = sc.nextLine();

        System.out.print("Beschreibung (optional): ");
        String description = sc.nextLine();

        System.out.print("Faelligkeitsdatum (optional, YYYY-MM-DD): ");
        String dueRaw = sc.nextLine().trim();

        LocalDate dueDate = null;
        if (!dueRaw.isEmpty()) {
            try {
                dueDate = LocalDate.parse(dueRaw);
            } catch (DateTimeParseException e) {
                System.out.println("Ungültiges Datum. Wird ignoriert.");
            }
        }

        try {
            Task created = taskCommandService.addTask(title, description, dueDate);
            System.out.println("✅ Aufgabe angelegt: #" + created.getId() + " " + created.getTitle());
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Fehler: " + e.getMessage());
        }
    }
}
