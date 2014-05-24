package de.mirkosertic.whobrokeit.runner;

import de.mirkosertic.whobrokeit.core.ClassLoadingLogger;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

public class WhoBrokeItRunner extends BlockJUnit4ClassRunner {

    public WhoBrokeItRunner(Class<?> aClass) throws InitializationError {
        super(aClass);
    }

    @Override
    protected Statement methodBlock(final FrameworkMethod aMethod) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                ClassLoadingLogger theLogger = ClassLoadingLogger.getInstance();
                try {
                    theLogger.initializeForRun();
                    WhoBrokeItRunner.super.methodBlock(aMethod).evaluate();
                } finally {
                    theLogger.finishRun();
                }
            }
        };
    }
}
