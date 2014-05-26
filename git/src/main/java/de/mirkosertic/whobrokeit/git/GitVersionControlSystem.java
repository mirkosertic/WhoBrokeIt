package de.mirkosertic.whobrokeit.git;

import de.mirkosertic.whobrokeit.core.Version;
import de.mirkosertic.whobrokeit.core.VersionControlSystem;

import java.io.File;

public class GitVersionControlSystem implements VersionControlSystem {

    @Override
    public Version computeVersionFor(File aFile) {
        return new GitVersion("lala");
    }
}
