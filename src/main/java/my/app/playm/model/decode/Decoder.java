package my.app.playm.model.decode;

import my.app.playm.entity.frame.ImageFrame;

import java.awt.image.BufferedImage;
import java.util.List;

public interface Decoder {

    List<BufferedImage> decode(String source);
    List<ImageFrame> decodeImages(String source);
    void decodeAndEvent(String source);
    int getFrameRate(String source);
}
