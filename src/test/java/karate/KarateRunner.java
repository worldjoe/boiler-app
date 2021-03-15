package karate;

import lombok.extern.slf4j.Slf4j;
import karate.test.ServerStart;
import karate.test.Startable;
import karate.test.Stoppable;

@Slf4j
public class KarateRunner implements Startable, Stoppable {

    public static final String[] TEST_ARGS = new String[]{
        "--spring.profiles.active=local",
        "--server.port=0"
    };

    private static ServerStart server;
    private static Object application;
    private static int port;

    public KarateRunner() {}

    public KarateRunner(Object application) {
        KarateRunner.application = application;
        server = new ServerStart(application, port);
    }

    public KarateRunner(Object application, int port) {
        this(application);
        KarateRunner.port = port;
    }

    @Override
    public void start(String [] args, boolean wait) throws Exception {
        String env = System.getProperty("karate.env");
        
        /* NO Environment or  Local */
        if(env == null || env.equalsIgnoreCase("local"))
        {
            log.debug("Starting Server in Karate.");
            server.start(args, wait);
            System.setProperty("karate.server.port", server.getPort() + "");

        } /* NOT LOCAL  OR SET */ else {
            log.debug("Environment has been set, not starting server.");
        }
    }

    @Override
    public void start() throws Exception {
        start(TEST_ARGS, false);
    }

    @Override
    public void stop() throws Exception {
        if(server != null) {
            server.stop();
        }
    }
}
