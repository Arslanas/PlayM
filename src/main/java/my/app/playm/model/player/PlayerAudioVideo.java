package my.app.playm.model.player;

import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.log4j.Log4j;
import my.app.playm.controller.Data;
import my.app.playm.model.time.PlayRange;
import my.app.playm.model.time.Timer;
import org.springframework.stereotype.Component;

@Log4j
@Component("audioVideo")
@ToString
@RequiredArgsConstructor
public class PlayerAudioVideo implements PlayerManager {
    private final PlayRange range;
    private final Timer timer;

    private boolean isPlaying = false;
    private MediaPlayer audioPlayer;

    @Override
    public void seekAudio(int seekFrame) {
        audioPlayer.seek(Duration.millis(getAudioTime(seekFrame)));
    }

    public void play() {
        timer.start();

        seekAudio(Data.currentFrame);
        audioPlayer.play();

        isPlaying = true;
    }

    public void stop() {
        timer.stop();
        timer.reset();
        audioPlayer.stop();
        isPlaying = false;
    }

    public void pause() {
        timer.stop();
        audioPlayer.pause();
        isPlaying = false;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    @Override
    public void updateAudio() {
        if(isPlaying()){
            pause();
            play();
        };
    }

    @Override
    public MediaPlayer getAudioPlayer() {
        return audioPlayer;
    }

    @Override
    public void setAudioPlayer(MediaPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
    }
    @Override
    public void playSound(int frame) {
        if(!isPlaying()){
            new Thread(()->{
                seekAudio(frame);
                audioPlayer.play();
                try {
                    Thread.sleep(1000/Data.framerate);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                audioPlayer.pause();
            }).start();
        }
    }

    private double getAudioTime(int currentFrame) {
        double duration = audioPlayer.getTotalDuration().toMillis();
        int originTotalFrames = range.getOrgEnd();
        return (currentFrame * duration) / originTotalFrames;
    }

}
