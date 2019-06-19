package my.app.playm.entity.frame.template;

import lombok.extern.log4j.Log4j;
import my.app.playm.entity.frame.Frame;
import org.springframework.stereotype.Component;

@Log4j
@Component
public class FrameEmptyTemplate implements Template<Frame> {
    private static int emptyFrameId = -1000;

    @Override
    public void buildView(Frame frame) {
        build(frame);
    }

    @Override
    public void addHandlers(Frame frame) {
    }

    private void build(Frame frame) {
        frame.setEmpty(true);
        frame.setId("emptyFrame_" + emptyFrameId--);
        frame.setVisible(false);
    }
}
