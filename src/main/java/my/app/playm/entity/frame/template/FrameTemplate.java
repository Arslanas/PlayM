package my.app.playm.entity.frame.template;

import javafx.scene.Node;
import javafx.scene.control.OverrunStyle;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import lombok.extern.log4j.Log4j;
import my.app.playm.controller.handlers.FramePaneHandler;
import my.app.playm.entity.frame.Frame;
import my.app.playm.model.repo.FrameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Log4j
@Component
public class FrameTemplate implements Template<Frame> {
    private Font font = new Font(12);

    @Autowired
    private FramePaneHandler framePaneHandler;

    @Override
    public void buildView(Frame frame){
        addId(frame);
        build(frame);
        buildStyle(frame);
    }

    private void addId(Frame frame) {
        frame.setId("frame_"+frame.getNum());
        frame.getInnerPane().setId("track_frame_innerPane_"+frame.getNum());
        frame.getLabel().setId("track_frame_label_"+frame.getNum());
    }

    private void build(Frame frame) {
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
    private void buildStyle(Frame frame) {
        frame.getStyleClass().add("track-keyframe-pane-frame");
        frame.getInnerPane().getStyleClass().add("track-keyframe-pane-inner-frame");
        frame.getLabel().getStyleClass().add("track-keyframe-label-frame");
        frame.getLabel().setFont(font);
    }
    @Override
    public void addHandlers(Frame frame) {
        frame.setOnMousePressed(framePaneHandler.FRAME_MOUSE_PRESSED);
        frame.setOnDragDetected(framePaneHandler.FRAME_DRAG_DETECTED);
        frame.setOnMouseReleased(framePaneHandler.FRAME_ON_MOUSE_RELEASED);
        frame.setOnMouseDragEntered(framePaneHandler.FRAME_MOUSE_DRAG_ENTERED);
    }

}
