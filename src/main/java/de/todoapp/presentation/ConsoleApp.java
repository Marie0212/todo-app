package de.todoapp.presentation;

import de.todoapp.domain.Task;
import de.todoapp.service.CategoryCommandService;
import de.todoapp.service.CategoryQueryService;
import de.todoapp.service.TaskCommandService;
import de.todoapp.service.TaskQueryService;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class ConsoleApp {

    private final TaskCommandService taskCommandService;
    private final TaskQueryService taskQueryService;
    private final CategoryCommandService categoryCommandService;
    private final CategoryQueryService categoryQueryService;

    private final Input in;
    private final Output out;

    public ConsoleApp(
            TaskCommandService taskCommandService,
            TaskQueryService taskQueryService,
            CategoryCommandService categoryCommandService,
            CategoryQueryService categoryQueryService,
            Input in,
            Output out
    ) {
        this.taskCommandService = taskCommandService;
        this.taskQueryService = taskQueryService;
        this.categoryCommandService = categoryCommandService;
        this.categoryQueryService = categoryQueryService;
        this.in = in;
        this.out = out;
    }

    public void run() {
        while (true) {
            out.println("");
            out.println("=== ToDo App ===");
            out.println("1) Aufgabe anlegen");
            out.println("2) Aufgaben anzeigen");
            out.println("3) Aufgabe erledigen");
            out.println("4) Aufgabe löschen");
            out.println("5) Kategorie anlegen");
            out.println("6) Kategorien anzeigen");
            out.println("0) Beenden");
            out.print("> ");

            String choice = in.readLine().trim();

            if ("0".equals(choice)) {
                out.println("Bye!");
                return;
            }

            switch (choice) {
                case "1" -> createTaskFlow();
                case "2" -> listTasksFlow();
                case "3" -> markDoneFlow();
                case "4" -> deleteTaskFlow();
                case "5" -> createCategoryFlow();
                case "6" -> listCategoriesFlow();
                default -> out.println("Unbekannte Eingabe.");
            }
        }
    }

    private void createTaskFlow() {
        out.print("Titel (Pflicht): ");
        String title = in.readLine();

        out.print("Beschreibung (optional): ");
        String description = in.readLine();

        out.print("Faelligkeitsdatum (optional, YYYY-MM-DD): ");
        String dueRaw = in.readLine().trim();

        LocalDate dueDate = null;
        if (!dueRaw.isEmpty()) {
            try {
                dueDate = LocalDate.parse(dueRaw);
            } catch (DateTimeParseException e) {
                out.println("Ungültiges Datum. Wird ignoriert.");
            }
        }

        try {
            Task created = taskCommandService.addTask(title, description, dueDate);
            out.println("✅ Aufgabe angelegt: #" + created.getId() + " " + created.getTitle());
        } catch (IllegalArgumentException e) {
            out.println("❌ Fehler: " + e.getMessage());
        }
    }

    private void listTasksFlow() {
        var tasks = taskQueryService.listTasks();
        if (tasks.isEmpty()) {
            out.println("Keine Aufgaben vorhanden.");
            return;
        }

        out.println("=== Aufgabenliste ===");
        for (var t : tasks) {
            String due = (t.getDueDate() == null) ? "-" : t.getDueDate().toString();
            out.println("#" + t.getId() + " [" + t.getStatus() + "] " + t.getTitle() + " (Due: " + due + ")");
        }
    }

    private void markDoneFlow() {
        out.print("Welche ID soll erledigt werden? ");
        String raw = in.readLine().trim();

        try {
            long id = Long.parseLong(raw);
            taskCommandService.markDone(id);
            out.println("✅ Aufgabe #" + id + " erledigt.");
        } catch (NumberFormatException e) {
            out.println("❌ Ungültige Zahl.");
        } catch (Exception e) {
            out.println("❌ Fehler: " + e.getMessage());
        }
    }

    private void deleteTaskFlow() {
        out.print("Welche ID soll gelöscht werden? ");
        String raw = in.readLine().trim();

        try {
            long id = Long.parseLong(raw);
            taskCommandService.deleteTask(id);
            out.println("🗑️ Aufgabe #" + id + " gelöscht.");
        } catch (NumberFormatException e) {
            out.println("❌ Ungültige Zahl.");
        } catch (Exception e) {
            out.println("❌ Fehler: " + e.getMessage());
        }
    }

    private void createCategoryFlow() {
        out.print("Kategorie-Name (Pflicht): ");
        String name = in.readLine();

        try {
            var created = categoryCommandService.addCategory(name);
            out.println("✅ Kategorie angelegt: #" + created.getId() + " " + created.getName());
        } catch (IllegalArgumentException e) {
            out.println("❌ Fehler: " + e.getMessage());
        }
    }

    private void listCategoriesFlow() {
        var categories = categoryQueryService.listCategories();
        if (categories.isEmpty()) {
            out.println("Keine Kategorien vorhanden.");
            return;
        }

        out.println("=== Kategorien ===");
        for (var c : categories) {
            out.println("#" + c.getId() + " " + c.getName());
        }
    }
}
