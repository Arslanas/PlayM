package my.app.playm.controller;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j;
import my.app.playm.model.player.PlaybackMode;
import my.app.playm.model.player.Player;

@Log4j
public class Data {
    public static BorderPane appRoot;
    public static ImageView imageView;
    public static Scene scene;
    public static Stage stage;

    public static String videoSource;

    public static Dispatcher dispatcher;

    public static boolean isDecodeComplete;
    public static int debugTotalFrames = 20;
    public static int currentFrame = 0;
    public static int frameStep = 1;
    public static int framerate = 24;

    //Mirror property linked with imageview scaleX property
    public static DoubleProperty mirrorProperty = new SimpleDoubleProperty(1);
    //Trackpan property linked with frame pane, slider pane, store pane
    public static DoubleProperty trackPan = new SimpleDoubleProperty();
    public static DoubleProperty imageWidth = new SimpleDoubleProperty();
    public static DoubleProperty imageHeight = new SimpleDoubleProperty();
    //Sound slider value linked with volume property
    public static DoubleProperty volumeProperty = new SimpleDoubleProperty();
    public static PlaybackMode playMode = PlaybackMode.ACTUAL;

}
