import org.jboss.set.cryo.Cryo;
import org.jboss.set.cryo.staging.OperationCenter;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.ServiceLoader;
import java.util.Set;

public class CryoAccess extends Cryo {

    public static List<String> createIncludeList(){
        List<String> includeList = Arrays.asList("2","3","4","5");
        return includeList;
    }

    CryoAccess(){
        super(new File(System.getProperty("user.dir")+"/backup/"), true, false, false, new HashSet<String>(), CryoAccess.createIncludeList(), "future", "", new String[]{});
    }
    CryoAccess(final File directory, final boolean dryRun, final boolean invertPullRequests, final boolean checkPRState, Set<String> excludeSet, List<String> includeList, String suffix, String opsCore, String[] mavenArgs){
        super(directory, dryRun, invertPullRequests, checkPRState, excludeSet, includeList, suffix, opsCore, mavenArgs);
    }

    public boolean createOperationCenter(){
        return super.createOperationCenter();
    }

    public boolean determineRepositoryURL(){
        return super.determineRepositoryURL();
    }

    public boolean initAphrodite(){
        return super.initAphrodite();
    }

    public boolean fetchPRList(){
        return super.fetchPRList();
    }

    public URL getRepositoryURL(){
        return super.repositoryURL;
    }




}
