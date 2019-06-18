package my.app.playm.model.player;

import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;


public interface PlayerManager {
    void play();

    void stop();

    void pause();

    boolean isPlaying();

    void seekAudio(int seekFrame);

    void updateAudio();

    void playSound(int frame);

    void setAudioPlayer(MediaPlayer audioPlayer);

    MediaPlayer getAudioPlayer();
}
