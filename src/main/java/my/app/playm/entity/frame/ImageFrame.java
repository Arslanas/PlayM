package my.app.playm.entity.frame;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import lombok.extern.log4j.Log4j;

import javax.imageio.ImageIO;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

@Log4j
public class ImageFrame implements Serializable {
    static final long serialVersionUID = -7588980448693010499L;
    transient private Image image;
    private final int num;
    private boolean empty;

    public ImageFrame(Image image, int num) {
        this.image = image;
        this.num = num;
    }
    public ImageFrame(ImageFrame imageFrame) {
        this.image = imageFrame.getImage();
        this.num = imageFrame.getNum();
    }

    public ImageFrame(ImageFrame imageFrame, boolean isEmpty) {
        this(imageFrame);
        empty = isEmpty;
    }
    public ImageFrame(Image image, int num, boolean isEmpty) {
        this(image, num);
        empty = isEmpty;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public int getNum() {
        return num;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ImageFrame that = (ImageFrame) o;

        return num == that.num;
    }

    @Override
    public int hashCode() {
        int result = num;
        result = 31 * result + (empty ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ImageFrame{ num = " + num + " , empty = " + empty + " }";
    }

    private void writeObject(ObjectOutputStream out) throws Exception {
        log.debug("Writing image - " + num);
        out.defaultWriteObject();
        ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", out);
    }

    private void readObject(ObjectInputStream in) throws Exception {
        in.defaultReadObject();
        image = SwingFXUtils.toFXImage(ImageIO.read(in), null);
    }

    public boolean isEmpty() {
        return empty;
    }
}
