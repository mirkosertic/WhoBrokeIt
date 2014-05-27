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

    @Override
    public String getAuthor() {
        return author;
    }

    @Override
    public boolean isIdenticalWith(Version aOtherVersion) {
        if (this == aOtherVersion) return true;
        if (aOtherVersion == null || getClass() != aOtherVersion.getClass()) return false;

        GitVersion that = (GitVersion) aOtherVersion;

        if (!author.equals(that.author)) return false;
        if (!name.equals(that.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + author.hashCode();
        return result;
    }

    static GitVersion parse(String aVersionStringInfo) {
        int p = aVersionStringInfo.indexOf(":");
        String theCommit = aVersionStringInfo.substring(0, p);
        String theAuthor = aVersionStringInfo.substring(p + 1);
        return new GitVersion(theCommit, theAuthor, 0);
    }
}
