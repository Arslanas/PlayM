package my.app.playm.controller.handlers;

import javafx.event.EventHandler;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import my.app.playm.controller.Data;
import my.app.playm.controller.Dispatcher;
import my.app.playm.model.decode.Decoder;
import my.app.playm.model.repo.VideoRepository;
import my.app.playm.model.repo.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Log4j
@Component
public class DragDropHandler {
    @Autowired
    private Dispatcher dispatcher;

    public final EventHandler<DragEvent> VIDEO_DRAG_OVER = e -> {
        if (e.getDragboard().hasFiles()) e.acceptTransferModes(TransferMode.ANY);
    };
    public final EventHandler<DragEvent> VIDEO_DROP = e -> {
        List<File> files = e.getDragboard().getFiles();
        String path = files.get(0).toString();
        dispatcher.decodeVideo(path);
    };

}
