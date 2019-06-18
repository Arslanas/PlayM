package my.app.playm.model.time;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import my.app.playm.controller.Data;
import my.app.playm.model.repo.FrameRepository;
import org.springframework.stereotype.Component;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import my.app.playm.controller.TrackData;
import my.app.playm.entity.frame.SliderFrame;

import java.util.List;
@Log4j
@Component
@RequiredArgsConstructor
public class PlayRangeImpl implements PlayRange {
    private int start = 0 ;
    private int end = 0;
    private int orgEnd = 0;
    private boolean isStart, isEnd;

    public void setRange(int num){
        if(num > Data.dispatcher.totalFrames() - 1) num = Data.dispatcher.totalFrames() - 1;
        if(num < 0 ) num = 0;

        if(!isStart && !isEnd) {
            start = num;
            end = num;
            isStart = true;
            update();
            return;
        }
        if(!isStart) {
            start = num;
            isStart = true;
            update();
            return;
        }
        if(!isEnd){
            end = num;
            isEnd = true;
            update();
            return;
        }
        if(num > end) {
            end = num;
            update();
            return;
        }

        if(num < start) {
            start = num;
            update();
            return;
        }
        int mid = (start + end)/2;
        if(num > mid) {
            end = num;
            update();
            return;
        }
        if(num < mid) {
            start = num;
            update();
            return;
        }
    }

    public void update(){
        if(!isStart && !isEnd) {
            hideRange();
            return;
        }else showRange();

        updateTrackRange();
        updateTotalRange();
    }

    private void showRange() {
        TrackData.totalRangePane.setVisible(true);
    }

    private void hideRange() {
        TrackData.totalRangePane.setVisible(false);
        List<Node> list = TrackData.sliderPane.getChildren();
        list.forEach(e->((SliderFrame)e).setCommonStyle());
    }

    private void updateTotalRange() {
        double totalFrames = Data.dispatcher.totalFrames()-1;
        double totalWidth = TrackData.totalSliderPane.getBoundsInLocal().getWidth() - TrackData.currentFrameLabel.getWidth() / 2 ;
        double frameWidth = totalWidth/totalFrames;

        Pane rangePane = TrackData.totalRangePane;
        rangePane.setTranslateX(getStart()* frameWidth);
        rangePane.setPrefWidth((getEnd() - getStart()) * frameWidth);
    }



    private void updateTrackRange() {
        // frame slider range
        List<Node> list = TrackData.sliderPane.getChildren();
        list.forEach(e->((SliderFrame)e).setCommonStyle());
        int startIndex = Math.min(getStart(), getEnd()+1);
        int endIndex = Math.max(getStart(), getEnd()+1);
        list.subList(startIndex, endIndex).forEach(e->((SliderFrame)e).setActiveStyle());
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }
    public void reset() {
        start = 0;
        end = orgEnd;
        isStart = false;
        isEnd = false;

        update();
    }

    public void setOrgEnd(int end) {
        this.end = end;
        this.orgEnd = end;
    }

    public int getOrgEnd() {
        return orgEnd;
    }

    public boolean isEnd() {
        return isEnd;
    }

    @Override
    public String toString() {
        return "PlayRange{" +
                "start=" + start +
                ", end=" + end +
                ", orgEnd=" + orgEnd +
                ", isStart=" + isStart +
                ", isEnd=" + isEnd +
                '}';
    }
}
