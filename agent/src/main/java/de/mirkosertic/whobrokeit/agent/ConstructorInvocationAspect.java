package de.mirkosertic.whobrokeit.agent;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import de.mirkosertic.whobrokeit.core.ClassLoadingLogger;

@Aspect
public class ConstructorInvocationAspect {

    @Before("initialization(*.new(..)) && !within(ConstructorInvocationAspect)")
    public void afterConstructor(JoinPoint thisJoinPoint) {
        try {
            org.aspectj.lang.Signature theSignature = thisJoinPoint.getSignature();
            if (theSignature == null) {
                theSignature = thisJoinPoint.getStaticPart().getSignature();
            }
            if (theSignature != null) {
                Class theConstructedClass = theSignature.getDeclaringType();
                ClassLoadingLogger theLogger = ClassLoadingLogger.getInstance();
                if (theLogger != null && theLogger.isStatisticsEnabled()) {
                    theLogger.getStatistics().recordConstructorUsageOf(theConstructedClass);
                }
            }
        } catch (NullPointerException e) {
            // .getSignature() sometimes gives us a NPE during initialization pointcut
            // we will skip this
        }
    }
}
