package my.app.playm.model.repo;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import lombok.extern.log4j.Log4j;
import my.app.playm.aop.SaveMoment;
import my.app.playm.aop.UpdateAfter;
import my.app.playm.controller.Util;
import my.app.playm.entity.frame.Frame;
import my.app.playm.entity.frame.FrameProducer;
import my.app.playm.entity.frame.FrameRemoved;
import my.app.playm.model.moment.Moment;
import my.app.playm.model.moment.MomentOriginator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Log4j
@Component("FrameRepository")
public class FrameRepositoryImpl implements FrameRepository, MomentOriginator {
    private ObservableList<Node> list;
    private ObservableList<Node> listRemoved;

    @Autowired
    private FrameProducer frameProducer;

    @Override
    public void saveMoment(Moment moment) {
        moment.setListFrames(new ArrayList<>(list));
        moment.setListFramesRemoved(new ArrayList<>(listRemoved));
    }

    @UpdateAfter
    @Override
    public void restoreMoment(Moment moment) {
        list.clear();
        listRemoved.clear();
        list.addAll(moment.getListFrames());
        listRemoved.addAll(moment.getListFramesRemoved());
        recalculateFrom(0);
    }

    @Override
    public List<Frame> getList() {
        return list.stream().map(e -> (Frame) e).collect(Collectors.toList());
    }

    @Override
    public int size() {
        return list.size();
    }

    @SaveMoment
    @Override
    public void makeEmpty(Frame frame) {
        int index = indexOf(frame);
        list.set(index, frameProducer.createFrame(frame.getNum(), true));
        recalculateFrom(index);

        FrameRemoved frameRemoved = frameProducer.createFrameRemoved(frame.getNum(), index);

        listRemoved.add(frameRemoved);
        frameRemoved.setTranslateX(frameRemoved.getNum() * FrameRepository.frameWidth.get());
    }

    @SaveMoment
    public boolean remove(Frame frame) {
        int index = indexOf(frame);
        boolean result = list.remove(frame);
        recalculateFrom(index);

        FrameRemoved frameRemoved = frameProducer.createFrameRemoved(frame.getNum(), index);

        listRemoved.add(frameRemoved);
        frameRemoved.setTranslateX(frameRemoved.getNum() * FrameRepository.frameWidth.get());

        return result;
    }

    @SaveMoment
    @Override
    public boolean remove(int index) {
        Frame frame = (Frame) list.get(index);
        if (frame.getNum() == 0) return false;
        list.remove(frame);
        recalculateFrom(index);

        if (frame.isEmpty()) return true;

        FrameRemoved frameRemoved = frameProducer.createFrameRemoved(frame.getNum(), index);

        listRemoved.add(frameRemoved);
        frameRemoved.setTranslateX(frameRemoved.getNum() * FrameRepository.frameWidth.get());

        return true;
    }

    @Override
    public int indexOf(Object node) {
        return list.indexOf(node);
    }

    @SaveMoment
    @Override
    public boolean add(Node node) {
        int index = size();
        boolean result = list.add(node);
        recalculate(index);
        return result;
    }

    @SaveMoment
    @Override
    public void add(int index, Node element) {
        list.add(index, element);
        recalculateFrom(index);
    }

    @SaveMoment
    @Override
    public void add(int imageNum) {
        list.add(frameProducer.createFrame(imageNum));
        recalculateFrom(0);
    }

    @SaveMoment
    @Override
    public boolean addAll(int index, Collection<? extends Node> c) {
        boolean result = list.addAll(index, c);
        recalculateFrom(index);
        return result;
    }

    @Override
    public Frame get(int index) {
        index = Util.clamp(index, 0, size() - 1);
        return (Frame) list.get(index);
    }

    @Override
    public void updateWidth() {
        recalculateFrom(0);
    }

    @SaveMoment
    @Override
    public void shiftRightFrom(int index) {
        list.add(index, frameProducer.createFrame(get(index).getNum(), true));
        recalculateFrom(index);
    }

    @SaveMoment
    @Override
    public void addEmpty(int index) {
        int num = get(index).getNum();
        list.add(++index, frameProducer.createFrame(num, true));
        recalculateFrom(index);
    }

