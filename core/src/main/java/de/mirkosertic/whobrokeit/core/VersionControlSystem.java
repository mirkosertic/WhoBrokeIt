package de.mirkosertic.whobrokeit.core;

import java.io.File;

public interface VersionControlSystem {

    Version computeVersionFor(SourceRepository aSourceRepository, File aFile);
}