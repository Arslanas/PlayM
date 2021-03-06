package my.app.playm.model.decode;

import lombok.extern.log4j.Log4j;
import my.app.playm.controller.Data;
import my.app.playm.controller.Dispatcher;
import my.app.playm.entity.frame.ImageFrame;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.List;

@Log4j
public class SerializedDecoder implements Decoder {
    @Override
    public List<BufferedImage> decode(String source) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<ImageFrame> decodeImages(String source) {
        List<ImageFrame> list = null;
        log.debug("Start video deserialization");
        try (FileInputStream file = new FileInputStream(source);
             ObjectInputStream in = new ObjectInputStream(file)) {
            list = (List<ImageFrame>) in.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.debug("Complete video deserialization");
        return list;
    }

    @Override
    public void decodeAndEvent(String source) {
        List<ImageFrame> list = decodeImages("video.data");
        list.forEach(imageFrame -> Data.dispatcher.onNewImageFrame(imageFrame));
    }

    @Override
    public int getFrameRate(String source) {
        throw new UnsupportedOperationException();
    }
}
