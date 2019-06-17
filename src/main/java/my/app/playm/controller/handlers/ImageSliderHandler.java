package my.app.playm.controller.handlers;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import my.app.playm.controller.Data;
import my.app.playm.controller.Dispatcher;
import my.app.playm.controller.Util;

public class ImageSliderHandler {
    private static double press;
    private static int pressFrameNum;
    private static int dragFrameNum;
    private static double width = 25;

    private static final int MIN_PIXELS = 10;

    private static ObjectProperty<Point2D> mouseDown = new SimpleObjectProperty<>();

    static public final EventHandler<MouseEvent> ON_PRESSED = e -> {
        press = e.getSceneX();
        pressFrameNum = Data.currentFrame;


        Point2D mousePress = imageViewToImage(Data.imageView, new Point2D(e.getX(), e.getY()));
        mouseDown.set(mousePress);
    };
    static public final EventHandler<MouseEvent> ON_DRAGGED = e -> {
        double delta = e.getSceneX() - press;
        if (e.isAltDown()) {
            if (e.isMiddleButtonDown()) pan(e);
            if (e.isPrimaryButtonDown()) ZoomHandler.doPan(delta);
            if (e.isSecondaryButtonDown()) ZoomHandler.doZoom(delta);
            return;
        }
        playVideo(delta);

    };
    static public final EventHandler<ScrollEvent> ON_SCROLL = e -> {
        ImageView view = Data.imageView;
        double delta = e.getDeltaY() * -1;
        Rectangle2D viewport = view.getViewport();

        double scale = Util.clamp(Math.pow(1.01, delta),

                // don't scale so we're zoomed in to fewer than MIN_PIXELS in any direction:
                Math.min(MIN_PIXELS / viewport.getWidth(), MIN_PIXELS / viewport.getHeight()),

                // don't scale so that we're bigger than image dimensions:
                Math.max(Data.imageWidth.get() / viewport.getWidth(), Data.imageHeight.get() / viewport.getHeight())

        );
        Point2D mouse = imageViewToImage(view, new Point2D(e.getX(), e.getY()));

        double newWidth = viewport.getWidth() * scale;
        double newHeight = viewport.getHeight() * scale;
        double newMinX = Util.clamp(mouse.getX() - (mouse.getX() - viewport.getMinX()) * scale,
                0, Data.imageWidth.get() - newWidth);
        double newMinY = Util.clamp(mouse.getY() - (mouse.getY() - viewport.getMinY()) * scale,
                0, Data.imageHeight.get() - newHeight);

        view.setViewport(new Rectangle2D(newMinX, newMinY, newWidth, newHeight));
    };

    private static void pan(MouseEvent e) {
        ImageView view = Data.imageView;
        Point2D dragPoint = imageViewToImage(view, new Point2D(e.getX(), e.getY()));
        shift(view, dragPoint.subtract(mouseDown.get()));
        mouseDown.set(imageViewToImage(view, new Point2D(e.getX(), e.getY())));
    }

    private static void playVideo(double delta) {
        int calcDragFrameNum = (int) (delta / width);

        if (dragFrameNum == calcDragFrameNum) return;
        else dragFrameNum = calcDragFrameNum;

        int frameNum = pressFrameNum + dragFrameNum;

        Dispatcher.updateFrame(frameNum);
    }

    private static Point2D imageViewToImage(ImageView imageView, Point2D imageViewCoordinates) {
        double xProportion = imageViewCoordinates.getX() / imageView.getBoundsInLocal().getWidth();
        double yProportion = imageViewCoordinates.getY() / imageView.getBoundsInLocal().getHeight();

        Rectangle2D viewport = imageView.getViewport();
        return new Point2D(
                viewport.getMinX() + xProportion * viewport.getWidth(),
                viewport.getMinY() + yProportion * viewport.getHeight());
    }

    private static void shift(ImageView imageView, Point2D delta) {
        Rectangle2D viewport = imageView.getViewport();

        double width = imageView.getImage().getWidth();
        double height = imageView.getImage().getHeight();

        double maxX = width - viewport.getWidth();
        double maxY = height - viewport.getHeight();

        double minX = Util.clamp(viewport.getMinX() - delta.getX(), 0, maxX);
        double minY = Util.clamp(viewport.getMinY() - delta.getY(), 0, maxY);

        imageView.setViewport(new Rectangle2D(minX, minY, viewport.getWidth(), viewport.getHeight()));
    }
}
