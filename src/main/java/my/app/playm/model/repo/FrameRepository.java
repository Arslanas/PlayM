package my.app.playm.model.repo;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import my.app.playm.entity.frame.Frame;
import my.app.playm.entity.frame.FrameRemoved;
import my.app.playm.model.moment.MomentOriginator;

import java.util.Collection;
import java.util.List;


public interface FrameRepository {

    List<Frame> getList();

    int size();

    boolean remove(Frame frame);

    boolean remove(int index);

    boolean add(Node node);

    void add(int index, Node element);

    void add(int imageNum);

    boolean addAll(int index, Collection<? extends Node> c);

    void addEmpty(int index);

    void makeEmpty(Frame frame);

    Frame get(int index);

    Node findById(String id);

    int indexOf(Object node);

    void updateWidth();

    void shiftRightFrom(int index);

    void locate(int frameNum, int newIndex);

    void clear();

    void recover(FrameRemoved frameRemoved);

    int getClosestNotEmptyFrameIndex(int start);

    void init(Pane parent, Pane storePane);

    IntegerProperty frameWidth = new SimpleIntegerProperty(45);
}
