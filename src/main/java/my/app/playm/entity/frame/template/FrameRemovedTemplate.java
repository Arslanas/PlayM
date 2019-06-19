package my.app.playm.entity.frame.template;

import javafx.scene.control.OverrunStyle;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import lombok.extern.log4j.Log4j;
import my.app.playm.controller.handlers.FramePaneHandler;
import my.app.playm.entity.frame.FrameRemoved;
import my.app.playm.model.repo.FrameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Log4j
@Component
public class FrameRemovedTemplate implements Template<FrameRemoved> {
    @Autowired
    private FramePaneHandler framePaneHandler;

    private Font font = new Font(12);

    @Override
    public void buildView(FrameRemoved frame) {
        addId(frame);
        build(frame);
        buildStyle(frame);
    }

    @Override
    public void addHandlers(FrameRemoved frame) {
        frame.setOnMousePressed(framePaneHandler.FRAME_REMOVED_MOUSE_PRESSED);
    }

    void addId(FrameRemoved frame) {
        frame.setId("frameRemoved_" + frame.getNum());
        frame.getInnerPane().setId("track_frameRemoved_innerPane_" + frame.getNum());
        frame.getLabel().setId("track_frameRemoved_label_" + frame.getNum());
    }
    void build(FrameRemoved frame) {
        AnchorPane.setBottomAnchor(frame, .0);
        AnchorPane.setTopAnchor(frame, .0);
        anchorAllSides(frame.getInnerPane(), 5);
        anchorAllSides(frame.getLabel(), 0);

        frame.getChildren().add(frame.getInnerPane());
        frame.getChildren().add(frame.getLabel());
        frame.prefWidthProperty().bind(FrameRepository.frameWidth);

        frame.getLabel().setTextOverrun(OverrunStyle.CLIP);
        frame.getLabel().setMouseTransparent(true);
    }
    void buildStyle(FrameRemoved frame) {
        frame.getStyleClass().add("track-keyframe-pane-frame");
        frame.getInnerPane().getStyleClass().add("track-frameRemoved-pane-inner-frame");
        frame.getLabel().getStyleClass().add("track-keyframe-label-frame");
        frame.getLabel().setFont(font);
    }
}
