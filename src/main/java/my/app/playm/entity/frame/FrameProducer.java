package my.app.playm.entity.frame;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import my.app.playm.controller.handlers.FramePaneHandler;
import my.app.playm.controller.handlers.SliderHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Log4j
@Component
@RequiredArgsConstructor
public class FrameProducer {

    private final FramePaneHandler framePaneHandler;
    private final SliderHandler sliderHandler;

    public Frame createFrame(int num){
        return new Frame(num);
    }
    public Frame createFrame(int num, boolean empty){
        return new Frame(num, empty);
    }
    public Frame createFrameRemoved(int num, int recoverIndex){
        return new FrameRemoved(num, recoverIndex);
    }
    public SliderFrame createSliderFrame(int num){
        return new SliderFrame(num);
    }
}
