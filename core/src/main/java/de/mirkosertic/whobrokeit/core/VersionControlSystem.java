package de.mirkosertic.whobrokeit.core;

import java.io.File;

public interface VersionControlSystem {

    <T extends VersionControlSystem> T configure(Configuration aConfiguration);

    Version computeVersionFor(SourceRepository aSourceRepository, File aFile);

    Version parseVersionFrom(String aVersionStringInfo);
}
