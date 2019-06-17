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



    public TrackController() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.debug("Initialiaze");
        initData();
        initBindings();
        addHandlers();
        // Tool pane is not ready yet, so I decided to hide it for a while
        vbox.getChildren().remove(toolPane);
    }
    @PostConstruct
    public void init(){
        log.debug("Post construct");
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
    }

    private void initBindings() {
        sliderControl.prefWidthProperty().bind(FrameRepository.frameWidth);
        markLine.prefWidthProperty().bind(sliderControl.prefWidthProperty());
        markLine.translateXProperty().bind(Bindings.add(sliderControl.translateXProperty(), sliderControlPane.translateXProperty()) );

        sliderControlPane.translateXProperty().bind(Data.trackPan);
        storePane.translateXProperty().bind(Data.trackPan);
        sliderPane.translateXProperty().bind(Data.trackPan);
        framePane.translateXProperty().bind(Data.trackPan);

        currentFrameLabel.translateXProperty().bind(totalSliderControl.translateXProperty().subtract(currentFrameLabel.prefWidthProperty().get()/2));
        Data.volumeProperty.bind(soundSlider.valueProperty());
    }

    private void addHandlers() {
        sliderPaneTop.addEventFilter(MouseEvent.MOUSE_PRESSED, ZoomHandler.ZOOM_ON_MOUSE_PRESSED);
        sliderPaneTop.addEventFilter(MouseEvent.MOUSE_DRAGGED, ZoomHandler.ZOOM_ON_MOUSE_DRAGGED);
        sliderPaneTop.addEventFilter(MouseEvent.MOUSE_RELEASED, ZoomHandler.ZOOM_ON_MOUSE_RELEASED);
        framePaneTop.addEventFilter(MouseEvent.MOUSE_PRESSED, ZoomHandler.ZOOM_ON_MOUSE_PRESSED);
        framePaneTop.addEventFilter(MouseEvent.MOUSE_DRAGGED, ZoomHandler.ZOOM_ON_MOUSE_DRAGGED);
        framePaneTop.addEventFilter(MouseEvent.MOUSE_RELEASED, ZoomHandler.ZOOM_ON_MOUSE_RELEASED);

        framePane.setOnMouseDragged(FramePaneHandler.FRAMEPANE_ON_MOUSE_DRAGGED);

        sliderPane.setOnMouseDragged(SliderHandler.ON_SLIDER_DRAGGED);
        sliderPane.setOnMouseReleased(SliderHandler.ON_SLIDER_RELEASED);
        storePane.setOnDragEntered(FramePaneHandler.STORE_LINE_DRAG_ENTERED);
        storePane.setOnDragExited(FramePaneHandler.STORE_LINE_DRAG_EXITED);
        storeLinePane.setOnDragOver(FramePaneHandler.STORE_LINE_DRAG_OVER);
        storeLinePane.setOnDragDropped(FramePaneHandler.STORE_LINE_DRAG_DROPPED);

        vbox.setOnDragDone(FramePaneHandler.ROOT_DRAG_DONE);

        totalSliderPane.setOnMousePressed(TotalSliderHandler.ON_PRESSED);
        totalSliderPane.setOnMouseDragged(TotalSliderHandler.ON_DRAGGED);

        framePaneTop.addEventFilter(EventType.ROOT, event -> {
            if (Data.playMode == PlaybackMode.ORIGINAL) event.consume();
        });
        storeLinePane.addEventFilter(EventType.ROOT, event -> {
            if (Data.playMode == PlaybackMode.ORIGINAL) event.consume();
        });
        //change focus on some other node, because after pressing it starts consuming keyboard events
        soundSlider.setOnMouseReleased(e-> framePane.requestFocus());
    }

}
