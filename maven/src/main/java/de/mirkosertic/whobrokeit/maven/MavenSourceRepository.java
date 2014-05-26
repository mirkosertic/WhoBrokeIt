package de.mirkosertic.whobrokeit.maven;

import de.mirkosertic.whobrokeit.core.CompositeSourceRepository;
import de.mirkosertic.whobrokeit.core.DirectorySourceRepository;

import java.io.File;

public class MavenSourceRepository extends CompositeSourceRepository {

    public MavenSourceRepository() {
        File theBaseDir = new File("C:\\work\\WhoBrokeIt\\runner");
        File theMainSources = new File(theBaseDir, "src\\main\\java");
        if (theMainSources.exists()) {
            add(new DirectorySourceRepository(theMainSources));
        }
        File theTestSources = new File(theBaseDir, "src\\test\\java");
        if (theTestSources.exists()) {
            add(new DirectorySourceRepository(theTestSources));
        }
    }
}
