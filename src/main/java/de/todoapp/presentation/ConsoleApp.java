package de.todoapp.presentation;

import de.todoapp.domain.Task;
import de.todoapp.service.TaskCommandService;
import de.todoapp.service.TaskQueryService;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class ConsoleApp {

    private final TaskCommandService taskCommandService;
    private final TaskQueryService taskQueryService;

    public ConsoleApp(TaskCommandService taskCommandService, TaskQueryService taskQueryService) {
        this.taskCommandService = taskCommandService;
        this.taskQueryService = taskQueryService;
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
                listTasksFlow();
            } else if ("3".equals(choice)) {
                markDoneFlow(sc);
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

    private void listTasksFlow() {
        var tasks = taskQueryService.listTasks();
        if (tasks.isEmpty()) {
            System.out.println("Keine Aufgaben vorhanden.");
            return;
        }

        System.out.println("=== Aufgabenliste ===");
        for (var t : tasks) {
            String due = (t.getDueDate() == null) ? "-" : t.getDueDate().toString();
            System.out.println("#" + t.getId() + " [" + t.getStatus() + "] " + t.getTitle() + " (Due: " + due + ")");
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
