package de.mirkosertic.whobrokeit.agent;

import de.mirkosertic.whobrokeit.core.ClassLoadingLogger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class ConstructorInvocationAspect {

    public ConstructorInvocationAspect() {
    }

    @Before("initialization(*.new(..)) && !within(ConstructorInvocationAspect)")
    public void afterConstructor(JoinPoint thisJoinPoint) {
        Class theConstructedClass = thisJoinPoint.getSignature().getDeclaringType();
        ClassLoadingLogger theLogger = ClassLoadingLogger.getInstance();
        if (theLogger != null && theLogger.isStatisticsEnabled()) {
            theLogger.getStatistics().recordConstructorUsageOf(theConstructedClass);
        }
    }
}
