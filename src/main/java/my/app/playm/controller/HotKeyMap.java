package my.app.playm.controller;

import javafx.scene.input.KeyEvent;
import lombok.extern.log4j.Log4j;
import my.app.playm.entity.Command;
import my.app.playm.entity.frame.Frame;
import my.app.playm.model.moment.MomentRepository;
import my.app.playm.model.player.PlaybackMode;
import my.app.playm.model.player.Player;
import my.app.playm.model.repo.FrameRepository;
import my.app.playm.model.repo.VideoService;
import my.app.playm.model.time.Timer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Log4j
@Component
public class HotKeyMap {
    private final Map<String, Command> map = new HashMap<>();
    private final Map<String, Command> mapShift = new HashMap<>();
    private final Map<String, Command> mapCtrl = new HashMap<>();

    @Autowired
    private Timer timer;
    @Autowired
    private MomentRepository momentRepo;
    @Autowired
    private VideoService videoService;
    @Autowired
    private FrameRepository frameRepo;
    @Autowired
    private Player player;

    public HotKeyMap() {
        initMap();
    }

    private void initMap() {
        map.put("X", new Command("mirror", () -> mirror()));
        map.put("D", new Command("changeVideoMode", () -> changeVideoMode()));
        map.put("1", new Command("frameStep = 1", () -> Data.frameStep = 1));
        map.put("2", new Command("frameStep = 2", () -> Data.frameStep = 2));
        map.put("3", new Command("frameStep = 3", () -> Data.frameStep = 3));
        map.put("4", new Command("frameStep = 4", () -> Data.frameStep = 4));
        map.put("8", new Command("frameStep = 8", () -> Data.frameStep = 8));
        map.put("9", new Command("Change speed to original", () -> changeSpeed(Data.framerate)));
        map.put("Subtract", new Command("Reduce speed", () -> changeSpeed((int) (Data.framerate / 1.2))));
        map.put("Add", new Command("Increase speed", () -> changeSpeed((int) (Data.framerate * 1.2))));
        map.put("Delete", new Command("Delete frame and shift", () -> frameRepo.remove(Data.currentFrame)));
        map.put("Backspace", new Command("Delete frame", () -> deleteFrame()));
        map.put("F1", new Command("Help", () -> hotkeyHelp()));
        map.put("F5", new Command("Play forward", () -> playForward()));
        map.put("F6", new Command("Play backward", () -> playBackward()));
        map.put("F10", new Command("Export", () -> videoService.export(Data.dispatcher.getExportPath(Data.videoSource))));
        map.put("G", new Command("Add empty", () -> frameRepo.addEmpty(Data.currentFrame)));
        map.put("Left", new Command("Prev frame", () -> Data.dispatcher.updateFrame(--Data.currentFrame)));
        map.put("Right", new Command("Next frame", () -> Data.dispatcher.updateFrame(++Data.currentFrame)));
        map.put("Space", new Command("Play and pause", () -> playPause()));
        map.put("B", new Command("Play and stop", () -> playStop()));

        mapCtrl.put("Z", new Command("SaveMoment", () -> momentRepo.restoreState()));

        mapShift.put("E", new Command("Sound reduce", () -> TrackData.soundSlider.decrement()));
        mapShift.put("R", new Command("Sound increase", () -> TrackData.soundSlider.increment()));
    }


    private void playForward() {
        timer.normalPlay();
        restartPlay();
    }

    private void restartPlay() {
        if (player.isPlaying()) {
            player.pause();
            player.play();
        }
    }

    private void playBackward() {
        timer.reversePlay();
        restartPlay();
    }

    public void process(KeyEvent e) {
        String key = e.getCode().getName();
        log.debug(key);

        if (e.isControlDown()) {
            if (mapCtrl.containsKey(key)) {
                mapCtrl.get(key).run();
                return;
            }
        }
        if (e.isShiftDown()) {
            if (mapShift.containsKey(key)) {
                mapShift.get(key).run();
                return;
            }
        }
        if (map.containsKey(key)) {
            map.get(key).run();
            return;
        }
    }

    private void hotkeyHelp() {
        map.forEach((key, command) -> System.out.println(String.format("%s - %s", key, command.getDescription())));
        mapShift.forEach((key, command) -> System.out.println(String.format("SHIFT %s - %s", key, command.getDescription())));
        mapCtrl.forEach((key, command) -> System.out.println(String.format("CTRL %s - %s", key, command.getDescription())));
    }

    private void deleteFrame() {

        int currentFrame = Data.currentFrame;
        Frame frame = frameRepo.get(currentFrame);
        if (frame.isEmpty() || currentFrame == 0) return;

        frameRepo.makeEmpty(frame);
    }

    private void changeSpeed(int i) {
        Data.framerate = i;
        restartPlay();
    }

    private void changeVideoMode() {
        Data.playMode = Data.playMode == PlaybackMode.ORIGINAL ? PlaybackMode.ACTUAL : PlaybackMode.ORIGINAL;
        if (Data.playMode == PlaybackMode.ORIGINAL) TrackData.videoModeCover.setVisible(true);
        if (Data.playMode == PlaybackMode.ACTUAL) TrackData.videoModeCover.setVisible(false);
        Data.dispatcher.updateFrame(Data.currentFrame);
    }

    private void mirror() {
        if (Data.mirrorProperty.get() > 0) Data.mirrorProperty.setValue(-1);
        else Data.mirrorProperty.setValue(1);
    }


    private void playPause() {
        if (player.isPlaying()) {
            player.pause();
            return;
        }
        player.play();
    }

    private void playStop() {
        if (player.isPlaying()) {
            player.stop();
            return;
        }
        player.play();
    }
}
