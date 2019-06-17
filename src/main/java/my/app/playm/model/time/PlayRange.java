package my.app.playm.model.time;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import lombok.extern.log4j.Log4j;
import my.app.playm.controller.Data;
import my.app.playm.controller.TrackData;
import my.app.playm.entity.frame.SliderFrame;

import java.util.List;

public interface PlayRange {
    //set playback boundary range
    void setRange(int num);

    //update visual style of range
    void update();

    //get start value of range
    int getStart();

    //get end value of range
    int getEnd();

    //remove range - start value is 0, end value is original size of video frames
    void reset();

    // setter of original end value
    void setOrgEnd(int end);

    // getter of original end value. It supposed to be used in audioPlayback and playback in ACTUAL mode
    int getOrgEnd();

    // is end value exists
    boolean isEnd();

}
