package my.app.playm.socket;

import java.io.IOException;

public interface PlayServer {
    void start();
    void stop();
    boolean isRunning();
}
