package de.mirkosertic.whobrokeit.core;

import java.io.File;

public class DirectorySourceRepository implements SourceRepository {

    private final File sourceDirectory;

    public DirectorySourceRepository(File aFile) {
        sourceDirectory = aFile;
    }

    @Override
    public DirectorySourceRepository configure(Configuration aConfiguration) {
        return this;
    }

    @Override
    public File locateFileForClass(Class aClass) {
        File theStartDirectory = sourceDirectory;
        String theClassName = aClass.getName();
        int p = theClassName.indexOf("$");
        if (p > 0) {
            theClassName = theClassName.substring(0, p);
        }
        // Find the right directory
        int theFirstPackage = theClassName.indexOf('.');
        while (theFirstPackage > 0) {
            String theSubpackage = theClassName.substring(0, theFirstPackage);
            theClassName = theClassName.substring(theFirstPackage + 1);
            theStartDirectory = new File(theStartDirectory, theSubpackage);
            if (!theStartDirectory.exists()) {
                return null;
            }

            theFirstPackage = theClassName.indexOf('.');
        }
        // Now find the source file
        for (File theFile : theStartDirectory.listFiles()) {
            if (theFile.getName().equals(theClassName + ".java")) {
                return theFile;
            }
        }
        return null;
    }
}
