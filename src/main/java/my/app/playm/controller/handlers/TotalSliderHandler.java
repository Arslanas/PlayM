package my.app.playm.controller.handlers;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import lombok.extern.log4j.Log4j;
import my.app.playm.controller.Data;
import my.app.playm.controller.Dispatcher;
import my.app.playm.controller.TrackData;
import my.app.playm.model.repo.FrameRepository;

@Log4j
public class TotalSliderHandler {

    private static int currentFrameNum;

    static public final EventHandler<MouseEvent> ON_PRESSED = e -> {
        double localX = e.getX();
        int frameNum = calcFrame(localX);

        currentFrameNum = frameNum;

        Dispatcher.updateFrame(frameNum);
        Data.player.updateAudio();

        if (e.isSecondaryButtonDown()) {
            if (e.getClickCount() == 2) {
                Data.range.reset();
                return;
            }
            Data.range.setRange(frameNum);
        }

    };

    static public final EventHandler<MouseEvent> ON_DRAGGED = e -> {
        double localX = e.getX();
        int frameNum = calcFrame(localX);

        if (frameNum == currentFrameNum) return;
        else currentFrameNum = frameNum;

        Dispatcher.updateFrame(frameNum);
        Data.player.playSound(frameNum);


        if (e.isSecondaryButtonDown()) {
            Data.range.setRange(frameNum);
            centralizeFrames();
        }
    };

    private static void centralizeFrames() {
        double frameWidth = FrameRepository.frameWidth.get();
        double totalWidth = Data.videoRepo.size() * frameWidth;
        double sceneWidth = Data.scene.getWidth();
        double sceneMiddle = sceneWidth / 2;

        int frameAmount = Data.range.getEnd() - Data.range.getStart();
        int middleNum = (Data.range.getStart() + Data.range.getEnd()) / 2;
        double newWidth = (sceneWidth - 150) / frameAmount;
        if (newWidth >= 45) newWidth = 45;
        ZoomHandler.zoom((int) newWidth);
        double frameMiddle = middleNum * FrameRepository.frameWidth.get();
        double panValue = sceneMiddle - frameMiddle;
        if (panValue > 0) panValue = 0;
        Data.trackPan.setValue(panValue);
    }
    static private int calcFrame(double localX) {
        double totalFrames = Data.videoRepo.size();
        double totalWidth = TrackData.totalSliderPane.getBoundsInLocal().getWidth() - TrackData.currentFrameLabel.getWidth() / 2;

        if (localX >= totalWidth) localX = totalWidth;
        if (localX <= 0) localX = 0;

        return (int) ((localX * totalFrames) / totalWidth);
    }


}
