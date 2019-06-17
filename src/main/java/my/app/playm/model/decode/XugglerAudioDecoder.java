package my.app.playm.model.decode;

import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.*;
import lombok.extern.log4j.Log4j;
import my.app.playm.controller.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Log4j
@Component
public class XugglerAudioDecoder implements DecoderAudio{

    private final List<IAudioSamples> samples = new ArrayList<>();

    private IAudioSamples.Format sampleFormat = IAudioSamples.Format.FMT_S16;
    private ICodec.ID audioCodec = ICodec.ID.CODEC_ID_MP3;

    private boolean isAudio;



    @Override
    public void decodeAudio(String source){
        samples.clear();

        String target = Data.prop.getAudioSource();

        IContainer container = getContainer(source);
        IStreamCoder audioCoder = findAudioCoder(container);
        if (audioCoder == null) return;
        isAudio  = true;

        audioCoder.open(null, null);
        IPacket packet = IPacket.make();
        IMediaWriter writer = ToolFactory.makeWriter(target);


        // Using stream 1 'cause there is also a video stream.
        // For an audio only file you should use stream 0.
        writer.addAudioStream(0, 0, audioCodec, audioCoder.getChannels(), audioCoder.getSampleRate());

        IAudioResampler resampler = IAudioResampler.make(audioCoder.getChannels(),
                audioCoder.getChannels(),
                audioCoder.getSampleRate(),
                audioCoder.getSampleRate(),
                sampleFormat,
                audioCoder.getSampleFormat());

        // The stream 1 here is consistent with the stream we added earlier.
        writer.getContainer().getStream(0).getStreamCoder().setSampleFormat(sampleFormat);

        while (container.readNextPacket(packet) >= 0) {
            if (packet.getStreamIndex() != audioCoder.getStream().getIndex()) continue;

            IAudioSamples rawSample = IAudioSamples.make(512, audioCoder.getChannels(), audioCoder.getSampleFormat());

            int offset = 0;

            while (offset < packet.getSize()) {
                int bytesDecoded = audioCoder.decodeAudio(rawSample, packet, offset);
                offset += bytesDecoded;
                if (rawSample.isComplete()){
                    IAudioSamples sample = IAudioSamples.make(512, audioCoder.getChannels(), sampleFormat);
                    resampler.resample(sample, rawSample, 0);
                    samples.add(sample);
                    writer.encodeAudio(0, sample);
                }
            }
        }

        audioCoder.close();
        container.close();
    }
    @Override
    public List<IAudioSamples> getSamples(){
        return samples;
    }

    private  IStreamCoder findAudioCoder(IContainer container) {
        int numStreams = container.getNumStreams();
        for (int i = 0; i < numStreams; i++) {
            IStreamCoder streamCoder = container.getStream(i).getStreamCoder();
            if (streamCoder.getCodecType() == ICodec.Type.CODEC_TYPE_AUDIO) return streamCoder;
        }
        return null;
    }

    private  IContainer getContainer(String source) {
        IContainer container = IContainer.make();
        container.open(source, IContainer.Type.READ, null);
        return container;
    }

    public boolean isAudio() {
        return isAudio;
    }
}
