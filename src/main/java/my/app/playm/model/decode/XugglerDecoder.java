package my.app.playm.model.decode;

import com.xuggle.xuggler.*;
import com.xuggle.xuggler.video.ConverterFactory;
import com.xuggle.xuggler.video.IConverter;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import lombok.extern.log4j.Log4j;
import my.app.playm.controller.Data;
import my.app.playm.controller.Dispatcher;
import my.app.playm.entity.frame.ImageFrame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

@Log4j
@Component
public class XugglerDecoder implements Decoder {

    @Override
    public List<BufferedImage> decode(String source) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<ImageFrame> decodeImages(String source) {
        return decodeEvented(source);
    }

    @Override
    public int getFrameRate(String source) {
        IContainer container = getContainer(source);
        // !!!!write proper method for getting video stream
        IStream stream = getVideoStream(container);
        IStreamCoder videoCoder = getStreamCoder(stream);
        return (int)videoCoder.getFrameRate().getValue();
    }

    @Override
    public void decodeAndEvent(String source) {
        decodeEvented(source);
    }

    private List<ImageFrame> decodeEvented(String source) {
        IContainer container = getContainer(source);
        // !!!!write proper method for getting video stream
        IStream stream = getVideoStream(container);
        IStreamCoder videoCoder = getStreamCoder(stream);

        Data.framerate = (int) videoCoder.getFrameRate().getValue();
        log.debug(String.format("Framerate - " + Data.framerate));

        IPacket packet = IPacket.make();
        List<ImageFrame> frameList = decodeContainer(container, stream, videoCoder, packet);
        videoCoder.close();
        container.close();
        return frameList;
    }


    private List<ImageFrame> decodeContainer(IContainer container, IStream stream, IStreamCoder videoCoder, IPacket packet) {
        List<ImageFrame> frameList = new ArrayList<>();
        long timestamp = 0;
        int imageNum = 0;
        IConverter converter = null;
        // Read data from container into packet by chunks untill it reaches the end of data
        while (container.readNextPacket(packet) >= 0) {
            // If it is not video stream, then skip it. It can be audio stream or subtitle
            if (packet.getStreamIndex() != stream.getIndex()) continue;

            IVideoPicture picture = IVideoPicture.make(videoCoder.getPixelType(), videoCoder.getWidth(), videoCoder.getHeight());

            if (converter == null)
                converter = ConverterFactory.createConverter(ConverterFactory.XUGGLER_BGR_24, picture);

            // Decode data of packet into picture and if picture is complete then add it to list of pictures
            int offset = 0;
            while (offset < packet.getSize()) {
                int bytesDecoded = videoCoder.decodeVideo(picture, packet, offset);
                offset += bytesDecoded;
                if (picture.isComplete()) {
                    ImageFrame imageFrame = convert(picture, converter, imageNum);
                    Data.dispatcher.onNewImageFrame(imageFrame);
                    imageNum++;
                    frameList.add(imageFrame);
                }
            }
        }
        // Fixing Xuggler bug in order to get missing frames - seek to the beginning of data
        // and start decoding packets again untill current decoded picture`s timestamp is not less than previous picture`s timestamp
        container.seekKeyFrame(stream.getIndex(), 0, 0, 0, 0);

        // Continue decoding as in previous cycle except lines commented below
        while (container.readNextPacket(packet) >= 0) {
            if (packet.getStreamIndex() != 0) continue;

            IVideoPicture picture = IVideoPicture.make(videoCoder.getPixelType(), videoCoder.getWidth(), videoCoder.getHeight());

            int offset = 0;
            while (offset < packet.getSize()) {
                int bytesDecoded = videoCoder.decodeVideo(picture, packet, offset);
                offset += bytesDecoded;
                // This line checks for timestamp and if it less than previous then stop decoding
                if (picture.getTimeStamp() <= timestamp) break;
                if (picture.isComplete()) {
                    ImageFrame imageFrame = convert(picture, converter, imageNum);
                    Data.dispatcher.onNewImageFrame(imageFrame);
                    // This line saves timestamp to compare it with next decoded picture`s timestamp
                    timestamp = picture.getTimeStamp();
                    imageNum++;
                    frameList.add(imageFrame);
                }
            }

            picture = null;
        }

        packet = null;
        return frameList;
    }

    private ImageFrame convert(IVideoPicture picture, IConverter converter, int num) {
        BufferedImage bufferedImage = converter.toImage(picture);
        Image image = SwingFXUtils.toFXImage(bufferedImage, null);
        return new ImageFrame(image, num);
    }


    private IStreamCoder getStreamCoder(IStream stream) {
        IStreamCoder videoCoder = stream.getStreamCoder();
        videoCoder.open(null, null);
        return videoCoder;
    }

    private IStream getVideoStream(IContainer container) {
        return container.getStream(0);
    }

    private IContainer getContainer(String source) {
        IContainer container = IContainer.make();
        container.open(source, IContainer.Type.READ, null);
        return container;
    }

}
