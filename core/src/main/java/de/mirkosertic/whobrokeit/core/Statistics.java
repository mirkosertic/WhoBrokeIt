package de.mirkosertic.whobrokeit.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
    private boolean failed;

    Statistics(Class aTargetClass) {
        usedClasses = new HashSet<>();
        targetClass = aTargetClass;
    }

    public void recordConstructorUsageOf(Class aConstructedClass) {
        usedClasses.add(aConstructedClass);
    }
    
    File computeLogFileFor(File aLogDirecotry, Method aTestMethod) {
        File theDirectoryToWriteTo = aLogDirecotry;
        String theClassName = targetClass.getName();
        int p = theClassName.indexOf('.');
        while(p>0) {
            String thePackageName = theClassName.substring(0, p);
            theDirectoryToWriteTo = new File(theDirectoryToWriteTo, thePackageName);
            theDirectoryToWriteTo.mkdirs();
            theClassName = theClassName.substring(p+1);
            p = theClassName.indexOf('.');
        }
        return new File(theDirectoryToWriteTo, aTestMethod.getName()+".log");
    }    

    public List<StatisticEntry> readEntriesFor(File aLogDirectory, Method aTestMethod, VersionControlSystem aVersionControlSystem) throws IOException {

        List<StatisticEntry> theResult = new ArrayList<>();

        File theLogFile = computeLogFileFor(aLogDirectory, aTestMethod);
        try(BufferedReader theReader = new BufferedReader(new FileReader(theLogFile))) {
            String theLine = theReader.readLine();
            theResult.add(StatisticEntry.parse(theLine, aVersionControlSystem));
        }

        return theResult;
    }

    public void writeTo(File aLogDirectory, SourceRepository aSourceRepository, VersionControlSystem aVersionControlSystem, Method aTestMethod)
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

        File theLogFile = computeLogFileFor(aLogDirectory, aTestMethod);
        
        PrintWriter thePrintWriter = new PrintWriter(new FileWriter(theLogFile));
        for (Class theClass : theUsedClasses) {
            File theClassSourceFile = aSourceRepository.locateFileForClass(theClass);
            if (theClassSourceFile != null) {
                Version theVersion = aVersionControlSystem.computeVersionFor(aSourceRepository, theClassSourceFile);
                if (theVersion != null) {
                    StatisticEntry theEntry = new StatisticEntry(theClass.getName(), theVersion);
                    thePrintWriter.println(theEntry.computeAsString());
                }
            } else {
                // No file found, we do nothing
            }
        }
        thePrintWriter.close();
    }

    public void markFailed() {
        failed = true;
    }

    public boolean isFailed() {
        return failed;
    }
}
