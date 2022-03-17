import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.jboss.set.aphrodite.Aphrodite;
import org.jboss.set.aphrodite.config.AphroditeConfig;
import org.jboss.set.aphrodite.config.IssueTrackerConfig;
import org.jboss.set.aphrodite.config.RepositoryConfig;
import org.jboss.set.aphrodite.config.StreamConfig;
import org.jboss.set.aphrodite.config.StreamType;
import org.jboss.set.aphrodite.repository.services.common.RepositoryType;
import org.jboss.set.aphrodite.repository.services.github.GithubPullRequestHomeService;
import org.jboss.set.aphrodite.spi.AphroditeException;
import org.jboss.set.cryo.Cryo;
import org.jboss.set.cryo.process.BisectablePullRequest;
import org.jboss.set.cryo.process.ExecuteProcess;

public class CryoAccess extends Cryo {

    public static List<String> createIncludeList(String[] includeList) {
        List<String> include;
        if (includeList != null) {
            include = new ArrayList<>();
            for (String pr : includeList)
                include.add(pr);
        } else
            include = Arrays.asList("4");
        return include;
    }

    CryoAccess() {
        super(new File(System.getProperty("user.dir") + "/backup/"), true, false, false, new HashSet<String>(),
                CryoAccess.createIncludeList(null), "future", "", new String[] {});

    }

    CryoAccess(String[] includeList) {
        super(new File(System.getProperty("user.dir") + "/backup/"), true, false, false, new HashSet<String>(),
                CryoAccess.createIncludeList(includeList), "future", "", new String[] {});
    }

    public boolean createOperationCenter() {
        return super.createOperationCenter();
    }

    public boolean determineRepositoryURL() {
        return super.determineRepositoryURL();
    }

    public boolean initAphrodite() {
        return super.initAphrodite();
    }

    public boolean determineCurrentBranch() {
        return super.determineCurrentBranch();
    }

    public boolean fetchPRList(String repoUrl) throws MalformedURLException {
        determineCurrentBranch();
        super.repositoryURL = new URL(repoUrl);
        return super.fetchPRList();
    }

    public URL getRepositoryURL() {
        return super.repositoryURL;
    }

    public boolean initializeAphrodite() throws MalformedURLException {
        RepositoryConfig githubService = new RepositoryConfig("https://github.com/", "fazer1929",
                "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",
                RepositoryType.GITHUB);
        List<RepositoryConfig> repositoryConfigs = new ArrayList<>();
        repositoryConfigs.add(githubService);

        List<IssueTrackerConfig> issueTrackerConfigs = new ArrayList<>();

        StreamConfig streamService = new StreamConfig(
                new URL("https://raw.githubusercontent.com/jboss-set/jboss-streams/master/streams.json"), StreamType.JSON);
        List<StreamConfig> streamConfigs = new ArrayList<>();
        streamConfigs.add(streamService);

        try {
            AphroditeConfig aphroditeConfig = new AphroditeConfig(issueTrackerConfigs, repositoryConfigs, streamConfigs);
            this.aphrodite = Aphrodite.instance(aphroditeConfig);
            GithubPullRequestHomeService GithubPullRequestHomeService = new GithubPullRequestHomeService(aphrodite);
            super.aphrodite = this.aphrodite;
            return true;
        } catch (AphroditeException e) {
            return false;
        }
    }

    public void setUpCryo(String repoURL) throws MalformedURLException {
        ProcessBuilder processBuilder = new ProcessBuilder(
                new String[] { "git", "clone", repoURL });
        ExecuteProcess executeProcess = new ExecuteProcess(processBuilder);
        executeProcess.getProcessResult();

        System.setProperty("NO_STOP_BEFORE_MERGE", "x");
        processBuilder = new ProcessBuilder(
                new String[] { "export", "NO_STOP_BEFORE_MERGE=x" });
        executeProcess = new ExecuteProcess(processBuilder);
        executeProcess.getProcessResult();

        this.createOperationCenter();
        this.initializeAphrodite();
    }

    private void cleanupFutureBranch() {
        String testDir = System.getProperty("user.dir") + "/backup/";
        ProcessBuilder processBuilder = new ProcessBuilder(
                new String[] { "git", "checkout", "master" });
        processBuilder.directory(new File(testDir));
        ExecuteProcess executeProcess = new ExecuteProcess(processBuilder);
        executeProcess.getProcessResult();
        processBuilder = new ProcessBuilder(
                new String[] { "git", "branch", "-D", "masterfuture" });
        processBuilder.directory(new File(testDir));
        executeProcess = new ExecuteProcess(processBuilder);
        executeProcess.getProcessResult();
    }

    public boolean setUpFutureBranch() throws Exception {
        if (super.setUpFutureBranch()) {
            cleanupFutureBranch();
            return true;
        }
        return false;
    }

    private boolean checkShaExists() {
        // Getting latest commit ids of every eligible PR
        List<String> shas = new ArrayList<>();
        for (BisectablePullRequest bpr : coldStorage) {
            shas.add(bpr.getPullRequest().getCommits().get(0).getSha());
        }
        System.out.println(shas);
        boolean includesAll = true;

        // Getting the logs of directory cryo created
        String testDir = System.getProperty("user.dir") + "/backup/";
        ProcessBuilder processBuilder = new ProcessBuilder(
                new String[] { "git", "log" });
        processBuilder.directory(new File(testDir));
        ExecuteProcess executeProcess = new ExecuteProcess(processBuilder);
        String logs = executeProcess.getProcessResult().getOutput();

        // Checking id all the commits exist
        for (String sha : shas) {
            includesAll = logs.contains(sha);
            if (!includesAll)
                break;
        }
        return includesAll;
    }

    public boolean mergePRs() {
        try {
            cleanupFutureBranch();
            super.createStorage();
            if (checkShaExists())
                return true;
            return false;
        } catch (Exception e) {
            return false;
        }
    }

}
