package my.app.playm.model.player;

import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.log4j.Log4j;
import my.app.playm.model.time.Timer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Log4j
@Component("onlyVideo")
@ToString
@RequiredArgsConstructor
public class PlayerOnlyVideo implements PlayerManager {
    private final Timer timer;
    private boolean isPlaying = false;


    public void play() {
        timer.start();
        isPlaying = true;
    }

    public void stop() {
        timer.stop();
        timer.reset();
        isPlaying = false;
    }

    public void pause() {
        timer.stop();
        isPlaying = false;
    }

    @Override
    public void updateAudio() {
    }

    @Override
    public void playSound(int frame) {
    }

    @Override
    public void setAudioPlayer(MediaPlayer audioPlayer) {
    }

    @Override
    public void seekAudio(int seekFrame) {
    }

    @Override
    public MediaPlayer getAudioPlayer() {
        throw new UnsupportedOperationException("This implementation does not support audio");
    }

    public boolean isPlaying() {
        return isPlaying;
    }
}
