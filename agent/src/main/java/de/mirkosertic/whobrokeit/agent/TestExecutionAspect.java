package de.mirkosertic.whobrokeit.agent;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import de.mirkosertic.whobrokeit.core.ClassLoadingLogger;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.Test;
import org.junit.Before;
import org.junit.BeforeClass;

@Aspect
public class TestExecutionAspect {

    @Around("execution(@org.junit.BeforeClass * *.*(..))")
    public Object beforeTestClassExecution(ProceedingJoinPoint aJoinPoint) throws Throwable{
        ClassLoadingLogger theLogger = ClassLoadingLogger.getInstance();
        if (theLogger != null) {
            JoinPoint.StaticPart theStaticPart = aJoinPoint.getStaticPart();
            MethodSignature theMethodSignature = (MethodSignature) theStaticPart.getSignature();
            theLogger.initializeForRun(theMethodSignature.getDeclaringType());
        }
        return aJoinPoint.proceed();
    }

    @Around("execution(@org.junit.Before * *.*(..))")
    public Object beforeTestMethodExecution(ProceedingJoinPoint aJoinPoint) throws Throwable{
        ClassLoadingLogger theLogger = ClassLoadingLogger.getInstance();
        if (theLogger != null) {
            JoinPoint.StaticPart theStaticPart = aJoinPoint.getStaticPart();
            MethodSignature theMethodSignature = (MethodSignature) theStaticPart.getSignature();
            theLogger.initializeForRun(theMethodSignature.getDeclaringType());
        }
        return aJoinPoint.proceed();
    }

    @Around("execution(@org.junit.Test * *.*(..))")
    public Object logTestExecution(ProceedingJoinPoint aJoinPoint) throws Throwable {
        ClassLoadingLogger theLogger = ClassLoadingLogger.getInstance();
        if (theLogger != null) {
            JoinPoint.StaticPart theStaticPart = aJoinPoint.getStaticPart();
            MethodSignature theMethodSignature = (MethodSignature) theStaticPart.getSignature();
            Test theTestAnnotation = theMethodSignature.getMethod().getAnnotation(Test.class);
            Class<?> theExceptionClass = null;
            if (theTestAnnotation.expected() != Test.None.class) {
                theExceptionClass = theTestAnnotation.expected();
            }
            try {

                theLogger.initializeForRun(theMethodSignature.getDeclaringType());
                Object theResult = aJoinPoint.proceed();

                if (theExceptionClass != null) {
                    // Exception was expected
                    theLogger.exceptionExpectedButNotThrown(theExceptionClass);
                }

                return theResult;
            } catch (Exception e) {

                if (theExceptionClass == null) {
                    theLogger.unexpectedExceptionThrown(e);
                } else {
                    if (!theExceptionClass.isAssignableFrom(e.getClass())) {
                        theLogger.unexpectedExceptionThrown(e);
                    }
                }
            } finally {
                theLogger.finishRun(theMethodSignature.getMethod());
            }
        }
        return aJoinPoint.proceed();
    }
}
