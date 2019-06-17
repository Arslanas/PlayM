package my.app.playm.model.moment;

import javafx.scene.Node;
import my.app.playm.entity.frame.ImageFrame;

import java.util.List;

public class Moment {

    private List<Node> listFrames;
    private List<Node> listFramesRemoved;
    private List<ImageFrame> listImages;

    public List<Node> getListFrames() {
        return listFrames;
    }

    public void setListFrames(List<Node> listFrames) {
        this.listFrames = listFrames;
    }

    public List<Node> getListFramesRemoved() {
        return listFramesRemoved;
    }

    public void setListFramesRemoved(List<Node> listFramesRemoved) {
        this.listFramesRemoved = listFramesRemoved;
    }

    public List<ImageFrame> getListImages() {
        return listImages;
    }

    public void setListImages(List<ImageFrame> listImages) {
        this.listImages = listImages;
    }

    @Override
    public String toString() {
        return "SaveMoment{" +
                "listFrames=" + listFrames +
                ", listFramesRemoved=" + listFramesRemoved +
                ", listImageFrames=" + listImages +
                '}';
    }
}
