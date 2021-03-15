package karate.test;

import org.junit.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;

@Slf4j
public class ServerStart implements Startable {
    private Object application;
    private ConfigurableApplicationContext context;
    private MonitorThread monitor;
    private int port;

    public ServerStart(Object application) {
        this.application = application;
        this.port = 0;
    }
    public ServerStart(Object application, int port) {
        this(application);
        this.port = port;
    }

    @Override
    public void start(String[] args, boolean wait) throws Exception {
        if (wait) {
            try {
                log.info("attempting to stop server if it is already running");
                new ServerStop().stopServer();
            } catch (Exception e) {
                log.info("failed to stop server (was probably not up): {}", e.getMessage());
            }
        }
        context = SpringApplication.run((Class)application, args);
        Environment ss = context.getBean(Environment.class);
        port = ss.getProperty("local.server.port",Integer.class);
        log.info("started server on port: {}", port);
        if (wait) {
            int stopPort = port + 1;
            log.info("will use stop port as {}", stopPort);
            monitor = new MonitorThread(stopPort, () -> context.close());
            monitor.start();
            monitor.join();
        }
    }

    @Override
    public void start() throws Exception {

    }

    public int getPort() {
        return port;
    }

    public void stop() {
        log.info("stopping spring context");
        context.stop();
    }

    @Test
    public void startServer() throws Exception {
        start(new String[]{
            "--spring.profiles.active=dev",
            "--spring.cloud.bootstrap.enabled=false"
        }, true);
    }
}
