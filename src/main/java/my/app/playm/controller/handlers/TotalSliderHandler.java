package my.app.playm.controller.handlers;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import lombok.extern.log4j.Log4j;
import my.app.playm.controller.Data;
import my.app.playm.controller.Dispatcher;
import my.app.playm.controller.TrackData;
import my.app.playm.model.player.Player;
import my.app.playm.model.repo.FrameRepository;
import my.app.playm.model.time.PlayRange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Log4j
@Component
public class TotalSliderHandler {

    @Autowired
    private FrameRepository frameRepository;
    @Autowired
    private ZoomHandler zoomHandler;
    @Autowired
    private Player player;
    @Autowired
    private PlayRange range;
    private int currentFrameNum;

    public final EventHandler<MouseEvent> ON_PRESSED = e -> {
        double localX = e.getX();
        int frameNum = calcFrame(localX);

        currentFrameNum = frameNum;

        Dispatcher.updateFrame(frameNum);
        player.updateAudio();

        if (e.isSecondaryButtonDown()) {
            if (e.getClickCount() == 2) {
                range.reset();
                return;
            }
            range.setRange(frameNum);
        }

    };

    public final EventHandler<MouseEvent> ON_DRAGGED = e -> {
        double localX = e.getX();
        int frameNum = calcFrame(localX);

        if (frameNum == currentFrameNum) return;
        else currentFrameNum = frameNum;

        Dispatcher.updateFrame(frameNum);
        player.playSound(frameNum);


        if (e.isSecondaryButtonDown()) {
            range.setRange(frameNum);
            centralizeFrames();
        }
    };

    private void centralizeFrames() {
        double sceneWidth = Data.scene.getWidth();
        double sceneMiddle = sceneWidth / 2;

        int frameAmount = range.getEnd() - range.getStart();
        int middleNum = (range.getStart() + range.getEnd()) / 2;
        double newWidth = (sceneWidth - 150) / frameAmount;
        if (newWidth >= 45) newWidth = 45;
        zoomHandler.zoom((int) newWidth);
        double frameMiddle = middleNum * FrameRepository.frameWidth.get();
        double panValue = sceneMiddle - frameMiddle;
        if (panValue > 0) panValue = 0;
        Data.trackPan.setValue(panValue);
    }

    private int calcFrame(double localX) {
        double totalFrames = frameRepository.size();
        double totalWidth = TrackData.totalSliderPane.getBoundsInLocal().getWidth() - TrackData.currentFrameLabel.getWidth() / 2;

        if (localX >= totalWidth) localX = totalWidth;
        if (localX <= 0) localX = 0;

        return (int) ((localX * totalFrames) / totalWidth);
    }


}
