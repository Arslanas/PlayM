package my.app.playm.controller;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j;
import my.app.playm.PlayM;
import my.app.playm.entity.frame.ImageFrame;
import my.app.playm.entity.frame.SliderFrame;
import my.app.playm.model.player.PlayerOnlyVideo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.IntStream;

@Component
@Log4j
public class Util {

    public static int clamp(int value, int min, int max) {
        if (value < min)
            return min;
        if (value > max)
            return max;
        return value;
    }

    public static double clamp(double value, double min, double max) {
        if (value < min)
            return min;
        if (value > max)
            return max;
        return value;
    }

    public static String getExportPath(String source) {
        Path sourcePath = Paths.get(source);
        String fileName = sourcePath.getFileName().toString().split("\\.")[0];
        String exportPath = sourcePath.getParent().resolve(fileName + Data.prop.getExportNamespace()).toString();
        return exportPath;
    }

    public static void loadSequence() throws Exception{
        String sequenceSource = Util.getSource(Data.prop.getDebugSequenceSource());
        Data.player.setManager(new PlayerOnlyVideo(Data.timer));
        IntStream.range(0, Data.debugTotalFrames).forEach(i -> {
            String imagePath = sequenceSource + "\\sampleFrame_" + i + ".png";
            Image debugImage = new Image(Paths.get(imagePath).toUri().toString());
            ImageFrame imageFrame = new ImageFrame(debugImage, i);
            Data.videoRepo.add(imageFrame);
            Data.frameRepo.add(imageFrame.getNum());
            TrackData.sliderPane.getChildren().add(new SliderFrame(i));
            if (i == 0) Data.imageView.setImage(imageFrame.getImage());
        });
        Dispatcher.onCompleteNewVideo();
    }
    public static String getSource(String resource){
        String source = null;
        try {
            source = Paths.get(PlayM.class.getResource(resource).toURI()).toString();
        } catch (URISyntaxException e) {
            log.debug(e);
            throw new NullPointerException("Can not find debugSequenceSource file");
        }
        return source;
    }
    public static void chooseMonitor(Stage stage) {
        List<Screen> screens = Screen.getScreens();
        if (screens.size() == 2) {
            Rectangle2D bounds = screens.get(1).getVisualBounds();
            stage.setX(bounds.getMinX());
            stage.setY(bounds.getMinY());
            stage.setMaximized(true);
        }
    }
}
