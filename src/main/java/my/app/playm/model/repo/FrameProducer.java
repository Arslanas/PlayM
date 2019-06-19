package my.app.playm.model.repo;

import javafx.scene.Node;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import my.app.playm.controller.handlers.SliderHandler;
import my.app.playm.entity.frame.Frame;
import my.app.playm.entity.frame.FrameRemoved;
import my.app.playm.entity.frame.SliderFrame;
import my.app.playm.entity.frame.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Log4j
@Component
@RequiredArgsConstructor
public class FrameProducer implements FrameFabric{
    @Autowired
    private SliderHandler sliderHandler;

    @Autowired @Qualifier("frameTemplate")
    private Template<Frame> frameTemplate;
    @Autowired @Qualifier("frameEmptyTemplate")
    private Template<Frame> frameEmptyTemplate;
    @Autowired @Qualifier("frameRemovedTemplate")
    private Template<FrameRemoved> frameRemovedTemplate;


    public Frame createFrame(int num) {
        Frame frame = new Frame(num);
        doTemplate(frameTemplate, frame);
        return frame;
    }

    public Frame createEmpty(int num) {
        Frame frame = new Frame(num);
        doTemplate(frameEmptyTemplate, frame);
        return frame;
    }

    public FrameRemoved createFrameRemoved(int num, int recoverIndex) {
        FrameRemoved frameRemoved = new FrameRemoved(num, recoverIndex);
        doTemplate(frameRemovedTemplate, frameRemoved);
        return frameRemoved;
    }

    public SliderFrame createSliderFrame(int num) {
        SliderFrame sliderFrame = new SliderFrame(num);
        sliderFrame.setOnMousePressed(sliderHandler.ON_SLIDER_PRESSED);
        return sliderFrame;
    }

    private <T extends Node> void doTemplate(Template<T> template, T frame){
        template.buildView(frame);
        template.addHandlers(frame);
    }
}
