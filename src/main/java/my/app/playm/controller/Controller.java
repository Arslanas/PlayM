package my.app.playm.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
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
    private MediaView mediaSound;

    @FXML
    private BorderPane appRoot;
    @FXML
    private StackPane centerPane;
    @FXML
    private Label counter;

    @Autowired
    private DragDropHandler dragDropHandler;
    @Autowired
    VideoRepository videoRepository;
    @Autowired
    VideoService videoService;
    @Autowired
    Player player;
    @Autowired
    PlayRange range;
    @Autowired
    Timer timer;
    @Autowired
    Decoder decoder;
    @Autowired
    DecoderAudio decoderAudio;
    @Autowired
    MomentRepository momentRepository;
    @Autowired
    FrameRepository frameRepository;
    @Autowired
    Properties prop;
    @Autowired
    HotKeyMap keyMap;

    @Autowired
    @Qualifier(value = "audioVideo")
    PlayerManager audioVideoManager;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.debug("Initialize");
    }

    @PostConstruct
    public void init() {
        Data.imageView = imageView;
        Data.appRoot = appRoot;
        counter.setVisible(false);

        imageView.scaleXProperty().bind(Data.mirrorProperty);

        log.debug("Post construct");
        Data.videoRepo = videoRepository;
        Data.frameRepo = frameRepository;
        Data.videoService = videoService;
        Data.player = player;
        Data.range = range;
        Data.timer = timer;
        Data.decoder = decoder;
        Data.decoderAudio = decoderAudio;
        Data.momentRepo = momentRepository;
        Data.prop = prop;
        Data.keyMap = keyMap;

        Data.frameRepo.init(TrackData.framePane, TrackData.storePane);
        Data.videoRepo.setImageView(imageView);
        Data.range.setOrgEnd(10);
        player.setManager(audioVideoManager);

        addHandlers();
    }

    private void addHandlers() {
        centerPane.setOnDragOver(dragDropHandler.VIDEO_DRAG_OVER);
        centerPane.setOnDragDropped(dragDropHandler.VIDEO_DROP);

        imageView.setOnMouseDragged(ImageSliderHandler.ON_DRAGGED);
        imageView.setOnMousePressed(ImageSliderHandler.ON_PRESSED);
        imageView.setOnScroll(ImageSliderHandler.ON_SCROLL);
    }
}
