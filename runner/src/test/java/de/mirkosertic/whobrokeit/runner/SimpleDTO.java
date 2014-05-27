package de.mirkosertic.whobrokeit.runner;

public class SimpleDTO {

    public String greet() {
        //return "hello";
        throw new RuntimeException("This is strange!");
    }
}
