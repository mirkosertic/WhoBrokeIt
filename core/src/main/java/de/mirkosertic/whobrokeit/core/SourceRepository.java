package de.mirkosertic.whobrokeit.core;

import java.io.File;

public interface SourceRepository {

    File locateFileForClass(Class aClass);
}
