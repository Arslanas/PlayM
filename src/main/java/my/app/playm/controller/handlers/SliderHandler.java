package my.app.playm.controller.handlers;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import lombok.extern.log4j.Log4j;
import my.app.playm.controller.Data;
import my.app.playm.controller.Dispatcher;
import my.app.playm.controller.TrackData;
import my.app.playm.model.repo.FrameRepository;
import my.app.playm.entity.frame.SliderFrame;

import java.util.List;

@Log4j
public final class SliderHandler {
    static List<Node> childList = TrackData.sliderPane.getChildren();
    static private  int orgPressSceneX, shift, orgIndex;

    static public  final EventHandler<MouseEvent> ON_SLIDER_PRESSED = e -> {
        orgPressSceneX  = (int) e.getSceneX();
        SliderFrame sliderFrame = (SliderFrame)getNode(e);
        orgIndex = sliderFrame.getNum();
        if(e.isSecondaryButtonDown()&& e.getClickCount() == 1) {
            Data.range.setRange(sliderFrame.getNum());
            return;
        }
        if(e.isSecondaryButtonDown() && e.getClickCount() == 2) {
            Data.range.reset();
        }

        Dispatcher.updateFrame(sliderFrame.getNum());
        Data.player.playSound(sliderFrame.getNum());
    };


    static public  final EventHandler<MouseEvent> ON_SLIDER_RELEASED = e -> {

    };
    static public  final EventHandler<MouseEvent> ON_SLIDER_DRAGGED = e -> {
        int width = FrameRepository.frameWidth.get();
        int delta = (int) (e.getSceneX() - orgPressSceneX);
        int shiftCurrent = delta / width;
        if (isAlreadyCalculated(shiftCurrent)) return;

        int frameNum = orgIndex + shiftCurrent;

        frameNum = boundIndex(frameNum);

        if(e.isSecondaryButtonDown()) {
            Data.range.setRange(frameNum);
            return;
        }

        Dispatcher.updateFrame(frameNum);
        Data.player.playSound(frameNum);
    };

    static private int boundIndex(int currentIndex) {
        if (currentIndex >= childList.size()) currentIndex = childList.size()-1;
        if (currentIndex < 0) currentIndex = 0;
        return currentIndex;
    }


    static private boolean isAlreadyCalculated(int shiftCurrent) {
        if (shiftCurrent == shift) return true;
        shift = shiftCurrent;
        return false;
    }
    static private  Node getNode(Event event) {
        return (Node) event.getSource();
    }

}