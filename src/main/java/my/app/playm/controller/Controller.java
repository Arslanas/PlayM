package my.app.playm.controller;

import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.extern.log4j.Log4j;
import my.app.playm.controller.handlers.*;
import my.app.playm.model.decode.Decoder;
import my.app.playm.model.decode.DecoderAudio;
import my.app.playm.model.moment.MomentRepository;
import my.app.playm.model.player.PlaybackMode;
import my.app.playm.model.player.Player;
import my.app.playm.model.player.PlayerManager;
import my.app.playm.model.repo.FrameRepository;
import my.app.playm.model.repo.VideoService;
import my.app.playm.model.time.PlayRange;
import my.app.playm.model.time.Timer;
import my.app.playm.model.repo.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.annotation.PostConstruct;
import java.net.URL;
import java.util.ResourceBundle;

@Log4j
@Getter
public class Controller implements Initializable {
    @FXML
    private ImageView imageView;
    @FXML
    private BorderPane appRoot;
    @FXML
    private StackPane centerPane;

    @Autowired
    private DragDropHandler dragDropHandler;
    @Autowired
    private ImageSliderHandler imageSliderHandler;
    @Autowired
    private VideoRepository videoRepository;
    @Autowired
    private FrameRepository frameRepository;
    @Autowired
    private Dispatcher dispatcher;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.debug("Initialize");
    }

    @PostConstruct
    public void init() {
        log.debug("Post construct");

        Data.imageView = imageView;
        Data.appRoot = appRoot;

        imageView.scaleXProperty().bind(Data.mirrorProperty);

        Data.dispatcher = dispatcher;

        frameRepository.init(TrackData.framePane, TrackData.storePane);
        videoRepository.setImageView(imageView);

        addHandlers();
    }

    private void addHandlers() {
        centerPane.setOnDragOver(dragDropHandler.VIDEO_DRAG_OVER);
        centerPane.setOnDragDropped(dragDropHandler.VIDEO_DROP);

        imageView.setOnMouseDragged(imageSliderHandler.ON_DRAGGED);
        imageView.setOnMousePressed(imageSliderHandler.ON_PRESSED);
        imageView.setOnMouseReleased(imageSliderHandler.ON_RELEASED);
        imageView.setOnScroll(imageSliderHandler.ON_SCROLL);
    }

}
