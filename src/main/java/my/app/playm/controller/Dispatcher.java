package my.app.playm.controller;

import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import my.app.playm.model.decode.DecoderAudio;
import my.app.playm.model.repo.*;
import my.app.playm.entity.frame.ImageFrame;
import my.app.playm.model.player.Player;
import my.app.playm.model.player.PlayerOnlyVideo;
import my.app.playm.model.time.PlayRange;
import my.app.playm.model.time.Timer;
import my.app.playm.socket.PlayServer;
import my.app.playm.socket.RequestRange;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Log4j
@Component
@RequiredArgsConstructor
public class Dispatcher {

    private final VideoService videoService;
    private final VideoRepository videoRepo;
    private final FrameRepository frameRepo;
    private final FrameFabric frameFabric;
    private final HotKeyMap keyMap;
    private final Properties prop;
    private final DecoderAudio decoderAudio;
    private final Timer timer;
    private final Player player;
    private final PlayServer playServer;
    private final PlayRange range;
    private final ConfigurableApplicationContext context;

    public static void main(String[] args) {
        Image image = new Image(Paths.get("D:\\temp\\PlayM\\maya\\samples\\view_20.jpg").toUri().toString());
        System.out.println(image);
    }

    public void update(RequestRange range){
        // load images from given range
        List<ImageFrame> newImageFrameList = Util.loadImages(range, prop.getMayaImagesPath());
        //get original
        List<ImageFrame> originalList = videoRepo.getOriginalList();
        // change images in original
        List<ImageFrame> listRemove = originalList.subList(range.getStart(), range.getEnd());
        originalList.removeAll(listRemove);
        originalList.addAll(range.getStart(), newImageFrameList);
        //save new original
        videoRepo.setOriginalList(originalList);

        updateAll();
        log.debug("Updated succeeded");
        /*
        save images from path to VideoRepo original list
        save original list to moment repo
        update view
         */
    }

    public void updateAll() {
        if (!Data.isDecodeComplete) return;
        videoRepo.synchronize(frameRepo.getList());
        updateSliderRange();
        range.update();
        updateFrame(Data.currentFrame);
    }
    public void resetTime(){
        Data.currentFrame = range.getStart();
        if (decoderAudio.isAudio()) player.seekAudio(Data.currentFrame);
        log.debug(String.format("Reset to %d frame", Data.currentFrame));
    }
    public void updateFrame(int frameNum) {
        updateFrameBase(frameNum);
        TrackData.sliderControl.setTranslateX(Data.currentFrame * FrameRepository.frameWidth.get());
        player.playSound(frameNum);
    }

    public void updateFrame(int frameNum, boolean updateSlider) {
        updateFrameBase(frameNum);
        if (updateSlider) TrackData.sliderControl.setTranslateX(Data.currentFrame * FrameRepository.frameWidth.get());
    }

    public void decodeVideo(String source) {
        onStartNewVideo(source);
        videoService.decodeVideo(source);
    }

    private void onStartNewVideo(String source) {
        log.debug("Start to decode new video - " + source);
        if (source != null) Data.videoSource = source;
        videoRepo.clear();
        frameRepo.clear();
        Platform.runLater(() -> TrackData.sliderPane.getChildren().clear());
        Data.isDecodeComplete = false;
    }

    public void onNewImageFrame(ImageFrame frame) {
        videoRepo.add(frame);
        Platform.runLater(() -> {
            frameRepo.add(frame.getNum());
            TrackData.sliderPane.getChildren().add(frameFabric.createSliderFrame(frame.getNum()));
            if (frame.getNum() == 0) Data.imageView.setImage(frame.getImage());
        });
        log.debug("New frame decoded- " + frame.getNum());
    }

    public void closeApp() {
        timer.close();
        playServer.stop();
        context.close();
        log.debug("Application closed");
    }
    public void loadSequence(int size) throws Exception {
        String sequenceSource = Util.getSource(prop.getDebugSequenceSource());
        player.setManager(new PlayerOnlyVideo(timer));
        IntStream.range(0, size).forEach(i -> {
            String imagePath = sequenceSource + "\\sampleFrame_" + i + ".png";
            Image debugImage = new Image(Paths.get(imagePath).toUri().toString());
            ImageFrame imageFrame = new ImageFrame(debugImage, i);
            videoRepo.add(imageFrame);
            frameRepo.add(imageFrame.getNum());
            TrackData.sliderPane.getChildren().add(frameFabric.createSliderFrame(i));
            if (i == 0) Data.imageView.setImage(imageFrame.getImage());
        });
        onCompleteNewVideo();
    }

    public String getExportPath(String source) {
        Path sourcePath = Paths.get(source);
        String fileName = sourcePath.getFileName().toString().split("\\.")[0];
        return sourcePath.getParent().resolve(fileName + prop.getExportNamespace()).toString();
    }

    public void onCompleteNewVideo() {
        videoRepo.saveOriginalImages();

        range.setOrgEnd(frameRepo.size() - 1);

        Image image = videoRepo.getImageByNum(0).getImage();
        Data.imageWidth.set(image.getWidth());
        Data.imageHeight.set(image.getHeight());
        Data.imageView.setViewport(new Rectangle2D(0, 0, Data.imageWidth.get(), Data.imageHeight.get()));

        Data.isDecodeComplete = true;

        log.debug("Complete to decode new video");
    }

    public void onSceneLoaded(Scene scene, Stage stage) {
        log.debug("After scene loaded");

        Data.imageView.fitWidthProperty().bind(scene.widthProperty());
        Data.imageView.fitHeightProperty().bind(scene.heightProperty().subtract(TrackData.trackRoot.getBoundsInLocal().getHeight() + 25));
        scene.setOnKeyPressed((e) -> keyMap.process(e));
        stage.widthProperty().addListener((obs, oldVal, newVal) -> updateAll());
        stage.maximizedProperty().addListener((obs, oldVal, newVal) -> updateAll());
        Data.scene = scene;
        Data.stage = stage;
    }
    public int totalFrames(){
        return frameRepo.size();
    }

    private void updateSliderRange() {
        List<Node> sliderList = TrackData.sliderPane.getChildren();
        int oldSize = sliderList.size();
        int newSize = frameRepo.size();
        if (oldSize != newSize) {
            if (newSize > sliderList.size()) {
                while (sliderList.size() != newSize) sliderList.add(frameFabric.createSliderFrame(sliderList.size()));
            } else {
                while (sliderList.size() != newSize) sliderList.remove(sliderList.size() - 1);
            }
            ;
        }
    }

    private void updateFrameBase(int frameNum) {
        if (frameNum >= frameRepo.size()) frameNum = frameRepo.size() - 1;
        if (frameNum <= 0) frameNum = 0;

        Data.currentFrame = frameNum;

        // manage total slider here
        TrackData.totalSliderControl.setTranslateX(calcTotalSliderControlPosition(Data.currentFrame));
        Platform.runLater(() -> TrackData.currentFrameLabel.setText(Data.currentFrame + ""));

        videoRepo.showImage(frameNum);

    }

    private double calcTotalSliderControlPosition(int frame) {
        double totalFrames = frameRepo.size() - 1;
        double totalWidth = TrackData.totalSliderPane.getBoundsInLocal().getWidth() - TrackData.currentFrameLabel.getWidth() / 2;

        double position = (frame * totalWidth) / totalFrames;

        if (position > totalWidth) position = totalWidth;

        return position;
    }

}
