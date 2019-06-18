package my.app.playm.model.time;

import javafx.scene.media.MediaPlayer;
import lombok.extern.log4j.Log4j;
import my.app.playm.controller.Data;
import my.app.playm.controller.Dispatcher;
import my.app.playm.model.decode.DecoderAudio;
import my.app.playm.model.player.PlaybackMode;
import my.app.playm.model.repo.FrameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Log4j
@Component
public class TimerThread implements Timer {
    @Autowired
    private PlayRange range;
    @Autowired
    private DecoderAudio decoderAudio;
    @Autowired
    private FrameRepository frameRepository;

    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    // process is for cancelling playback
    private ScheduledFuture process;


    private Runnable playForward = () -> {
        Data.currentFrame++;
        // return back to start frame if reaches the end frame (looping playback)
        if (range.isEnd()) {
            if (Data.currentFrame > range.getEnd()) {
                log.debug(String.format("Frame %d is more than range end - %d", Data.currentFrame, range.getEnd()));
                reset();
            }
        } else if (Data.playMode == PlaybackMode.ACTUAL) {
            if (Data.currentFrame > frameRepository.size() - 1) reset();
        } else {
            if (Data.currentFrame > range.getOrgEnd()) reset();
        }


        // skip every n-th frame, if Data.frameStep more than 1
        if (Data.currentFrame % Data.frameStep != 0) return;


        Dispatcher.updateFrame(Data.currentFrame);
    };
    private Runnable playBackward = () -> {
        // return back to start frame if reaches the end frame (looping playback)
        if (--Data.currentFrame < 0) Data.currentFrame = frameRepository.size() - 1;
        if (Data.currentFrame % Data.frameStep != 0) return;

        Dispatcher.updateFrame(Data.currentFrame);
    };

    private Runnable launcher = playForward;

    @Override
    public void start() {
        process = executor.scheduleAtFixedRate(launcher, 0, 1000 / Data.framerate, TimeUnit.MILLISECONDS);
    }

    @Override
    public void stop() {
        process.cancel(false);
    }

    @Override
    public void reset() {
        Data.currentFrame = range.getStart();
        MediaPlayer audioPlayer = Data.player.getAudioPlayer();
        if (decoderAudio.isAudio()) audioPlayer.seek(audioPlayer.getStartTime());
        log.debug(String.format("Reset to %d frame", Data.currentFrame));
    }

    @Override
    public void close() {
        executor.shutdown();
    }

    @Override
    public void reversePlay() {
        launcher = playBackward;
    }

    @Override
    public void normalPlay() {
        launcher = playForward;
    }
}
