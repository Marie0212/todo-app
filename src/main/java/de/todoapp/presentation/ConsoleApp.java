package de.todoapp.presentation;

import de.todoapp.domain.Task;
import de.todoapp.service.CategoryCommandService;
import de.todoapp.service.CategoryQueryService;
import de.todoapp.service.TaskCommandService;
import de.todoapp.service.TaskQueryService;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class ConsoleApp {

    private final TaskCommandService taskCommandService;
    private final TaskQueryService taskQueryService;
    private final CategoryCommandService categoryCommandService;
    private final CategoryQueryService categoryQueryService;

    public ConsoleApp(
            TaskCommandService taskCommandService,
            TaskQueryService taskQueryService,
            CategoryCommandService categoryCommandService,
            CategoryQueryService categoryQueryService
    ) {
        this.taskCommandService = taskCommandService;
        this.taskQueryService = taskQueryService;
        this.categoryCommandService = categoryCommandService;
        this.categoryQueryService = categoryQueryService;
    }

    public void run() {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println();
            System.out.println("=== ToDo App ===");
            System.out.println("1) Aufgabe anlegen");
            System.out.println("2) Aufgaben anzeigen");
            System.out.println("3) Aufgabe erledigen");
            System.out.println("4) Aufgabe löschen");
            System.out.println("5) Kategorie anlegen");
            System.out.println("6) Kategorien anzeigen");
            System.out.println("0) Beenden");
            System.out.print("> ");

            String choice = sc.nextLine().trim();

            if ("0".equals(choice)) {
                System.out.println("Bye!");
                return;
            }

            switch (choice) {
                case "1" -> createTaskFlow(sc);
                case "2" -> listTasksFlow();
                case "3" -> markDoneFlow(sc);
                case "4" -> deleteTaskFlow(sc);
                case "5" -> createCategoryFlow(sc);
                case "6" -> listCategoriesFlow();
                default -> System.out.println("Unbekannte Eingabe.");
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

    private void listTasksFlow() {
        var tasks = taskQueryService.listTasks();
        if (tasks.isEmpty()) {
            System.out.println("Keine Aufgaben vorhanden.");
            return;
        }

        System.out.println("=== Aufgabenliste ===");
        for (var t : tasks) {
            String due = (t.getDueDate() == null) ? "-" : t.getDueDate().toString();
            String cat = (t.getCategoryId() == null) ? "-" : t.getCategoryId().toString();
            System.out.println("#" + t.getId() + " [" + t.getStatus() + "] " + t.getTitle()
                    + " (Due: " + due + ", CategoryId: " + cat + ")");
        }
    }

    private void markDoneFlow(Scanner sc) {
        System.out.print("Welche ID soll erledigt werden? ");
        String raw = sc.nextLine().trim();

        try {
            long id = Long.parseLong(raw);
            taskCommandService.markDone(id);
            System.out.println("✅ Aufgabe #" + id + " erledigt.");
        } catch (NumberFormatException e) {
            System.out.println("❌ Ungültige Zahl.");
        } catch (Exception e) {
            System.out.println("❌ Fehler: " + e.getMessage());
        }
    }

    private void deleteTaskFlow(Scanner sc) {
        System.out.print("Welche ID soll gelöscht werden? ");
        String raw = sc.nextLine().trim();

        try {
            long id = Long.parseLong(raw);
            taskCommandService.deleteTask(id);
            System.out.println("🗑️ Aufgabe #" + id + " gelöscht.");
        } catch (NumberFormatException e) {
            System.out.println("❌ Ungültige Zahl.");
        } catch (Exception e) {
            System.out.println("❌ Fehler: " + e.getMessage());
        }
    }

    private void createCategoryFlow(Scanner sc) {
        System.out.print("Kategorie-Name (Pflicht): ");
        String name = sc.nextLine();

        try {
            var created = categoryCommandService.addCategory(name);
            System.out.println("✅ Kategorie angelegt: #" + created.getId() + " " + created.getName());
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Fehler: " + e.getMessage());
        }
    }

    private void listCategoriesFlow() {
        var categories = categoryQueryService.listCategories();
        if (categories.isEmpty()) {
            System.out.println("Keine Kategorien vorhanden.");
            return;
        }

        System.out.println("=== Kategorien ===");
        for (var c : categories) {
            System.out.println("#" + c.getId() + " " + c.getName());
        }
    }
}