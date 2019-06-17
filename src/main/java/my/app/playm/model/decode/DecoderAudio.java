package my.app.playm.model.decode;

import com.xuggle.xuggler.IAudioSamples;

import java.util.List;

public interface DecoderAudio {

    void decodeAudio(String source);
    List<IAudioSamples> getSamples();
    boolean isAudio();
}
