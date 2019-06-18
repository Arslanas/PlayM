package my.app.playm.controller;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j;
import my.app.playm.PlayM;

import java.nio.file.Paths;
import java.util.List;

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

    public static String getSource(String resource){
        String source = null;
        try {
            source = Paths.get(PlayM.class.getResource(resource).toURI()).toString();
        } catch (Exception e) {
            log.debug(e);
            throw new NullPointerException("Can not find debugSequenceSource file");
        }
        return source;
    }
    public static void chooseMonitor(Stage stage) {
        List<Screen> screens = Screen.getScreens();
        if (screens.size() == 2) {
            Rectangle2D bounds = screens.get(0).getVisualBounds();
            stage.setX(bounds.getMinX());
            stage.setY(bounds.getMinY());
            //stage.setMaximized(true);
        }
    }
}
