package my.app.playm.entity.frame;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import lombok.Data;
import lombok.Getter;
import my.app.playm.model.repo.FrameRepository;

@Data
public class Frame extends AnchorPane {
    private final Pane innerPane;
    private final Label label;
    private boolean empty;

    private final int num;

    public Frame(int num) {
        this.num = num;
        innerPane = new Pane();
        label = new Label(num + "");
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (!(o instanceof Frame)) return false;
        Frame frame = (Frame) o;
        if (!getId().equals(frame.getId())) return false;
        if (empty != frame.empty) return false;
        return num == frame.num;
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
