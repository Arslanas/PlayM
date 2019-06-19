package my.app.playm.entity.frame.template;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import my.app.playm.entity.frame.Frame;

public interface Template<T extends Node> {
    void buildView(T frame);
    void addHandlers(T frame);

    default void anchorAllSides(Node node, double value) {
        AnchorPane.setLeftAnchor(node, value);
        AnchorPane.setRightAnchor(node, value);
        AnchorPane.setTopAnchor(node, value);
        AnchorPane.setBottomAnchor(node, value);
    }
}
