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

    private static ClassLoadingLogger INSTANCE;

    private Statistics statistics;

    private final Configuration configuration;

    public static ClassLoadingLogger getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ClassLoadingLogger(Configuration.getInstance());
        }
        return INSTANCE;
    }

    private ClassLoadingLogger(Configuration aConfiguration) {
        configuration = aConfiguration;
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
        try {

            VersionControlSystem theVersionControlSystem = configuration.getVersionControlSystem();
            SourceRepository theSourceRepository = configuration.getSourceRepository();

            File theLogDirectory = configuration.getLogDir();

            if (statistics.isFailed()) {

                StringWriter theStringWriter = new StringWriter();
                PrintWriter theResultWriter = new PrintWriter(theStringWriter);

                List<StatisticEntry> theOldLog = statistics.readEntriesFor(theLogDirectory, aTestmethod, theVersionControlSystem);
                for (StatisticEntry theEntry : theOldLog) {
                    Class theClass = Class.forName(theEntry.getClazz());
                    File theSourceFile = theSourceRepository.locateFileForClass(theClass);
                    Version theVersionInRepository = theVersionControlSystem.computeVersionFor(theSourceRepository, theSourceFile);
                    if (!theVersionInRepository.isIdenticalWith(theEntry.getVersion())) {
                        theResultWriter.println("Version change detected in " + theSourceFile + " latest change in repository was by " + theVersionInRepository.getAuthor());
                    }
                }
                theResultWriter.flush();

                System.out.println("Possible changes what broke the test : " + theStringWriter.toString());
            } else {
                // Write out the new log
                statistics.writeTo(theLogDirectory, theSourceRepository, theVersionControlSystem, aTestmethod);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error processing log file", e);
        } finally {
            statistics = null;
        }
    }
}
