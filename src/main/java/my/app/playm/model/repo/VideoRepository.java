package my.app.playm.model.repo;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import my.app.playm.controller.Data;
import my.app.playm.entity.frame.Frame;
import my.app.playm.entity.frame.ImageFrame;
import my.app.playm.model.moment.Moment;
import my.app.playm.model.player.PlaybackMode;

import java.util.ArrayList;
import java.util.List;

public interface VideoRepository {
    void synchronize(List<Frame> frameList);

    void synchronize(int index, Frame frame);

    void saveMoment(Moment moment);

    void restoreMoment(Moment moment);

    void saveOriginalImages();

    void clear();

    void showImage(int index);

    void add(ImageFrame image);

    void add(int index, ImageFrame image);

    void addEmpty(int index);

    ImageFrame get(int index);

    int size();

    List<ImageFrame> getList();

    ImageFrame getImageByNum(int num);

    void setImageView(ImageView imageView);

    int getPrevIndexByNum(int frameNum);

}
