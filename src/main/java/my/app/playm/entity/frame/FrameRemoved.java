package my.app.playm.entity.frame;

import my.app.playm.controller.TrackData;
import my.app.playm.controller.handlers.FramePaneHandler;

public class FrameRemoved extends Frame {
    private int recoverIndex;

    public FrameRemoved(int num, int recoverIndex){
        super(num);
        this.recoverIndex = recoverIndex;
        setId("frameRemoved_" + num);
    }
    public int getRecoverIndex(){
        return recoverIndex;
    }

    @Override
    public String toString() {
        return "FrameRemoved{" +
                "recoverIndex=" + recoverIndex +
                ", num=" + getNum() +
                '}';
    }
}
