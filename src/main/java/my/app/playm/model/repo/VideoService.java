package my.app.playm.model.repo;

public interface VideoService {
    void decodeVideo(String source);

    void serializeImageFrames();

    void export(String target);

    void decodeAudio(String source);
}
