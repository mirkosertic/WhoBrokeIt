package de.mirkosertic.whobrokeit.core;

import java.io.File;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClassLoadingLogger {

    private final static Logger LOGGER = Logger.getLogger(ClassLoadingLogger.class.getName());

    private static final ClassLoadingLogger INSTANCE = new ClassLoadingLogger();

    private Statistics statistics;

    public static ClassLoadingLogger getInstance() {
        return INSTANCE;
    }

    private ClassLoadingLogger() {
    }

    public void initializeForRun(Class aTargetClass, Method aTestMethod) {
        statistics = new Statistics(aTargetClass, aTestMethod);
    }

    public boolean isStatisticsEnabled() {
        return statistics != null;
    }

    public Statistics getStatistics() {
        return statistics;
    }

    public void finishRun() {
        try {
            statistics.writeTo(new File("C:\\Temp\\logs"));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error writing log file", e);
        } finally {
            statistics = null;
        }
    }
}
