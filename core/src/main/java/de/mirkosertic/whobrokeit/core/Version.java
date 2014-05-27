package de.mirkosertic.whobrokeit.core;

public interface Version {

    String computeAsString();

    boolean isIdenticalWith(Version aOtherVersion);

    String getAuthor();
}
