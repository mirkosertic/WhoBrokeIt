package de.mirkosertic.whobrokeit.svn;

import de.mirkosertic.whobrokeit.core.SourceRepository;
import de.mirkosertic.whobrokeit.core.Version;
import de.mirkosertic.whobrokeit.core.VersionControlSystem;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.wc.*;

import java.io.File;

public class SVNVersionControlSystem implements VersionControlSystem {

    private final SVNClientManager svnClientManager;

    public SVNVersionControlSystem() {
        ISVNOptions theOptions = SVNWCUtil.createDefaultOptions(true);
        ISVNAuthenticationManager theAuthManager = SVNWCUtil.createDefaultAuthenticationManager();

        svnClientManager = SVNClientManager.newInstance(theOptions, theAuthManager);
    }

    @Override
    public Version computeVersionFor(SourceRepository aSourceRepository, File aFile) {
        try {
            SVNStatus theStatus = svnClientManager.getStatusClient().doStatus(aFile, false);
            SVNRevision theRevision = theStatus.getRevision();
            return new SVNVersion("" + theRevision.getNumber(), theStatus.getAuthor(), theStatus.getCommittedDate().getTime());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Version parseVersionFrom(String aVersionStringInfo) {
        return SVNVersion.parse(aVersionStringInfo);
    }
}
