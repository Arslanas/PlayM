package my.app.playm;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import my.app.playm.config.ControllerConfig;
import my.app.playm.controller.Data;
import my.app.playm.controller.Dispatcher;
import my.app.playm.controller.Util;;
import my.app.playm.entity.frame.Frame;
import my.app.playm.entity.frame.FrameRemoved;
import my.app.playm.entity.frame.SliderFrame;
import my.app.playm.model.repo.FrameRepository;
import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootApplication
public class PlayMTest extends ApplicationTest {
    ConfigurableApplicationContext context ;
    @Autowired
    private ControllerConfig.View view;

    @Override
    public void start(Stage stage) throws Exception {
        String[] args = {};
        context = SpringApplication.run(getClass(), args);
        context.getAutowireCapableBeanFactory().autowireBean(this);

        Scene scene = new Scene(view.getRoot(), 1280, 700);

        scene.getStylesheets().add(getClass().getResource("/styles/PlayM.css").toExternalForm());
        stage.setTitle("PlayM");
        stage.setScene(scene);
        stage.centerOnScreen();

        stage.show();
        Dispatcher.onSceneLoaded(scene, stage);
        Util.loadSequence();
    }
    @Override
    public void stop() throws Exception {
        Data.timer.close();
        super.stop();
        context.close();
    }
    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
        Data.isDecodeComplete = false;
        FxToolkit.hideStage();
        release(new KeyCode[]{});
        release(new MouseButton[]{});
    }

    public void randomizeFrames() {
        dragRight(10, 3);
        dragRight(13, 3);
        changeFrame(14);
        dragRight(19, 3);
        press(KeyCode.DELETE);
        release(KeyCode.DELETE);
        press(KeyCode.DELETE);
        release(KeyCode.DELETE);
    }

    public void changeFrame(int num) {
        SliderFrame frame = (SliderFrame) Data.appRoot.lookup("#sliderFrame_" + num);
        clickOn(frame);
        release(MouseButton.PRIMARY);
    }

    public void dragRight(int frameNum, double offset) {
        Frame frame = getFrame(frameNum);
        drag(frame).moveBy(FrameRepository.frameWidth.get() * offset, 0);
        release(MouseButton.PRIMARY);
    }

    public void dragLeft(int frameNum, double offset) {
        Frame frame = getFrame(frameNum);
        drag(frame).moveBy(FrameRepository.frameWidth.get() * offset * -1, 0);
        release(MouseButton.PRIMARY);
    }

    public Frame getFrame(int frameNum) {
        return (Frame) Data.appRoot.lookup("#frame_" + frameNum);
    }
    public FrameRemoved getFrameRemoved(int frameNum) {
        return (FrameRemoved) Data.appRoot.lookup("#frameRemoved_" + frameNum);
    }
}
