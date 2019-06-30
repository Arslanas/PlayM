package my.app.playm;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j;
import my.app.playm.config.ControllerConfig;
import my.app.playm.controller.Dispatcher;
import my.app.playm.controller.Properties;
import my.app.playm.controller.TrackController;
import my.app.playm.controller.Util;
import my.app.playm.model.repo.VideoService;
import my.app.playm.model.time.Timer;
import my.app.playm.socket.PlayServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@Log4j
@SpringBootApplication
public class PlayM extends Application {
    private static String[] savedArgs;
    private ConfigurableApplicationContext context;
    @Autowired
    private PlayServer playServer;
    @Autowired
    private Properties props;
    @Autowired
    private ControllerConfig.View view;
    @Autowired
    private Dispatcher dispatcher;

    @Override
    public void start(Stage stage) throws Exception {
        context = SpringApplication.run(getClass(), savedArgs);
        context.getAutowireCapableBeanFactory().autowireBean(this);

        playServer.start();

        Scene scene = setupAndShowScene(stage);

        dispatcher.onSceneLoaded(scene, stage);

        loadVideo();
        //dispatcher.loadSequence(50);
    }

    private void loadVideo() throws Exception {
        if (savedArgs.length > 0) dispatcher.decodeVideo(savedArgs[0]);
        else dispatcher.decodeVideo(Util.getSource(props.getDebugVideoSource()));
    }

    @Override
    public void stop() throws Exception {
        dispatcher.closeApp();
        super.stop();
    }

    public static void main(String[] args) {
        savedArgs = args;
        Application.launch(PlayM.class, args);
    }

    private Scene setupAndShowScene(Stage stage) {
        Scene scene = new Scene(view.getRoot(), 1280, 700);
        Util.chooseMonitor(stage);
        scene.getStylesheets().add(getClass().getResource("/styles/PlayM.css").toExternalForm());
        stage.setTitle("PlayM");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
        return scene;
    }

}
