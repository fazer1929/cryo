/*
 * JBoss, Home of Professional Open Source.
 * Copyright (c) 2020, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.MalformedURLException;
import java.net.URL;

import org.jboss.set.cryo.process.ExecuteProcess;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CryoTest {
    private CryoAccess cryo;
    private String repoURL;
    protected static final String[] COMMAND_REMOVE_TEST_DIR = new String[] { "rm", "-rf", "cryo-tests" };

    CryoTest() throws MalformedURLException {
        repoURL = "https://github.com/jboss-set/cryo-tests";
        cryo = new CryoAccess();
        cryo.setUpCryo(repoURL);
        System.setProperty("aphrodite.config","/home/abagrawa/Desktop/aphrodite.properties.json");
    }

    @Test
    @Order(1)
    @DisplayName("Check Repository URL")
    public void testDetermineRepositoryURL() throws MalformedURLException {
        assertEquals(cryo.determineRepositoryURL(), true);
        assertEquals(cryo.getRepositoryURL(), new URL(repoURL));
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
        assertEquals(cryo.mergePRs(), true);
    }

    @Test
    @Order(6)
    @DisplayName("Check multiple PR merge")
    public void testMergemultiplePRs() throws MalformedURLException {
        cryo = new CryoAccess(new String[] { "2", "3" });
        cryo.setUpCryo(repoURL);
        assertEquals(cryo.mergePRs(), true);
    }

    @Test
    @Order(7)
    @DisplayName("Check PR merge with Dependency")
    public void testMergePRWithDependency() throws MalformedURLException {
        cryo = new CryoAccess(new String[] { "10","11","12" });
        cryo.setUpCryo(repoURL);
        assertEquals(cryo.mergePRs(), true);
    }

    @AfterAll
    @DisplayName("Removing Downloaded Files/Directories")
    public static void removeDir() {
        ProcessBuilder processBuilder = new ProcessBuilder(COMMAND_REMOVE_TEST_DIR);
        ExecuteProcess executeProcess = new ExecuteProcess(processBuilder);
        executeProcess.getProcessResult();
    }
}