package my.app.playm.entity.frame;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import my.app.playm.model.repo.FrameRepository;
import my.app.playm.controller.handlers.FramePaneHandler;

public class Frame extends AnchorPane {
    final Pane innerPane;
    final Label label;
    private boolean empty;
    private static int emptyFrameId = -1000;

    private final int num;

    public Frame(int num) {
        this.num = num;
        innerPane = new Pane();
        label = new Label(num + "");
        addId();
        buildView();
        buildStyle();
        addHandlers();
    }

    public Frame(int num, boolean isEmpty) {
        this(num);
        if (isEmpty) buildEmpty();
    }

    private void buildEmpty() {
        empty = true;
        setId("emptyFrame_" + emptyFrameId--);
        setMouseTransparent(true);
        innerPane.setMouseTransparent(true);
        label.setMouseTransparent(true);
        setVisible(false);
    }

    private void addId() {
        setId("frame_" + num);
        innerPane.setId("track-keyframe-innerPane-" + num);
        label.setId("track-keyframe-label-" + num);
    }

    void addHandlers() {
        setOnMousePressed(FramePaneHandler.FRAME_MOUSE_PRESSED);
        setOnDragDetected(FramePaneHandler.FRAME_DRAG_DETECTED);
        setOnMouseReleased(FramePaneHandler.FRAME_ON_MOUSE_RELEASED);
        setOnMouseDragEntered(FramePaneHandler.FRAME_MOUSE_DRAG_ENTERED);
    }

    private void buildView() {
        label.setMouseTransparent(true);
        AnchorPane.setBottomAnchor(this, .0);
        AnchorPane.setTopAnchor(this, .0);
        prefWidthProperty().bind(FrameRepository.frameWidth);
        createAnchor(innerPane, 5);
        createAnchor(label, 0);
        getChildren().add(innerPane);
        getChildren().add(label);
        label.setTextOverrun(OverrunStyle.CLIP);
    }

    private void buildStyle() {
        getStyleClass().add("track-keyframe-pane-frame");
        innerPane.getStyleClass().add("track-keyframe-pane-inner-frame");
        label.getStyleClass().add("track-keyframe-label-frame");
        label.setFont(new Font(12));
    }

    private void createAnchor(Node node, double value) {
        AnchorPane.setLeftAnchor(node, value);
        AnchorPane.setRightAnchor(node, value);
        AnchorPane.setTopAnchor(node, value);
        AnchorPane.setBottomAnchor(node, value);
    }

    public Label getLabel() {
        return label;
    }

    public int getNum() {
        return num;
    }


    public boolean isEmpty() {
        return empty;
    }

    @Override
    public String toString() {
        return "Frame{" +
                " num = " + num +
                ", empty = " + empty + " }";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof Frame)) return false;
        Frame frame = (Frame) o;
        if(!getId().equals(frame.getId())) return  false;
        if(empty != frame.empty)return false;
        return num == frame.num;
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
