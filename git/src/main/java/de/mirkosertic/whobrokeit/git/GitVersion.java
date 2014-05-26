package de.mirkosertic.whobrokeit.git;

import de.mirkosertic.whobrokeit.core.Version;

class GitVersion implements Version {

    private final String version;

    GitVersion(String aVersion) {
        version = aVersion;
    }

    @Override
    public String computeAsString() {
        return version;
    }
}
