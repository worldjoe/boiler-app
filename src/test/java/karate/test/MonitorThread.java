package karate.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MonitorThread extends Thread {
    private Stoppable stoppable;
    private ServerSocket socket;

    public MonitorThread(int port, Stoppable stoppable) {
        this.stoppable = stoppable;
        setDaemon(true);
        setName("stop-monitor-" + port);
        try {
            socket = new ServerSocket(port, 1, InetAddress.getByName("127.0.0.1"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void stop(int port) {
        try {
            Socket s = new Socket(InetAddress.getByName("127.0.0.1"), port);
            OutputStream out = s.getOutputStream();
            log.info("sending stop request to monitor thread on port: {}", port);
            out.write(("\r\n").getBytes());
            out.flush();
            s.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        log.info("starting thread: {}", getName());
        Socket accept;
        try {
            accept = socket.accept();
            BufferedReader reader = new BufferedReader(new InputStreamReader(accept.getInputStream()));
            reader.readLine();
            log.info("shutting down thread: {}", getName());
            stoppable.stop();
            accept.close();
            socket.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
