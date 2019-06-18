package my.app.playm.controller;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import my.app.playm.controller.handlers.FramePaneHandler;
import my.app.playm.controller.handlers.SliderHandler;

public class TrackData {
    static public AnchorPane framePane ;
    static public AnchorPane markLine;
    static public AnchorPane sliderPane;
    static public AnchorPane sliderControlPane;
    static public AnchorPane sliderControl;
    static public Pane storePane;
    static public Pane storeLinePane;
    static public Node vbox;
    static public Node trackRoot;
    static public Node zoomTrack;
    static public Node videoModeCover;
    static public Node totalSliderPane;
    static public Node totalSliderControl;
    static public Label currentFrameLabel;
    static public Pane totalRangePane;
    static public Slider soundSlider;


    static public AnchorPane sliderPaneTop;
    static public AnchorPane framePaneTop;


    static public FramePaneHandler framePaneHandler;
    static public SliderHandler sliderHandler;
}
