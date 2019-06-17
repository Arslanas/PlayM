package my.app.playm.controller;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j;
import my.app.playm.model.decode.Decoder;
import my.app.playm.model.decode.DecoderAudio;
import my.app.playm.model.moment.MomentRepository;
import my.app.playm.model.player.PlaybackMode;
import my.app.playm.model.player.Player;
import my.app.playm.model.repo.FrameRepository;
import my.app.playm.model.repo.VideoRepository;
import my.app.playm.model.repo.VideoService;
import my.app.playm.model.time.PlayRange;
import my.app.playm.model.time.Timer;

@Log4j
public class Data {
    public static BorderPane appRoot;
    public static ImageView imageView;
    public static Scene scene;
    public static Stage stage;

    public static String videoSource;

    public static HotKeyMap keyMap;
    public static Properties prop;
    public static PlayRange range;
    public static Player player;
    public static VideoService videoService;
    public static VideoRepository videoRepo;
    public static FrameRepository frameRepo;
    public static Timer timer;
    public static Decoder decoder;
    public static MomentRepository momentRepo;
    public static DecoderAudio decoderAudio;

    public static boolean isDecodeComplete;
    public static int debugTotalFrames = 20;
    public static int currentFrame = 0;
    public static int frameStep = 1;
    public static int framerate = 24;
    public static DoubleProperty mirrorProperty = new SimpleDoubleProperty(1);
    public static DoubleProperty trackPan = new SimpleDoubleProperty();
    public static DoubleProperty imageWidth = new SimpleDoubleProperty();
    public static DoubleProperty imageHeight = new SimpleDoubleProperty();
    public static DoubleProperty volumeProperty = new SimpleDoubleProperty();
    public static PlaybackMode playMode = PlaybackMode.ACTUAL;

}
