package my.app.playm.controller.handlers;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import my.app.playm.controller.Data;
import my.app.playm.controller.Dispatcher;
import my.app.playm.controller.TrackData;
import my.app.playm.model.repo.FrameRepository;
import my.app.playm.entity.frame.Frame;
import my.app.playm.entity.frame.SliderFrame;

import java.util.List;
import java.util.stream.IntStream;

public final class ZoomHandler {
    static private double oSceneX;
    static private double timelinePosition;
    static private int frameWidth = FrameRepository.frameWidth.get();
    static private int centerIndex;

    static public final EventHandler<MouseEvent> ZOOM_ON_MOUSE_PRESSED = e -> {
        if (!e.isAltDown()) return;
        oSceneX = e.getSceneX();
        if (e.isSecondaryButtonDown()) centerIndex = calculateCenterIndex();
        e.consume();
    };

    static public final EventHandler<MouseEvent> ZOOM_ON_MOUSE_RELEASED = e -> {
        if (!e.isAltDown()) return;
        timelinePosition = TrackData.framePane.getTranslateX();
        frameWidth = FrameRepository.frameWidth.get();
        e.consume();
    };
    /*Calculate delta between current point and origin and move framePane on delta value*/
    static public final EventHandler<MouseEvent> ZOOM_ON_MOUSE_DRAGGED = e -> {
        if (!e.isAltDown()) return;
        double delta = e.getSceneX() - oSceneX;
        if (e.isPrimaryButtonDown()) doPan(delta);
        if (e.isSecondaryButtonDown()) doZoom(delta);
        e.consume();
    };

    public static void doZoom(double delta) {
        double speed = 0.2;
        int width = (int) (speed * delta + frameWidth);
        if (1 < width && width < 65) {
            zoom(width);
            centerOnScreen();
            Dispatcher.updateFrame(Data.currentFrame);
        }
    }

    public static void zoom(int width) {
        FrameRepository.frameWidth.set(width);
        Data.frameRepo.updateWidth();
        recalculatePositionByIndex(TrackData.sliderPane);
        recalculatePositionByNum(TrackData.storePane);
        resizeFrameLabelFont(width);
        calculateSliderNumDisplay();
    }

    public static void doPan(double delta) {
        double speed = 2;
        double position = speed * delta + timelinePosition;
        if (position > 0) position = 0;
        Data.trackPan.setValue(position);
    }

    static private int calculateCenterIndex() {
        double screenWidth = TrackData.storePane.getScene().getWidth();
        double screenMid = screenWidth / 2;
        int frameWidth = FrameRepository.frameWidth.get();
        double shift = Data.trackPan.get();
        double value = (screenMid - shift) / frameWidth;
        int centerIndex = (int) value;
        if (value - centerIndex >= 0.5) centerIndex++;
        return centerIndex;
    }

    static private void centerOnScreen() {
        int newCenterIndex = calculateCenterIndex();
        Double currentValue = Data.trackPan.getValue();
        double offsetValue = (centerIndex - newCenterIndex) * FrameRepository.frameWidth.get();
        double centerValue = currentValue - offsetValue;
        if (centerValue >= 0) centerValue = 0;


        Data.trackPan.setValue(centerValue);
    }

    static private void calculateSliderNumDisplay() {
        int width = FrameRepository.frameWidth.get();
        TrackData.sliderPane.getChildren().forEach(node -> {
            SliderFrame sliderFrame = (SliderFrame) node;
            int sliderNum = sliderFrame.getNum();
            Label label = sliderFrame.getLabel();

            if (width > 30) {
                label.setVisible(true);
            }
            if (14 < width && width < 30) {
                if (sliderNum % 2 != 0) label.setVisible(false);
                if (sliderNum % 2 == 0) label.setVisible(true);
            }
            if (5 < width && width < 14) {
                if (sliderNum % 4 != 0) label.setVisible(false);
                if (sliderNum % 4 == 0) label.setVisible(true);
            }
            if (1 < width && width < 5) {
                if (sliderNum % 8 != 0) label.setVisible(false);
                if (sliderNum % 8 == 0) label.setVisible(true);
            }
        });
    }

    static private void recalculatePositionByIndex(Pane pane) {
        int width = FrameRepository.frameWidth.get();
        List<Node> childList = pane.getChildren();
        IntStream.range(0, childList.size()).forEach(index -> {
            childList.get(index).setTranslateX(index * width);
        });
    }

    static private void recalculatePositionByNum(Pane pane) {
        int width = FrameRepository.frameWidth.get();
        List<Node> childList = pane.getChildren();
        IntStream.range(0, childList.size()).forEach(index -> {
            Frame frame = (Frame) childList.get(index);
            frame.setTranslateX(frame.getNum() * width);
        });
    }

    static private void resizeFrameLabelFont(double width) {
        double size = (width * 12) / 40;
        if (width < 12) {
            changeFont(new Font(1));
        } else {
            changeFont(new Font(size + 4));
        }
    }

    static private void changeFont(Font font) {
        TrackData.framePane.getChildren().stream()
                .map(e -> ((Frame) e).getLabel())
                .forEach(label -> label.setFont(font));
    }
}
