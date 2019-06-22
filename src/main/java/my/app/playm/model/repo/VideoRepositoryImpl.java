package my.app.playm.model.repo;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.extern.log4j.Log4j;
import my.app.playm.controller.Data;
import my.app.playm.entity.frame.Frame;
import my.app.playm.entity.frame.ImageFrame;
import my.app.playm.model.moment.Moment;
import my.app.playm.model.player.PlaybackMode;
import my.app.playm.model.moment.MomentOriginator;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/*
VideoRepositoryImpl should always be synchronized with UI representation of frames.
After each change in UI frames, we should call synchronize()
 */
@Log4j
@Component("VideoRepository")
public class VideoRepositoryImpl implements VideoRepository{
    private ImageView imageView;
    private final List<ImageFrame> list = new ArrayList<>();
    private List<ImageFrame> listOriginal;

    public VideoRepositoryImpl() {
    }

    @Override
    public List<ImageFrame> getOriginalList() {
        return new ArrayList<>(listOriginal);
    }
    @Override
    public void setOriginalList(List<ImageFrame> list) {
        listOriginal = list;
    }

    @Override
    public void synchronize(List<Frame> frameList) {
        clear();
        frameList.forEach(frame -> {
            if (frame.isEmpty()) {
                ImageFrame imageFrameEmpty = new ImageFrame(listOriginal.get(frame.getNum()), true);
                add(imageFrameEmpty);
                return;
            }
            ImageFrame imageFrame = new ImageFrame(listOriginal.get(frame.getNum()));
            add(imageFrame);
        });
    }
    @Override
    public void synchronize(int index, Frame frame) {
        int num = frame.getNum();
        if (frame.isEmpty()) list.set(index, new ImageFrame(listOriginal.get(num), true));
        list.set(index, new ImageFrame(listOriginal.get(num)));
    }
    @Override
    public void saveOriginalImages() {
        listOriginal = new ArrayList<>(list);
    }
    @Override
    public void clear() {
        list.clear();
    }
    @Override
    public void showImage(int index) {
        Image image;
        List<ImageFrame> frames = Data.playMode == PlaybackMode.ACTUAL ? list : listOriginal;

        if (index >= frames.size()) return;

        image = frames.get(index).getImage();
        imageView.setImage(image);
    }
    @Override
    public void add(ImageFrame image) {
        list.add(image);
    }
    @Override
    public void add(int index, ImageFrame image) {
        list.add(index, image);
    }
    @Override
    public void addEmpty(int index) {
        ImageFrame imageFrame = list.get(index);
        list.add(++index, new ImageFrame(imageFrame.getImage(), imageFrame.getNum(), true));
    }
    @Override
    public ImageFrame get(int index) {
        return list.get(index);
    }
    @Override
    public int size() {
        return list.size();
    }
    @Override
    public List<ImageFrame> getList() {
        return new ArrayList<>(list);
    }
    @Override
    public ImageFrame getImageByNum(int num) {
        ImageFrame imageFrame = list.stream()
                .filter(i -> i.getNum() == num)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Can not find ImageFrame by given num"));
        return imageFrame;
    }

    @Override
    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }
    @Override
    public int getPrevIndexByNum(int frameNum) {
        int num = --frameNum;
        ImageFrame imageFrame = null;
        while (imageFrame == null) imageFrame = getImageFrameByNum(num--);
        return list.indexOf(imageFrame);
    }


    private ImageFrame getImageFrameByNum(int num) {
        return list.stream()
                .filter(imageFrame -> imageFrame.getNum() == num)
                .findFirst().orElse(null);
    }
}
