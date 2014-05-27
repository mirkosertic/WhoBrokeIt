package de.mirkosertic.whobrokeit.git;

import de.mirkosertic.whobrokeit.core.Version;

class GitVersion implements Version {

    private final String name;
    private final String author;
    private int commitTime;

    GitVersion(String aName, String aAuthor, int aCommitTime) {
        name = aName;
        author = aAuthor;
        commitTime = aCommitTime;
    }

    @Override
    public String computeAsString() {
        return name + ":" + author;
    }
}
