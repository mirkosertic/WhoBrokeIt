package de.mirkosertic.whobrokeit.core;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Statistics {

    private Set<Class> usedClasses;

    Statistics() {
        usedClasses = new HashSet<Class>();
    }

    public void recordConstructorUsageOf(Class aConstructedClass) {
        usedClasses.add(aConstructedClass);
    }

    public Set<Class> getUsedClasses() {
        return Collections.unmodifiableSet(usedClasses);
    }
}
