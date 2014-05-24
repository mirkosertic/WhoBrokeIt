package de.mirkosertic.whobrokeit.agent;

import java.lang.instrument.Instrumentation;
import java.util.logging.Logger;

public class Agent {

    private final static Logger LOGGER = Logger.getLogger(Agent.class.getName());

    public static void premain(String aAgentArguments, Instrumentation aInstrumentation) {
        LOGGER.info("WhoBrokeItAgent is starting up");
        aInstrumentation.addTransformer(new LoadingLoggerTransformer());
    }
}
