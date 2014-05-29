package de.mirkosertic.whobrokeit.agent;

import de.mirkosertic.whobrokeit.core.ClassLoadingLogger;
import de.mirkosertic.whobrokeit.core.Configuration;

import java.lang.instrument.Instrumentation;
import java.util.logging.Logger;

public class Agent {

    private final static Logger LOGGER = Logger.getLogger(Agent.class.getName());

    public static void premain(String aAgentArguments, Instrumentation aInstrumentation) {
        LOGGER.info("WhoBrokeItAgent is starting up");

        Configuration.getInstance().initialize(aAgentArguments);
        ClassLoadingLogger theLogger = ClassLoadingLogger.getInstance();

        // System initialized, now add the logging transformer
        LOGGER.info("ClassLoadingLogger is " + theLogger);

        aInstrumentation.addTransformer(new LoadingLoggerTransformer());
    }
}
