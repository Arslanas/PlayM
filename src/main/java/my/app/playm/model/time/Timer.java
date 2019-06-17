package my.app.playm.model.time;


public interface Timer {
    void start();
    void stop();
    void reset();
    void close();
    void reversePlay();
    void normalPlay();
}
