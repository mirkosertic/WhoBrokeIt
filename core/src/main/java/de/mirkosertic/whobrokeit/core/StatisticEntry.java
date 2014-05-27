package de.mirkosertic.whobrokeit.core;

public class StatisticEntry {

    private final String clazz;
    private final Version version;

    StatisticEntry(String aClass, Version aVersion) {
        clazz = aClass;
        version = aVersion;
    }

    public String getClazz() {
        return clazz;
    }

    public Version getVersion() {
        return version;
    }

    public String computeAsString() {
        return clazz + ";" + version.computeAsString();
    }

    public static StatisticEntry parse(String aLine, VersionControlSystem aVersionControlSystem) {
        int p = aLine.indexOf(';');
        String theClassName = aLine.substring(0, p);
        String theVersionInfo = aLine.substring(p + 1);
        return new StatisticEntry(theClassName, aVersionControlSystem.parseVersionFrom(theVersionInfo));
    }
}
