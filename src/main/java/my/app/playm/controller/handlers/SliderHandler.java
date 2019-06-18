package my.app.playm.controller.handlers;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import lombok.extern.log4j.Log4j;
import my.app.playm.controller.Dispatcher;
import my.app.playm.entity.frame.SliderFrame;
import my.app.playm.model.player.Player;
import my.app.playm.model.repo.FrameRepository;
import my.app.playm.model.time.PlayRange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Log4j
@Component
public final class SliderHandler {
    @Autowired
    private FrameRepository frameRepository;
    @Autowired
    private PlayRange range;
    @Autowired
    private Player player;

    private int orgPressSceneX, shift, orgIndex;

    public final EventHandler<MouseEvent> ON_SLIDER_PRESSED = e -> {
        orgPressSceneX = (int) e.getSceneX();
        SliderFrame sliderFrame = (SliderFrame) getNode(e);
        orgIndex = sliderFrame.getNum();
        if (e.isSecondaryButtonDown() && e.getClickCount() == 1) {
            range.setRange(sliderFrame.getNum());
            return;
        }
        if (e.isSecondaryButtonDown() && e.getClickCount() == 2) {
            range.reset();
        }

        Dispatcher.updateFrame(sliderFrame.getNum());
        player.playSound(sliderFrame.getNum());
    };


    public final EventHandler<MouseEvent> ON_SLIDER_RELEASED = e -> {

    };
    public final EventHandler<MouseEvent> ON_SLIDER_DRAGGED = e -> {
        int width = FrameRepository.frameWidth.get();
        int delta = (int) (e.getSceneX() - orgPressSceneX);
        int shiftCurrent = delta / width;
        if (isAlreadyCalculated(shiftCurrent)) return;

        int frameNum = orgIndex + shiftCurrent;

        frameNum = boundIndex(frameNum);

        if (e.isSecondaryButtonDown()) {
            range.setRange(frameNum);
            return;
        }

        Dispatcher.updateFrame(frameNum);
        player.playSound(frameNum);
    };

    private int boundIndex(int currentIndex) {
        if (currentIndex >= frameRepository.size()) currentIndex = frameRepository.size() - 1;
        if (currentIndex < 0) currentIndex = 0;
        return currentIndex;
    }


    private boolean isAlreadyCalculated(int shiftCurrent) {
        if (shiftCurrent == shift) return true;
        shift = shiftCurrent;
        return false;
    }

    private Node getNode(Event event) {
        return (Node) event.getSource();
    }

}