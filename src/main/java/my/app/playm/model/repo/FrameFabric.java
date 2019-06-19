package my.app.playm.model.repo;

import my.app.playm.entity.frame.Frame;
import my.app.playm.entity.frame.FrameRemoved;
import my.app.playm.entity.frame.SliderFrame;

public interface FrameFabric {

    Frame createFrame(int num);

    Frame createEmpty(int num);

    FrameRemoved createFrameRemoved(int num, int recoverIndex);

    SliderFrame createSliderFrame(int num);
}
