package my.app.playm.entity.frame;

import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import lombok.Data;

@Data
public class FrameRemoved extends AnchorPane {
    private int recoverIndex;
    private final Pane innerPane;
    private final Label label;
    private final int num;

    public FrameRemoved(int num, int recoverIndex){
        this.num = num;
        innerPane = new Pane();
        label = new Label(num + "");
        this.recoverIndex = recoverIndex;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (!(o instanceof Frame)) return false;
        Frame frame = (Frame) o;
        if (!getId().equals(frame.getId())) return false;
        return getNum() == frame.getNum();
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
