package my.app.playm.entity.frame;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import my.app.playm.controller.TrackData;
import my.app.playm.controller.handlers.FramePaneHandler;
import my.app.playm.controller.handlers.SliderHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

@Log4j
@Component
@RequiredArgsConstructor
public class FrameProducer {
    @Autowired
    private FramePaneHandler framePaneHandler;
    @Autowired
    private SliderHandler sliderHandler;

    public Frame createFrame(int num) {
        Frame frame = new Frame(num);
        frame.setOnMousePressed(framePaneHandler.FRAME_MOUSE_PRESSED);
        frame.setOnDragDetected(framePaneHandler.FRAME_DRAG_DETECTED);
        frame.setOnMouseReleased(framePaneHandler.FRAME_ON_MOUSE_RELEASED);
        frame.setOnMouseDragEntered(framePaneHandler.FRAME_MOUSE_DRAG_ENTERED);
        return frame;
    }

    public Frame createFrame(int num, boolean empty) {
        return new Frame(num, empty);
    }

    public FrameRemoved createFrameRemoved(int num, int recoverIndex) {
        FrameRemoved frameRemoved = new FrameRemoved(num, recoverIndex);
        frameRemoved.setOnMousePressed(framePaneHandler.FRAME_REMOVED_MOUSE_PRESSED);
        return frameRemoved;
    }

    public SliderFrame createSliderFrame(int num) {
        SliderFrame sliderFrame = new SliderFrame(num);
        sliderFrame.setOnMousePressed(sliderHandler.ON_SLIDER_PRESSED);
        return sliderFrame;
    }
}
