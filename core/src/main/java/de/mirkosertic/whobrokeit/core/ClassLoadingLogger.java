package de.mirkosertic.whobrokeit.core;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClassLoadingLogger {

    private final static Logger LOGGER = Logger.getLogger(ClassLoadingLogger.class.getName());

    private static final ClassLoadingLogger INSTANCE = new ClassLoadingLogger();

    private Statistics statistics;

    private CompositeSourceRepository sourceRepository;

    private VersionControlSystem versionControlSystem;

    public static ClassLoadingLogger getInstance() {
        return INSTANCE;
    }

    private ClassLoadingLogger() {
        sourceRepository = new CompositeSourceRepository();
        ServiceLoader<SourceRepository> theRepositoryLoader = ServiceLoader.load(SourceRepository.class);
        for (Iterator<SourceRepository> theRepIterator = theRepositoryLoader.iterator(); theRepIterator.hasNext();) {
            sourceRepository.add(theRepIterator.next());
        }
        ServiceLoader<VersionControlSystem> theVCSLoader = ServiceLoader.load(VersionControlSystem.class);
        for (Iterator<VersionControlSystem> theVCSIterator = theVCSLoader.iterator(); theVCSIterator.hasNext();) {
            if (versionControlSystem == null) {
                versionControlSystem = theVCSIterator.next();
            } else {
                throw new IllegalStateException("Only one VCS adapter is supported at runtime!");
            }
        }
        if (versionControlSystem == null) {
            throw new IllegalArgumentException("You have to add one VCS adapter implementation");
        }
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
            statistics.writeTo(new File("C:\\Temp\\logs"), sourceRepository, versionControlSystem);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error writing log file", e);
        } finally {
            statistics = null;
        }
    }
}
