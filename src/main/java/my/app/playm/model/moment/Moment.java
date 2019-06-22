package my.app.playm.model.moment;

import javafx.scene.Node;
import lombok.Data;
import my.app.playm.entity.frame.ImageFrame;

import java.util.List;
@Data
public class Moment {
    private List<Node> listFrames;
    private List<Node> listFramesRemoved;
    private List<ImageFrame> listImages;
}
