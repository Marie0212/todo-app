package de.todoapp.presentation;

import de.todoapp.domain.TaskStatus;
import de.todoapp.service.CategoryCommandService;
import de.todoapp.service.CategoryQueryService;
import de.todoapp.service.TaskCommandService;
import de.todoapp.service.TaskQueryService;

import java.util.Scanner;

public class ConsoleApp {

    private final TaskCommandService taskCommandService;
    private final TaskQueryService taskQueryService;
    private final CategoryCommandService categoryCommandService;
    private final CategoryQueryService categoryQueryService;

    public ConsoleApp(TaskCommandService taskCommandService,
                      TaskQueryService taskQueryService,
                      CategoryCommandService categoryCommandService,
                      CategoryQueryService categoryQueryService) {
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
            System.out.println("0) Beenden");
            System.out.print("> ");

            String choice = sc.nextLine().trim();

            if ("0".equals(choice)) {
                System.out.println("Bye!");
                return;
            }

            if ("1".equals(choice)) {
                createTaskFlow(sc);
            } else if ("2".equals(choice)) {
                listTasksFlow(sc);
            } else if ("3".equals(choice)) {
                markDoneFlow(sc);
            } else {
                System.out.println("Unbekannte Eingabe.");
            }
        }
    }

    private void createTaskFlow(Scanner sc) {
        System.out.println("Create Task ist hier unverändert (US-06).");
        System.out.println("Wenn du willst, erweitern wir das später um Kategorie-Eingabe.");
    }

    private void listTasksFlow(Scanner sc) {
        System.out.print("Filtern? (j/n): ");
        String filter = sc.nextLine().trim().toLowerCase();

        TaskStatus status = null;
        String category = null;

        if (filter.equals("j") || filter.equals("y")) {
            System.out.print("Status (OPEN/DONE oder leer):_toggle): ");
            String statusRaw = sc.nextLine().trim();

            if (!statusRaw.isEmpty()) {
                try {
                    status = TaskStatus.valueOf(statusRaw.toUpperCase());
                } catch (IllegalArgumentException e) {
                    System.out.println("Ungültiger Status. Wird ignoriert.");
                }
            }

            System.out.print("Kategorie (Name oder leer): ");
            category = sc.nextLine().trim();
            if (category.isEmpty()) category = null;
        }

        var tasks = (status == null && category == null)
                ? taskQueryService.listTasks()
                : taskQueryService.listTasksFiltered(status, category);

        if (tasks.isEmpty()) {
            System.out.println("Keine Aufgaben vorhanden.");
            return;
        }

        System.out.println("=== Aufgabenliste ===");
        for (var t : tasks) {
            System.out.println("#" + t.getId() + " [" + t.getStatus() + "] " + t.getTitle()
                    + (t.getCategory() == null ? "" : " (Cat: " + t.getCategory() + ")"));
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
}
