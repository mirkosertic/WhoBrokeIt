package de.mirkosertic.whobrokeit.maven;

import de.mirkosertic.whobrokeit.core.CompositeSourceRepository;
import de.mirkosertic.whobrokeit.core.Configuration;
import de.mirkosertic.whobrokeit.core.DirectorySourceRepository;

import java.io.File;

public class MavenSourceRepository extends CompositeSourceRepository {

    public MavenSourceRepository() {
    }

    @Override
    public MavenSourceRepository configure(Configuration aConfiguration) {
        File theBaseDir = aConfiguration.getProjectBaseDir();
        File theMainSources = new File(theBaseDir, "src\\main\\java");
        if (theMainSources.exists()) {
            add(new DirectorySourceRepository(theMainSources).configure(aConfiguration));
        }
        File theTestSources = new File(theBaseDir, "src\\test\\java");
        if (theTestSources.exists()) {
            add(new DirectorySourceRepository(theTestSources).configure(aConfiguration));
        }
        return this;
    }
}
