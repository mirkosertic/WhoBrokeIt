package de.mirkosertic.whobrokeit.svn;

import de.mirkosertic.whobrokeit.core.Version;

class SVNVersion implements Version {

    private final String name;
    private final String author;
    private long commitTime;

    SVNVersion(String aName, String aAuthor, long aCommitTime) {
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

        SVNVersion that = (SVNVersion) aOtherVersion;

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

    static SVNVersion parse(String aVersionStringInfo) {
        int p = aVersionStringInfo.indexOf(":");
        String theCommit = aVersionStringInfo.substring(0, p);
        String theAuthor = aVersionStringInfo.substring(p + 1);
        return new SVNVersion(theCommit, theAuthor, 0);
    }
}