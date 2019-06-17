package my.app.playm.model;

import my.app.playm.PlayMTest;
import my.app.playm.controller.Data;
import my.app.playm.entity.frame.Frame;
import my.app.playm.entity.frame.ImageFrame;
import my.app.playm.model.repo.FrameRepository;
import my.app.playm.model.repo.VideoRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;


public class FrameTest extends PlayMTest {

    @Autowired
    FrameRepository frameRepo;
    @Autowired
    VideoRepository videoRepo;

    @Test
    public void checkForCorrectTestSetup() {
        assertThat(frameRepo.size()).isEqualTo(Data.debugTotalFrames);
        assertThat(videoRepo.size()).isEqualTo(Data.debugTotalFrames);
        assertThat(frameRepo.size()).isEqualTo(videoRepo.size());
    }

    @Test
    public void that_imageFrameNum_is_same_as_frameNum() {
        Frame frame = getFrame(10);
        ImageFrame imageFrame = videoRepo.getImageByNum(frame.getNum());
        assertThat(frame.getNum()).isEqualTo(imageFrame.getNum());
    }

    @Test
    public void that_frame_has_correct_id() {
        Frame frame = getFrame(10);
        assertThat(frame.getNum()).isEqualTo(10);
    }
    @Test
    public void frame_drag_is_correct() throws Exception{
        Frame frame = getFrame(10);
        dragRight(frame.getNum(), 1);
        assertThat(videoRepo.size()).isEqualTo(Data.debugTotalFrames+1);
        assertThat(frame.getTranslateX()).isEqualTo(FrameRepository.frameWidth.get()*(frame.getNum()+1));
        Frame emptyFrame = frameRepo.get(10);
        assertThat(emptyFrame.isEmpty()).isTrue();
    }
    @Test
    public void when_dragged_less_than_framewidth_then_nothing_happens() throws Exception{
        Frame frame = getFrame(10);
        dragRight(frame.getNum(), 3);
        double beforeValue = frame.getTranslateX();
        dragLeft(frame.getNum(), 0.9);
        double afterValue = frame.getTranslateX();
        assertThat(beforeValue).isEqualTo(afterValue);
    }
    @Test
    public void previous_frame_prevent_from_dragging() throws Exception{
        Frame frame = getFrame(10);
        double beforeValue = frame.getTranslateX();
        dragLeft(frame.getNum(), 5);
        double afterValue = frame.getTranslateX();
        assertThat(beforeValue).isEqualTo(afterValue);
    }
}