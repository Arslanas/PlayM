package my.app.playm.entity.frame;

import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import my.app.playm.controller.TrackData;
import my.app.playm.model.repo.FrameRepository;
import my.app.playm.controller.handlers.SliderHandler;

import java.util.List;

public class SliderFrame extends AnchorPane {
    final Line line;
    final Label label;
    final int num;

    public SliderFrame(int num){
        this.num = num;
        line = new Line();
        label = new Label(num + "");
        this.setTranslateX(num * FrameRepository.frameWidth.get());
        addId();
        buildView();
        buildStyle();
    }
    public Label getLabel(){
        return label;
    }

    public Line getLine() {
        return line;
    }

    public int getNum() {
        return num;
    }

    private void addId() {
        setId("sliderFrame_"+num);
        label.setId("slider-frame-label-"+num);
        line.setId("slider-frame-line-"+num);
    }

    private void buildView() {
        label.setMouseTransparent(true);
        line.setMouseTransparent(true);
        line.setStartY(25);
        line.setEndY(12);
        prefWidthProperty().bind(FrameRepository.frameWidth);
        AnchorPane.setBottomAnchor(this, .0);
        AnchorPane.setTopAnchor(this, .0);
        getChildren().add(line);
        getChildren().add(label);
        modifyView();
    }
    public void setCommonStyle(){
        List<String> styleList = getStyleClass();
        styleList.clear();
        getStyleClass().add("track-slider-pane");
    }
    public void setActiveStyle(){
        List<String> styleList = getStyleClass();
        styleList.clear();
        getStyleClass().add("track-slider-active-pane");
    }
    void buildStyle() {
        setCommonStyle();
        label.getStyleClass().add("track-slider-label");
        line.getStyleClass().add("track-slider-line");
    }
    void modifyView() {
    }

}
