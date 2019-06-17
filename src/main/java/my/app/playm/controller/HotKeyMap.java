package my.app.playm.controller;

import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import lombok.extern.log4j.Log4j;
import my.app.playm.entity.Command;
import my.app.playm.entity.frame.Frame;
import my.app.playm.model.player.PlaybackMode;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Log4j
@Component
public class HotKeyMap {
    private final Map<String, Command> map = new HashMap<>();
    private final Map<String, Command> mapShift = new HashMap<>();
    private final Map<String, Command> mapCtrl = new HashMap<>();

    public HotKeyMap(){
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
        map.put("Delete", new Command("Delete frame and shift", () -> Data.frameRepo.remove(Data.currentFrame)));
        map.put("Backspace", new Command("Delete frame", () -> deleteFrame()));
        map.put("F1", new Command("Help", () -> hotkeyHelp()));
        map.put("F5", new Command("Play forward", () -> playForward()));
        map.put("F6", new Command("Play backward", () -> playBackward()));
        map.put("F10", new Command("Export", () -> Data.videoService.export(Util.getExportPath(Data.videoSource))));
        map.put("G", new Command("Add empty", () -> Data.frameRepo.addEmpty(Data.currentFrame)));
        map.put("Left", new Command("Prev frame", () -> Dispatcher.updateFrame(--Data.currentFrame)));
        map.put("Right", new Command("Next frame", () -> Dispatcher.updateFrame(++Data.currentFrame)));
        map.put("Space", new Command("Play and pause", () -> playPause()));
        map.put("B", new Command("Play and stop", () -> playStop()));

        mapCtrl.put("Z", new Command("SaveMoment", () -> Data.momentRepo.restoreState()));

        mapShift.put("E", new Command("Sound reduce", () -> TrackData.soundSlider.decrement()));
        mapShift.put("R", new Command("Sound increase", () -> TrackData.soundSlider.increment()));
    }


    private void playForward() {
        Data.timer.normalPlay();
        if (Data.player.isPlaying()) {
            Data.player.pause();
            Data.player.play();
        }
    }

    private void playBackward() {
        Data.timer.reversePlay();
        if (Data.player.isPlaying()) {
            Data.player.pause();
            Data.player.play();
        }
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

    private static void deleteFrame() {

        int currentFrame = Data.currentFrame;
        Frame frame = Data.frameRepo.get(currentFrame);
        if (frame.isEmpty() || currentFrame == 0) return;

        Data.frameRepo.makeEmpty(frame);
    }

    private void changeSpeed(int i) {
        Data.framerate = i;
        if (Data.player.isPlaying()) {
            Data.player.pause();
            Data.player.play();
        }
    }

    private void changeVideoMode() {
        Data.playMode = Data.playMode == PlaybackMode.ORIGINAL ? PlaybackMode.ACTUAL : PlaybackMode.ORIGINAL;
        if (Data.playMode == PlaybackMode.ORIGINAL) TrackData.videoModeCover.setVisible(true);
        if (Data.playMode == PlaybackMode.ACTUAL) TrackData.videoModeCover.setVisible(false);
        Dispatcher.updateFrame(Data.currentFrame);
    }

    private void mirror() {
        if (Data.mirrorProperty.get() > 0) Data.mirrorProperty.setValue(-1);
        else Data.mirrorProperty.setValue(1);
    }


    private void playPause() {
        if (Data.player.isPlaying()) {
            Data.player.pause();
            return;
        }
        Data.player.play();
    }

    private void playStop() {
        if (Data.player.isPlaying()) {
            Data.player.stop();
            return;
        }
        Data.player.play();
    }
}
