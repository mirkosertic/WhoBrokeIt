package de.mirkosertic.whobrokeit.core;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Statistics {

    private Set<Class> usedClasses;
    private Class targetClass;

    Statistics(Class aTargetClass) {
        usedClasses = new HashSet<>();
        targetClass = aTargetClass;
    }

    public void recordConstructorUsageOf(Class aConstructedClass) {
        usedClasses.add(aConstructedClass);
    }

    public void writeTo(File aSourceFile, SourceRepository aSourceRepository, VersionControlSystem aVersionControlSystem, Method aTestMethod)
            throws IOException {

        List<Class> theUsedClasses = new ArrayList<>();
        for (Class theClass : usedClasses) {
            theUsedClasses.add(theClass);
        }
        Collections.sort(theUsedClasses, new Comparator<Class>() {
            @Override
            public int compare(Class o1, Class o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });

        String theFileName = targetClass.getName() + "_" + aTestMethod.getName();
        File theLogFile = new File(aSourceFile, theFileName.replace('.', '_') + ".log");
        PrintWriter thePrintWriter = new PrintWriter(new FileWriter(theLogFile));
        for (Class theClass : theUsedClasses) {
            File theClassSourceFile = aSourceRepository.locateFileForClass(theClass);
            if (theClassSourceFile != null) {
                Version theVersion = aVersionControlSystem.computeVersionFor(aSourceRepository, theClassSourceFile);
                if (theVersion != null) {
                    thePrintWriter.println(theClass.getName() + ";" + theVersion.computeAsString());
                } else {
                    // Not a versioned file
                    thePrintWriter.println(theClass.getName() + ";");
                }
            } else {
                // No file found, we do nothing
            }
        }
        thePrintWriter.close();
    }
}
