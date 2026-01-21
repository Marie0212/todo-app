package de.todoapp;

public class Main {

    public static void main(String[] args) {
        try {
            Class<?> appClass = Class.forName("de.todoapp.presentation.ConsoleApp");
            Object app = appClass.getDeclaredConstructor().newInstance();
            appClass.getMethod("run").invoke(app);
        } catch (Exception e) {
            System.err.println("Fehler beim Starten der Anwendung:");
            e.printStackTrace();
        }
    }
}
