package karate;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.Options;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import karate.test.Startable;
import karate.test.Stoppable;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

@Slf4j
public class KarateMockRunner extends KarateRunner implements Startable, Stoppable {
    private static WireMockServer wireMockServer;

    public KarateMockRunner(Object application) {
        super(application);

        // new wiremock
        String mockDir = System.getProperty("mock-dir", "/");
        Options opts = options()
                .port(8081)
                .withRootDirectory(getClass().getResource("/" + mockDir).getPath());

        wireMockServer = new WireMockServer(opts);
    }

    public KarateMockRunner(Object application, int port) {
        super(application, port);
    }

    @Override
    public void start(String[] args, boolean wait) throws Exception {
        log.info("Starting WireMock server ...");
        wireMockServer.start();
    }

    @Override
    public void stop() {
        wireMockServer.stop();
    }
}
