package karate.test;

public interface Startable {
  void start() throws Exception;
  void start(String[] args, boolean wait) throws Exception;
}
