package de.mirkosertic.whobrokeit.git;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevSort;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.TreeWalk;

import de.mirkosertic.whobrokeit.core.SourceRepository;
import de.mirkosertic.whobrokeit.core.Version;
import de.mirkosertic.whobrokeit.core.VersionControlSystem;

public class GitVersionControlSystem implements VersionControlSystem {

    private Map<SourceRepository, Repository> repositories;

    public GitVersionControlSystem() {
        repositories = new HashMap<>();
    }

    @Override
    public Version computeVersionFor(SourceRepository aSourceRepository, File aFile) {
        try {
            Repository theRepository = repositories.get(aSourceRepository);
            if (theRepository == null) {
                FileRepositoryBuilder theBuilder = new FileRepositoryBuilder();
                theRepository = theBuilder.setGitDir(new File("C:\\work\\WhoBrokeIt\\.git")).readEnvironment()
                        .findGitDir().build();
                repositories.put(aSourceRepository, theRepository);
            }

            // We start with the HEAD commit
            ObjectId theHead = theRepository.resolve(Constants.HEAD);
            RevWalk theRevWalk = new RevWalk(theRepository);
            RevCommit theHeadCommit = theRevWalk.parseCommit(theHead);
            theRevWalk.sort(RevSort.COMMIT_TIME_DESC);
            theRevWalk.markStart(theHeadCommit);

            String theFullFileName = aFile.toString().replace('\\', '/');

            try {
                // And iterate over all commits
                for (RevCommit theCommit : theRevWalk) {
                    // We will search for all affected files by this commit
                    TreeWalk theTreeWalk = new TreeWalk(theRepository);
                    theTreeWalk.addTree(theCommit.getTree());
                    theTreeWalk.setRecursive(true);
                    while (theTreeWalk.next()) {
                        if (theFullFileName.endsWith(theTreeWalk.getPathString())) {
                            return new GitVersion(theCommit.getName(), theCommit.getAuthorIdent().getEmailAddress(), theCommit.getCommitTime());
                        }
                    }
                }
            } finally {
                theRevWalk.dispose();
            }

            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
