package de.mirkosertic.whobrokeit.core;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
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
        for (Iterator<SourceRepository> theRepIterator = theRepositoryLoader.iterator(); theRepIterator.hasNext(); ) {
            sourceRepository.add(theRepIterator.next());
        }
        ServiceLoader<VersionControlSystem> theVCSLoader = ServiceLoader.load(VersionControlSystem.class);
        for (Iterator<VersionControlSystem> theVCSIterator = theVCSLoader.iterator(); theVCSIterator.hasNext(); ) {
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

    public void initializeForRun(Class aTargetClass) {
        if (statistics == null) {
            statistics = new Statistics(aTargetClass);
        }
    }

    public void exceptionExpectedButNotThrown(Class<?> aExceptionClass) {
        if (statistics != null) {
            statistics.markFailed();
        }
    }

    public void unexpectedExceptionThrown(Exception aException) {
        if (statistics != null) {
            statistics.markFailed();
        }
    }

    public boolean isStatisticsEnabled() {
        return statistics != null;
    }

    public Statistics getStatistics() {
        return statistics;
    }

    public void finishRun(Method aTestmethod) {
        File theLogDirectory = new File("C:\\Temp\\logs");
        try {
            if (statistics.isFailed()) {

                StringWriter theStringWriter = new StringWriter();
                PrintWriter theResultWriter = new PrintWriter(theStringWriter);

                List<StatisticEntry> theOldLog = statistics.readEntriesFor(theLogDirectory, aTestmethod, versionControlSystem);
                for (StatisticEntry theEntry : theOldLog) {
                    Class theClass = Class.forName(theEntry.getClazz());
                    File theSourceFile = sourceRepository.locateFileForClass(theClass);
                    Version theVersionInRepository = versionControlSystem.computeVersionFor(sourceRepository, theSourceFile);
                    if (!theVersionInRepository.isIdenticalWith(theEntry.getVersion())) {
                        theResultWriter.println("Version change detected in " + theSourceFile + " latest change in repository was by " + theVersionInRepository.getAuthor());
                    }
                }
                theResultWriter.flush();

                System.out.println("Possible changes what broke the test : " + theStringWriter.toString());
            } else {
                // Write out the new log
                statistics.writeTo(theLogDirectory, sourceRepository, versionControlSystem, aTestmethod);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error processing log file", e);
        } finally {
            statistics = null;
        }
    }
}
