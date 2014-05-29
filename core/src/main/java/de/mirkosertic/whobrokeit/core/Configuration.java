package de.mirkosertic.whobrokeit.core;

import java.io.File;
import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.logging.Logger;

public class Configuration {

    private final static Logger LOGGER = Logger.getLogger(Configuration.class.getName());

    private static final String PROJECT_DIR="whobrokeit.projectDir";
    private static final String LOG_DIR="whobrokeit.logDir";

    private static Configuration CONFIGURATION;

    private final CompositeSourceRepository sourceRepository;
    private VersionControlSystem versionControlSystem;
    private File projectBaseDir;
    private File logDir;

    private Configuration() {
        sourceRepository = new CompositeSourceRepository();
        ServiceLoader<SourceRepository> theRepositoryLoader = ServiceLoader.load(SourceRepository.class);
        for (Iterator<SourceRepository> theRepIterator = theRepositoryLoader.iterator(); theRepIterator.hasNext(); ) {
            SourceRepository theSourceRepository = theRepIterator.next();
            theSourceRepository.configure(this);
            sourceRepository.add(theSourceRepository);
        }
        ServiceLoader<VersionControlSystem> theVCSLoader = ServiceLoader.load(VersionControlSystem.class);
        for (Iterator<VersionControlSystem> theVCSIterator = theVCSLoader.iterator(); theVCSIterator.hasNext(); ) {
            if (versionControlSystem == null) {
                versionControlSystem = theVCSIterator.next().configure(this);
            } else {
                throw new IllegalStateException("Only one VCS adapter is supported at runtime!");
            }
        }
        if (versionControlSystem == null) {
            throw new IllegalArgumentException("You have to add one VCS adapter implementation");
        }
    }

    public void initialize(String aAgentArguments) {
        String theProjectDir = System.getProperty(PROJECT_DIR);
        if (theProjectDir == null) {
            throw new IllegalArgumentException("System property "+PROJECT_DIR+" not set");
        }
        projectBaseDir = new File(theProjectDir);
        if (!projectBaseDir.exists()) {
            throw new IllegalArgumentException("Project directory "+projectBaseDir+" does not exist");
        }
        String theLogDir = System.getProperty(LOG_DIR);
        if (theLogDir == null) {
            throw new IllegalArgumentException("System property "+LOG_DIR+" not set");
        }
        logDir = new File(theLogDir);
        if (!logDir.exists() || !logDir.canWrite()) {
            throw new IllegalArgumentException("Log directory "+logDir+" does not exist or is not writable");
        }
        LOGGER.info("WhoBrokeIt ProjectDir set to "+projectBaseDir);
        LOGGER.info("WhoBrokeIt LogDir set to "+logDir);
    }

    public SourceRepository getSourceRepository() {
        return sourceRepository;
    }

    public VersionControlSystem getVersionControlSystem() {
        return versionControlSystem;
    }

    public File getProjectBaseDir() {
        return projectBaseDir;
    }

    public File getLogDir() {
        return logDir;
    }

    public static Configuration getInstance() {
        if (CONFIGURATION == null) {
            CONFIGURATION = new Configuration();
        }
        return CONFIGURATION;
    }
}
