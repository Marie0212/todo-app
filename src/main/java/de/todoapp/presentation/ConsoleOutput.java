package de.todoapp.presentation;

public class ConsoleOutput implements Output {

    @Override
    public void println(String line) {
        System.out.println(line);
    }

    @Override
    public void print(String text) {
        System.out.print(text);
    }
}
