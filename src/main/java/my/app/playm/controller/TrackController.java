package my.app.playm.controller;

import javafx.beans.binding.Bindings;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j;
import my.app.playm.controller.handlers.*;
import my.app.playm.model.player.PlaybackMode;
import my.app.playm.model.repo.FrameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.URL;
import java.util.ResourceBundle;

@Component
@Log4j
public class TrackController implements Initializable {

    @FXML
    public AnchorPane framePane;
    @FXML
    public AnchorPane framePaneTop;
    @FXML
    public AnchorPane markLine;
    @FXML
    public AnchorPane sliderPane;
    @FXML
    public AnchorPane sliderPaneTop;
    @FXML
    public AnchorPane sliderControlPane;
    @FXML
    public AnchorPane sliderControl;
    @FXML
    public Pane storePane;
    @FXML
    public Pane toolPane;
    @FXML
    public Pane storeLinePane;
    @FXML
    public VBox vbox;
    @FXML
    public Node trackRoot;
    @FXML
    public Node zoomTrack;
    @FXML
    public Node videoModeCover;
    @FXML
    public Node totalSliderPane;
    @FXML
    public Node totalSliderControl;
    @FXML
    public Label currentFrameLabel;
    @FXML
    public AnchorPane totalSliderRange;
    @FXML
    public Slider soundSlider;

    @Autowired
    private FramePaneHandler framePaneHandler;
    @Autowired
    private SliderHandler sliderHandler;
    @Autowired
    private TotalSliderHandler totalSliderHandler;
    @Autowired
    private ZoomHandler zoomHandler;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.debug("Initialiaze");
        initData();
        initBindings();
        // Tool pane is not ready yet, so I decided to hide it for a while
        vbox.getChildren().remove(toolPane);
    }

    @PostConstruct
    public void init() {
        log.debug("Post construct");
        addHandlers();
    }

    private void initData() {
        TrackData.framePane = framePane;
        TrackData.markLine = markLine;
        TrackData.sliderPane = sliderPane;
        TrackData.sliderControlPane = sliderControlPane;
        TrackData.sliderControl = sliderControl;
        TrackData.storePane = storePane;
        TrackData.storeLinePane = storeLinePane;
        TrackData.vbox = vbox;
        TrackData.trackRoot = trackRoot;
        TrackData.zoomTrack = zoomTrack;
        TrackData.videoModeCover = videoModeCover;
        TrackData.totalSliderPane = totalSliderPane;
        TrackData.totalSliderControl = totalSliderControl;
        TrackData.currentFrameLabel = currentFrameLabel;
        TrackData.totalRangePane = totalSliderRange;
        TrackData.soundSlider = soundSlider;
        TrackData.sliderPaneTop = sliderPaneTop;
        TrackData.framePaneTop = framePaneTop;
    }

    private void initBindings() {
        sliderControl.prefWidthProperty().bind(FrameRepository.frameWidth);
        markLine.prefWidthProperty().bind(sliderControl.prefWidthProperty());
        markLine.translateXProperty().bind(Bindings.add(sliderControl.translateXProperty(), sliderControlPane.translateXProperty()));

        sliderControlPane.translateXProperty().bind(Data.trackPan);
        storePane.translateXProperty().bind(Data.trackPan);
        sliderPane.translateXProperty().bind(Data.trackPan);
        framePane.translateXProperty().bind(Data.trackPan);

        currentFrameLabel.translateXProperty().bind(totalSliderControl.translateXProperty().subtract(currentFrameLabel.prefWidthProperty().get() / 2));
        Data.volumeProperty.bind(soundSlider.valueProperty());
    }

    private void addHandlers() {
        TrackData.sliderPaneTop.addEventFilter(MouseEvent.MOUSE_DRAGGED, zoomHandler.ZOOM_ON_MOUSE_DRAGGED);
        TrackData.sliderPaneTop.addEventFilter(MouseEvent.MOUSE_RELEASED, zoomHandler.ZOOM_ON_MOUSE_RELEASED);
        TrackData.framePaneTop.addEventFilter(MouseEvent.MOUSE_PRESSED, zoomHandler.ZOOM_ON_MOUSE_PRESSED);
        TrackData.framePaneTop.addEventFilter(MouseEvent.MOUSE_DRAGGED, zoomHandler.ZOOM_ON_MOUSE_DRAGGED);
        TrackData.framePaneTop.addEventFilter(MouseEvent.MOUSE_RELEASED, zoomHandler.ZOOM_ON_MOUSE_RELEASED);

        TrackData.framePane.setOnMouseDragged(framePaneHandler.FRAMEPANE_ON_MOUSE_DRAGGED);

        TrackData.sliderPane.setOnMouseDragged(sliderHandler.ON_SLIDER_DRAGGED);
        TrackData.sliderPane.setOnMouseReleased(sliderHandler.ON_SLIDER_RELEASED);
        TrackData.storePane.setOnDragEntered(framePaneHandler.STORE_LINE_DRAG_ENTERED);
        TrackData.storePane.setOnDragExited(framePaneHandler.STORE_LINE_DRAG_EXITED);
        TrackData.storeLinePane.setOnDragOver(framePaneHandler.STORE_LINE_DRAG_OVER);
        TrackData.storeLinePane.setOnDragDropped(framePaneHandler.STORE_LINE_DRAG_DROPPED);

        TrackData.vbox.setOnDragDone(framePaneHandler.ROOT_DRAG_DONE);

        TrackData.totalSliderPane.setOnMousePressed(totalSliderHandler.ON_PRESSED);
        TrackData.totalSliderPane.setOnMouseDragged(totalSliderHandler.ON_DRAGGED);

        TrackData.framePaneTop.addEventFilter(EventType.ROOT, event -> {
            if (Data.playMode == PlaybackMode.ORIGINAL) event.consume();
        });
        TrackData.storeLinePane.addEventFilter(EventType.ROOT, event -> {
            if (Data.playMode == PlaybackMode.ORIGINAL) event.consume();
        });
        //change focus on some other node, because after pressing it starts consuming keyboard events
        TrackData.soundSlider.setOnMouseReleased(e -> framePane.requestFocus());
    }

}
