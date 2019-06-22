package my.app.playm.socket;

import lombok.Data;

import java.nio.file.Path;

@Data
public class RequestRange {
    private int start;
    private int end;
    private String path;
}
