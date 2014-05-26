package de.mirkosertic.whobrokeit.core;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Statistics {

    private Set<Class> usedClasses;
    private Class targetClass;
    private Method targetMethod;

    Statistics(Class aTargetClass, Method aTestMethod) {
        usedClasses = new HashSet<Class>();
        targetClass = aTargetClass;
        targetMethod = aTestMethod;
    }

    public void recordConstructorUsageOf(Class aConstructedClass) {
        usedClasses.add(aConstructedClass);
    }

    public void writeTo(File aSourceFile) throws IOException {

        List<String> theUsedClasses = new ArrayList<>();
        for (Class theClass : usedClasses) {
            theUsedClasses.add(theClass.getCanonicalName());
        }
        Collections.sort(theUsedClasses);

        String theFileName = targetClass.getName() + "_" + targetMethod.getName();
        File theLogFile = new File(aSourceFile, theFileName.replace('.', '_') + ".log");
        PrintWriter thePrintWriter = new PrintWriter(new FileWriter(theLogFile));
        for (String theClassName : theUsedClasses) {
            thePrintWriter.println(theClassName);
        }
        thePrintWriter.close();
    }
}
