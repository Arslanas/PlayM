package my.app.playm.model.repo;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import my.app.playm.controller.Data;
import my.app.playm.controller.Dispatcher;
import my.app.playm.model.decode.Decoder;
import my.app.playm.model.decode.DecoderAudio;
import my.app.playm.model.decode.Exporter;
import my.app.playm.model.player.Player;
import my.app.playm.model.player.PlayerAudioVideo;
import my.app.playm.model.player.PlayerOnlyVideo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Paths;

@Log4j
@Component
@RequiredArgsConstructor
public class VideoServiceImpl implements VideoService{
    private final Player player;
    private final VideoRepository videoRepository;
    private final Exporter exporter;
    private final Decoder decoder;
    private final DecoderAudio decoderAudio;
    private final PlayerOnlyVideo onlyVideoPlayer;
    private final PlayerAudioVideo audioVideoPlayer;

    public void decodeVideo(String source) {
        Dispatcher.onStartNewVideo(source);
        new Thread(() -> {
            decoder.decodeAndEvent(source);
            Dispatcher.onCompleteNewVideo();
        }).start();
        new Thread(() -> {
            decodeAudio(source);
        }).start();
    }
    public void serializeImageFrames() {
        log.debug("Start video serialization");
        try (FileOutputStream file = new FileOutputStream("video.data");
             ObjectOutputStream out = new ObjectOutputStream(file)) {
            out.writeObject(videoRepository.getList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.debug("Complete video serialization");
    }
    public void export(String target){
        exporter.export(target);
    }
    public void decodeAudio(String source){
        decoderAudio.decodeAudio(source);
        if(!decoderAudio.isAudio()){
            player.setManager(onlyVideoPlayer);
            return;
        }
        player.setManager(audioVideoPlayer);
        Media media = new Media(Paths.get(Data.prop.getAudioSource()).toUri().toString());
        MediaPlayer audioPlayer = new MediaPlayer(media);

        audioPlayer.volumeProperty().bind(Data.volumeProperty);
        audioPlayer.volumeProperty().addListener((obs, oldVal, newVal)->{
            if(newVal.doubleValue() == 0 ) log.debug("Volume iz zero");
        });
        player.setAudioPlayer(audioPlayer);

    }
}
