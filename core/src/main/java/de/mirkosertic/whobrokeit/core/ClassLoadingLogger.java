package de.mirkosertic.whobrokeit.core;

public class ClassLoadingLogger {

    private static final ClassLoadingLogger INSTANCE = new ClassLoadingLogger();

    private Statistics statistics;

    public static ClassLoadingLogger getInstance() {
        return INSTANCE;
    }

    private ClassLoadingLogger() {
    }

    public void initializeForRun() {
        statistics = new Statistics();
    }

    public boolean isStatisticsEnabled() {
        return statistics != null;
    }

    public Statistics getStatistics() {
        return statistics;
    }

    public void finishRun() {
        System.out.println("Used classes in this run");
        System.out.println("========================");
        for (Class theClass : statistics.getUsedClasses()) {
            System.out.println(theClass.getName());
        }
        statistics = null;
    }
}
