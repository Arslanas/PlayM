package my.app.playm.controller.handlers;


import javafx.css.PseudoClass;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import lombok.extern.log4j.Log4j;
import my.app.playm.controller.Data;
import my.app.playm.model.repo.FrameRepository;
import my.app.playm.entity.frame.Frame;
import my.app.playm.entity.frame.FrameRemoved;

@Log4j
public class FramePaneHandler {
    //point of press event in scene coordinates
    private static double orgPressSceneX;
    private static int pressedFrameIndex, shift, closestNonEmptyIndex;
    private static int pressedFrameNum;
    private static boolean isFramePressed;

    static public final EventHandler<MouseEvent> FRAMEPANE_ON_MOUSE_DRAGGED = e -> {

        // if frame is not pressed then stop calculation. Flag becomes true when frame pressed and false when released
        if (!isFramePressed) return;
        if (pressedFrameNum == 0) return;
        int frameWidth = FrameRepository.frameWidth.get();
        int delta = (int) (e.getSceneX() - orgPressSceneX);
        int shiftCurrent = delta / frameWidth;
        /*compares with previous shift value.
        If it false then previous shift value overwritten by current and method continues */
        if (isAlreadyCalculated(shiftCurrent)) return;
        // All frames from pressedFrameIndex will be shifted to newIndex
        int newIndex = shiftCurrent + pressedFrameIndex;

        Data.frameRepo.locate(pressedFrameNum, newIndex);
    };

    static public final EventHandler<MouseEvent> FRAME_MOUSE_PRESSED = e -> {
        isFramePressed = true;
        orgPressSceneX = e.getSceneX();
        Frame frame = (Frame) e.getSource();
        pressedFrameIndex = Data.frameRepo.indexOf(frame);
        pressedFrameNum = frame.getNum();
        closestNonEmptyIndex = Data.frameRepo.getClosestNotEmptyFrameIndex(pressedFrameIndex);
    };
    //
    static public final EventHandler<MouseEvent> FRAME_REMOVED_MOUSE_PRESSED = e -> {
        if (e.isPrimaryButtonDown() && e.getClickCount() == 2) {
            Data.frameRepo.recover((FrameRemoved) e.getSource());
        }
    };
    //
    static public final EventHandler<MouseEvent> FRAME_ON_MOUSE_RELEASED = e -> {
        isFramePressed = false;
    };

    static public final EventHandler<MouseEvent> FRAME_MOUSE_DRAG_ENTERED = e -> {
    };

    //starts drag and drop gesture and optionally puts some data to clipboard
    static public final EventHandler<MouseEvent> FRAME_DRAG_DETECTED = e -> {
        if (e.isControlDown()) {
            Node source = (Node) e.getSource();
            ClipboardContent content = new ClipboardContent();
            content.putString(source.getId());
            source.startDragAndDrop(TransferMode.ANY).setContent(content);

        }
        if (e.isShiftDown()) {
            Node source = (Node) e.getSource();
            source.startFullDrag();
        }
    };

    //show ghost frame and change style to hover
    static public final EventHandler<DragEvent> STORE_LINE_DRAG_ENTERED = e -> {
        ((Node)e.getSource()).pseudoClassStateChanged(PseudoClass.getPseudoClass("hover"), true);
    };

    //hide ghost frame and change style hover false
    static public final EventHandler<DragEvent> STORE_LINE_DRAG_EXITED = e -> {
        ((Node)e.getSource()).pseudoClassStateChanged(PseudoClass.getPseudoClass("hover"), false);
        e.acceptTransferModes(TransferMode.NONE);
    };

    //accept drag gesture and move ghost frame alongside with a mouse
    static public final EventHandler<DragEvent> STORE_LINE_DRAG_OVER = e -> {
        log.debug("Drag over storepane " + e.getSceneX());
        e.acceptTransferModes(TransferMode.ANY);
    };

    //removeByNum from repository and shift remain frames
    static public final EventHandler<DragEvent> STORE_LINE_DRAG_DROPPED = e -> {
        // removeByNum frame from framepane by id
        log.debug("Dropped on storepane");
        String id = e.getDragboard().getString();
        Frame frame = (Frame) Data.frameRepo.findById(id);
        Data.frameRepo.remove(frame);

        e.setDropCompleted(true);
    };

    //optional
    public static final EventHandler<DragEvent> ROOT_DRAG_DONE = e -> {
        System.out.println("Drag done ");
    };

    static private boolean isAlreadyCalculated(int shiftCurrent) {
        if (shiftCurrent == shift) return true;
        shift = shiftCurrent;
        return false;
    }

    private FramePaneHandler() {
    }
}
