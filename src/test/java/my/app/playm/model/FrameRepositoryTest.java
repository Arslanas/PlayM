package my.app.playm.model;

import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import lombok.extern.log4j.Log4j;
import my.app.playm.PlayMTest;
import my.app.playm.controller.Data;
import my.app.playm.controller.TrackData;
import my.app.playm.entity.frame.Frame;
import my.app.playm.entity.frame.FrameRemoved;
import my.app.playm.model.repo.FrameRepository;
import my.app.playm.model.repo.VideoRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
@Log4j
public class FrameRepositoryTest extends PlayMTest {

    @Autowired
    FrameRepository frameRepo;
    @Autowired
    VideoRepository videoRepo;

    @Test
    public void when_delete_pressed_then_frame_deleted() throws Exception {
        changeFrame(10);
        press(KeyCode.DELETE);
        release(KeyCode.DELETE);
        assertThat(frameRepo.size()).isEqualTo(Data.debugTotalFrames - 1);
        assertThat(frameRepo.get(10).getNum()).isEqualTo(11);
    }
    @Test
    public void when_frame_deleted_then_frameRemoved_created() throws Exception {
        randomizeFrames();
        FrameRemoved frameRemoved = getFrameRemoved(11);
        assertThat(frameRemoved.getParent()).isSameAs(TrackData.storePane);
    }
    @Test
    public void when_doubleClick_on_frameRemoved_then_frame_recovered_at_recoverIndex() throws Exception {
        randomizeFrames();
        FrameRemoved frameRemoved = getFrameRemoved(11);
        int removedFrameNum = frameRemoved.getNum();
        doubleClickOn(frameRemoved);
        release(MouseButton.PRIMARY);
        assertThat(frameRepo.get(frameRemoved.getRecoverIndex()).getNum()).isEqualTo(removedFrameNum);
    }

    @Test
    public void when_delete_after_drag_pressed_then_frame_deleted() throws Exception {
        dragRight(10, 3);
        changeFrame(10);
        press(KeyCode.DELETE);
        release(KeyCode.DELETE);
        assertThat(frameRepo.get(9).getNum()).isEqualTo(9);
        assertThat(frameRepo.get(9).isEmpty()).isFalse();
    }

    @Test
    public void when_dragged_then_FrameEmpty_and_ImageFrameEmpty_added() throws Exception {
        Frame frame = (Frame) Data.appRoot.lookup("#frame_10");
        drag(frame).moveBy(FrameRepository.frameWidth.get() * 3, 0);
        release(MouseButton.PRIMARY);
        List<Frame> nodes = frameRepo.getList().subList(10, 13);
        assertThat(nodes).allSatisfy(e -> assertThat(e.isEmpty()).isTrue());
    }

    @Test
    public void when_dragged_frame_0_then_it_not_moved() throws Exception {
        dragRight(0, 3);
        assertThat(getFrame(0).getTranslateX()).isEqualTo(0);
    }

    @Test
    public void getClosestNotEmptyFrameIndexTest() throws Exception {
        dragRight(10, 3);
        assertThat(frameRepo.getClosestNotEmptyFrameIndex(12)).isEqualTo(9);
        assertThat(frameRepo.getClosestNotEmptyFrameIndex(13)).isEqualTo(9);
        dragLeft(10, 3);
        assertThat(frameRepo.getClosestNotEmptyFrameIndex(10)).isEqualTo(9);
        assertThat(frameRepo.getClosestNotEmptyFrameIndex(9)).isEqualTo(8);
        assertThat(frameRepo.getClosestNotEmptyFrameIndex(0)).isEqualTo(0);
        assertThat(frameRepo.getClosestNotEmptyFrameIndex(1)).isEqualTo(0);
        assertThat(frameRepo.getClosestNotEmptyFrameIndex(19)).isEqualTo(18);
    }

    @Test
    public void frame_repository_is_synchronized_with_videoRepository() throws Exception {
        randomizeFrames();
        assertThat(videoRepo.size()).isEqualTo(frameRepo.size());
        assertThat(IntStream.range(0, frameRepo.size())).allSatisfy(i ->
                assertThat(frameRepo.get(i).getNum()).isEqualTo(videoRepo.get(i).getNum())
        );
    }
}
