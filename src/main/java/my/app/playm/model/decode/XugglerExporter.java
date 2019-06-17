package my.app.playm.model.decode;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.*;
import javafx.embed.swing.SwingFXUtils;
import lombok.extern.log4j.Log4j;
import my.app.playm.controller.Data;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Log4j
public class XugglerExporter  {

    private IAudioSamples.Format sampleFormat = IAudioSamples.Format.FMT_S16;
    private ICodec.ID videoCodec = ICodec.ID.CODEC_ID_MPEG4;
    private ICodec.ID audioCodec = ICodec.ID.CODEC_ID_MP3;

    public void export(String target) {

        log.debug("Start exporting to " + target);

        List<BufferedImage> images = getImages();
        String source = Data.videoSource;

        IMediaReader reader = ToolFactory.makeReader(source);
        reader.getContainer().open(source, IContainer.Type.READ, null);
        int width = reader.getContainer().getStream(0).getStreamCoder().getWidth();
        int height = reader.getContainer().getStream(0).getStreamCoder().getHeight();
        int channels = reader.getContainer().getStream(1).getStreamCoder().getChannels();
        int sampleRate = reader.getContainer().getStream(1).getStreamCoder().getSampleRate();

        IMediaWriter writer = ToolFactory.makeWriter(target);


        //Video
        long frameRate = 24 / 1000;
        long nextFrameTime = 0;
        int videoIdx = 0;
        int audioIdx = 1;

        writer.addVideoStream(videoIdx, videoIdx, videoCodec, IRational.make(frameRate), width, height);
        writer.addAudioStream(audioIdx, audioIdx, audioCodec, channels, sampleRate);

        int i = 0;
        while (i < images.size()) {
            BufferedImage videoImage = images.get(i);
            log.debug("Encoding - "+videoImage);
            writer.encodeVideo(videoIdx, videoImage, nextFrameTime, TimeUnit.MILLISECONDS);
            nextFrameTime += 1000 / 24;
            i++;
        }

        // Audio
        IStreamCoder audioCoder = writer.getContainer().getStream(audioIdx).getStreamCoder();
        audioCoder.setSampleFormat(sampleFormat);

        Data.decoderAudio.getSamples().forEach(sample -> writer.encodeAudio(audioIdx, sample));

        writer.close();
        log.debug("Export complete");

    }

    private List<BufferedImage> getImages() {
        return Data.videoRepo.getList().stream()
                .map(frame -> SwingFXUtils.fromFXImage(frame.getImage(),null))
                .map(bimage -> {
                    BufferedImage newImage = new BufferedImage(bimage.getWidth(), bimage.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
                    newImage.getGraphics().drawImage(bimage, 0, 0, null);
                    return newImage;
                })
                .collect(Collectors.toList());
    }

}
