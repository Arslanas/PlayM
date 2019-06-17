package my.app.playm.controller;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j;
import my.app.playm.entity.frame.ImageFrame;
import my.app.playm.entity.frame.SliderFrame;
import my.app.playm.model.repo.FrameRepository;

import java.util.List;
@Log4j
public class Dispatcher {

    public static void fire(Event event) {
        Data.appRoot.fireEvent(event);
    }

    public static void updateAll() {
        Data.videoRepo.synchronize(Data.frameRepo.getList());
        updateSliderRange();
        Data.range.update();
        updateFrame(Data.currentFrame);
    }

    public static void updateFrame(int frameNum) {
        updateFrameBase(frameNum);
        TrackData.sliderControl.setTranslateX(Data.currentFrame * FrameRepository.frameWidth.get());
    }

    public static void updateFrame(int frameNum, boolean updateSlider) {
        updateFrameBase(frameNum);
        if (updateSlider) TrackData.sliderControl.setTranslateX(Data.currentFrame * FrameRepository.frameWidth.get());
    }
    public static void onStartNewVideo(String source){
        log.debug("Start to decode new video - " + source);
        if (source != null) Data.videoSource = source;
        Data.videoRepo.clear();
        Data.frameRepo.clear();
        Platform.runLater(() -> TrackData.sliderPane.getChildren().clear());
        Data.isDecodeComplete = false;
    }
    public static void onNewImageFrame(ImageFrame frame){
        Data.videoRepo.add(frame);
        Platform.runLater(() -> {
            Data.frameRepo.add(frame.getNum());
            TrackData.sliderPane.getChildren().add(new SliderFrame(frame.getNum()));
            if (frame.getNum() == 0) Data.imageView.setImage(frame.getImage());
        });
        log.debug("New frame decoded- " + frame.getNum());
    }
    public static void onCompleteNewVideo(){
        Data.videoRepo.saveOriginalImages();

        Data.range.setOrgEnd(Data.videoRepo.size() - 1);

        Image image = Data.videoRepo.getImageByNum(0).getImage();
        Data.imageWidth.set(image.getWidth());
        Data.imageHeight.set(image.getHeight());
        Data.imageView.setViewport(new Rectangle2D(0, 0, Data.imageWidth.get(), Data.imageHeight.get()));

        Data.isDecodeComplete = true;

        log.debug("Complete to decode new video");
    }
    public static void onSceneLoaded(Scene scene, Stage stage){
        log.debug("After scene loaded");

        Data.imageView.fitWidthProperty().bind(scene.widthProperty());
        Data.imageView.fitHeightProperty().bind(scene.heightProperty().subtract(TrackData.trackRoot.getBoundsInLocal().getHeight() + 25));
        scene.setOnKeyPressed((e)->Data.keyMap.process(e));
        stage.widthProperty().addListener((obs, oldVal, newVal) -> Dispatcher.updateAll());
        stage.maximizedProperty().addListener((obs, oldVal, newVal) -> Dispatcher.updateAll());
        Data.scene = scene;
        Data.stage = stage;
    }



    private static void updateSliderRange() {
        List<Node> sliderList = TrackData.sliderPane.getChildren();
        int oldSize = sliderList.size();
        int newSize = Data.frameRepo.size();
        if (oldSize != newSize) {
            if (newSize > sliderList.size()) {
                while (sliderList.size() != newSize) sliderList.add(new SliderFrame(sliderList.size()));
            } else {
                while (sliderList.size() != newSize) sliderList.remove(sliderList.size() - 1);
            }
            ;
        }
    }

    private static void updateFrameBase(int frameNum) {
        if (frameNum >= Data.videoRepo.size()) frameNum = Data.videoRepo.size() - 1;
        if (frameNum <= 0) frameNum = 0;

        Data.currentFrame = frameNum;

        // manage total slider here
        TrackData.totalSliderControl.setTranslateX(calcTotalSliderControlPosition(Data.currentFrame));
        Platform.runLater(() -> TrackData.currentFrameLabel.setText(Data.currentFrame + ""));

        Data.videoRepo.showImage(frameNum);

    }

    private static double calcTotalSliderControlPosition(int frame) {
        double totalFrames = Data.videoRepo.size() - 1;
        double totalWidth = TrackData.totalSliderPane.getBoundsInLocal().getWidth() - TrackData.currentFrameLabel.getWidth() / 2;

        double position = (frame * totalWidth) / totalFrames;

        if (position > totalWidth) position = totalWidth;

        return position;
    }

}
