package karate;

import com.intuit.karate.junit4.Karate;
import net.singleclick.boilerapp.BoilerAppApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

/**
 * Created by jsk-initializr.
 */
@Slf4j
@RunWith(Karate.class)
public class KarateTestBase {

    private static KarateParallelRunner runner;

    @BeforeClass
    public static void setUpClass() throws Exception {
        log.info("Karate setUpClass called.");
        runner = new KarateParallelRunner(BoilerAppApplication.class);
        runner.start(new String[]{"--spring.profiles.active=local", "--server.port=0"}, false);

    }

    @AfterClass
    public static void afterClass() {
        if (runner != null) {
            runner.stop();
        }
    }
}