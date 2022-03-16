import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.MalformedURLException;
import java.net.URL;

import org.jboss.set.cryo.Main;
import org.jboss.set.cryo.process.ExecuteProcess;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CryoTest {
    private CryoAccess cryo;
    private String repoURL;

    CryoTest() throws MalformedURLException {
        repoURL = "https://github.com/fazer1929/backup";
        cryo = new CryoAccess();
        cryo.setUpCryo(repoURL);
        Main.startTimeTracker();
        System.out.println("\n\nExecuted\n\n");
    }

    @Test
    @Order(1)
    @DisplayName("Check Repository URL")
    public void testDetermineRepositoryURL() throws MalformedURLException {
        assertEquals(cryo.determineRepositoryURL(), true);
        assertEquals(cryo.getRepositoryURL(), new URL("https://github.com/fazer1929/backup"));
    }

    @Test
    @Order(2)
    @DisplayName("Check determining current branch")
    public void testDetermineCurrentBranch() {
        assertEquals(cryo.determineCurrentBranch(), true);
    }

    @Test
    @Order(3)
    @DisplayName("Check Fetch PR list")
    public void testFetchPRList() throws MalformedURLException {
        assertEquals(cryo.fetchPRList(repoURL), true);
    }

    @Test
    @Order(4)
    @DisplayName("Check future branch setup")
    public void testSetupFutureBranch() throws Exception {
        cryo.fetchPRList(repoURL);
        assertEquals(cryo.setUpFutureBranch(), true);
    }

    @Test
    @Order(5)
    @DisplayName("Check single PR merge")
    public void testMergeSinglePR() throws MalformedURLException {
        assertEquals(cryo.mergeSinglePR(), true);
    }

//    @Test
//    @Order(6)
//    @DisplayName("Check PR merge with Dependency")
//    public void testMergePRWithDependency() throws MalformedURLException {
//        assertEquals(cryo.mergePRWithDependency(), true);
//    }

    @AfterAll
    @DisplayName("Removing Downloaded Files/Directories")
    public static void removeDir() {
        ProcessBuilder processBuilder = new ProcessBuilder(new String[] { "rm", "-rf", "backup" });
        ExecuteProcess executeProcess = new ExecuteProcess(processBuilder);
        executeProcess.getProcessResult();
    }
}
