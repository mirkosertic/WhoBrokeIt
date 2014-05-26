package de.mirkosertic.whobrokeit.core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CompositeSourceRepository implements SourceRepository {

    private List<SourceRepository> repositories;

    public CompositeSourceRepository() {
        repositories = new ArrayList<>();
    }

    public void add(SourceRepository aRepository) {
        repositories.add(aRepository);
    }

    @Override
    public File locateFileForClass(Class aClass) {
        for (SourceRepository theRepository : repositories) {
            File theFile = theRepository.locateFileForClass(aClass);
            if (theFile != null) {
                return theFile;
            }
        }
        return null;
    }
}
