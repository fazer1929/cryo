import org.jboss.set.cryo.Cryo;
import org.jboss.set.cryo.Main;
import org.jboss.set.cryo.process.ExecuteProcess;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;



public class CryoTest {
    private CryoAccess cryo;


    CryoTest(){
        ProcessBuilder processBuilder = new ProcessBuilder(new String[]{"git","clone","https://github.com/fazer1929/backup"});
        ExecuteProcess executeProcess= new ExecuteProcess(processBuilder);
        executeProcess.getProcessResult();
        cryo=new CryoAccess();
        cryo.createOperationCenter();
    }

    @Test
    @DisplayName("Check Repository URL")
    public void testDetermineRepositoryURL() {
        Main.startTimeTracker();
        assertEquals(cryo.determineRepositoryURL(),true);
//        assertEquals(cryo.getRepositoryURL(),new URL("https://github.com/fazer1929/backup"));
    }

//    @Test
//    @DisplayName("Check Operation Center Creation")
//    public void testCreateOperationCenter(){
//        assertEquals(cryo.createOperationCenter(),true);
//    }
//
//    @Test
//    @DisplayName("Check Operation Center Creation")
//    public void testCreateOperationCenter(){
//        assertEquals(cryo.createOperationCenter(),true);
//    }


    @AfterAll
    @DisplayName("Removing Downloaded Files/Directories")
    public static void removeDir(){
        ProcessBuilder processBuilder = new ProcessBuilder(new String[]{"rm","-rf","backup"});
        ExecuteProcess executeProcess= new ExecuteProcess(processBuilder);
//        executeProcess.getProcessResult();

    }
}
