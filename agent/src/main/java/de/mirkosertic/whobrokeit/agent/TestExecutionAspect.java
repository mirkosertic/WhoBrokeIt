package de.mirkosertic.whobrokeit.agent;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import de.mirkosertic.whobrokeit.core.ClassLoadingLogger;
import org.aspectj.lang.reflect.MethodSignature;

@Aspect
public class TestExecutionAspect {

    @Around("execution(@org.junit.Test * *.*(..))")
    public Object logTestExecution(ProceedingJoinPoint aJoinPoint) throws Throwable {
        ClassLoadingLogger theLogger = ClassLoadingLogger.getInstance();
        if (theLogger != null) {
            try {
                JoinPoint.StaticPart theStaticPart = aJoinPoint.getStaticPart();
                MethodSignature theMethodSignature = (MethodSignature) theStaticPart.getSignature();
                theLogger.initializeForRun(theMethodSignature.getDeclaringType(), theMethodSignature.getMethod());
                return aJoinPoint.proceed();
            } finally {
                theLogger.finishRun();
            }
        }
        return aJoinPoint.proceed();
    }
}
