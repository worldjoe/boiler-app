package karate;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.intuit.karate.cucumber.CucumberRunner;
import com.intuit.karate.cucumber.KarateStats;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import cucumber.api.CucumberOptions;
import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;
import karate.test.ServerStart;
import karate.test.Startable;
import karate.test.Stoppable;

@Slf4j
@CucumberOptions(plugin = {"pretty", "html:target/surefire-reports"}, tags = {"~@ignore"})
public class KarateParallelRunner implements Startable, Stoppable {

    public static final String[] TEST_ARGS = new String[]{
        "--spring.profiles.active=local",
        "--server.port=0"
    };

    protected static ServerStart server;
    protected static Object application;
    private static int port;

    public KarateParallelRunner() {

    }

    public KarateParallelRunner(Object application) {
        KarateParallelRunner.application = application;
        server = new ServerStart(application, port);
    }

    public KarateParallelRunner(Object application, int port) {
        this(application);
        KarateParallelRunner.port = port;
    }

    @Override
    public void start(String[] args, boolean wait) throws Exception {
        String env = System.getProperty("karate.env");
        log.info("Karate env: "+ env);
        if(env == null
            || env.equalsIgnoreCase("local")
            || env.equalsIgnoreCase("mock")
            || env.equalsIgnoreCase("mocked")
            || env.equalsIgnoreCase("comp"))
        {
            server.start(args, wait);
            System.setProperty("karate.server.port",Integer.toString(server.getPort()));
            log.debug("Application Server started on PORT={}", server.getPort());
        }
    }

    @Override
    public void start() throws Exception {
        start(TEST_ARGS, false);
    }

    @Override
    public void stop() {
        if(server != null) {
            server.stop();
        }
    }

    @Test
    public void testParallel() {
        String karateOutputPath = "target/surefire-reports";
        KarateStats stats = CucumberRunner.parallel(getClass(), 5, karateOutputPath);
        generateReport(karateOutputPath);
        assertTrue("there are scenario failures", stats.getFailCount() == 0);
    }

    private static void generateReport(String karateOutputPath) {
        Collection<File> jsonFiles = FileUtils.listFiles(new File(karateOutputPath), new String[]{"json"}, true);
        List<String> jsonPaths = new ArrayList<>(jsonFiles.size());
        for (File file : jsonFiles) {
            jsonPaths.add(file.getAbsolutePath());
        }
        Configuration config = new Configuration(new File("target"), "demo");
        ReportBuilder reportBuilder = new ReportBuilder(jsonPaths, config);
        reportBuilder.generateReports();
    }
}
