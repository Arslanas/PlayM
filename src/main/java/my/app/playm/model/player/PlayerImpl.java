package my.app.playm.model.player;

import javafx.scene.media.MediaPlayer;
import lombok.ToString;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Log4j
@Component("player")
@ToString
public class PlayerImpl implements Player {
    private PlayerManager manager;

    @Autowired
    public PlayerImpl(@Qualifier("audioVideo")PlayerManager manager) {
        this.manager = manager;
    }

    public void play() {
        manager.play();
    }

    public void stop() {
        manager.stop();
    }

    public void pause() {
        manager.pause();
    }

    public boolean isPlaying() {
        return manager.isPlaying();
    }

    @Override
    public void updateAudio() {
        manager.updateAudio();
    }

    @Override
    public void playSound(int frame) {
        manager.playSound(frame);
    }

    @Override
    public void setAudioPlayer(MediaPlayer audioPlayer) {
        manager.setAudioPlayer(audioPlayer);
    }

    @Override
    public MediaPlayer getAudioPlayer() {
        return manager.getAudioPlayer();
    }

    @Override
    public void setManager(PlayerManager manager) {
        this.manager = manager;
    }

}