    private void recalculate(int index) {
        Frame frame = (Frame) list.get(index);
        if (frame.isEmpty()) frame = frameProducer.createFrame(get(index - 1).getNum(), true);
        frame.setTranslateX(index * FrameRepository.frameWidth.get());
        list.set(index, frame);
    }

    private void recalculateFrom(int from) {
        IntStream.range(from, size()).forEach(this::recalculate);
    }

    @Override
    public Node findById(String id) {
        return list.stream()
                .filter(child -> child.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Can not find frame by id"));
    }

    @SaveMoment
    @Override
    public void locate(int frameNum, int newIndex) {
        // Calculate current position of pressed frame - it may differ from pressedFrameIndex
        int currentIndex = getIndexByNum(frameNum);
        int closestNotEmptyFrameIndex = getClosestNotEmptyFrameIndex(currentIndex);
        int currentDelta = newIndex - currentIndex;
        if (currentDelta > 0) {
            List<Node> dummies = IntStream.range(0, currentDelta).mapToObj(i -> {
                int num = get(closestNotEmptyFrameIndex).getNum();
                return frameProducer.createFrame(num, true);
            }).collect(Collectors.toList());
            addAll(currentIndex, dummies);
        }
        if (currentDelta < 0 && newIndex >= 0) {
            //Return if newIndex is behind of closestNonEmptyIndex
            if (newIndex < closestNotEmptyFrameIndex) return;
            //Return if newIndex is not empty frame
            if (!isEmptyFrame(newIndex)) return;
            list.remove(newIndex, currentIndex);
            recalculateFrom(newIndex);
        }
    }

    @Override
    public void clear() {
        if (list != null) list.clear();
        if (listRemoved != null) listRemoved.clear();
    }

    @SaveMoment
    @Override
    public void recover(FrameRemoved frameRemoved) {
        int num = frameRemoved.getNum();
        Frame frame = frameProducer.createFrame(num);
        int prevFrameIndex = getPrevIndexByNum(num);
        int nextFrameIndex = getNextIndexByNum(num);
        int recoverIndex = frameRemoved.getRecoverIndex();
        if (prevFrameIndex < recoverIndex && recoverIndex < nextFrameIndex) add(recoverIndex, frame);
        else if (recoverIndex == nextFrameIndex) add(recoverIndex, frame);
        else {
            recoverIndex = prevFrameIndex + 1;
            add(recoverIndex, frame);
        }
        listRemoved.remove(frameRemoved);
    }

    private int getPrevIndexByNum(int frameNum) {
        int num = --frameNum;
        Frame frame = null;
        while (frame == null) frame = getFrameByNum(num--);
        return indexOf(frame);
    }

    private int getNextIndexByNum(int frameNum) {
        int num = ++frameNum;
        Frame frame = null;
        while (frame == null) frame = getFrameByNum(num++);
        return indexOf(frame);
    }

    private Frame getFrameByNum(int num) {
        if (num == 0) {
            return (Frame) list.stream()
                    .filter(node -> ((Frame) node).getClass() == Frame.class)
                    .findFirst().orElseThrow(() -> new NullPointerException("Can not find Frame from zero index"));
        }
        return (Frame) list.stream()
                .filter(node -> ((Frame) node).getNum() == num)
                .findFirst().orElse(null);
    }

    private int getIndexByNum(int num) {
        Node startNode = list.stream()
                .filter(n -> ((Frame) n).getNum() == num)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Can not find frame by given id"));
        return list.indexOf(startNode);
    }

    private boolean isEmptyFrame(int newIndex) {
        return ((Frame) list.get(newIndex)).isEmpty();
    }

    @Override
    public int getClosestNotEmptyFrameIndex(int start) {
        int indexBefore = start > 0 ? start - 1 : 0;
        while (isEmptyFrame(indexBefore) && indexBefore > 0) indexBefore--;
        return indexBefore;
    }


    @Override
    public void init(Pane parent, Pane storePane) {
        list = parent.getChildren();
        listRemoved = storePane.getChildren();
    }

}

