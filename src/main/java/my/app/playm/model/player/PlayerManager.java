package my.app.playm.model.player;

import javafx.scene.media.MediaPlayer;

public interface PlayerManager {
    void play();

    void stop();

    void pause();

    boolean isPlaying();

    void updateAudio();

    void playSound(int frame);

    void setAudioPlayer(MediaPlayer audioPlayer);

    MediaPlayer getAudioPlayer();
}
