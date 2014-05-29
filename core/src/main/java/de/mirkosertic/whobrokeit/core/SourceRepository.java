package de.mirkosertic.whobrokeit.core;

import java.io.File;

public interface SourceRepository {

    <T extends SourceRepository> T configure(Configuration aConfiguration);

    File locateFileForClass(Class aClass);
}
