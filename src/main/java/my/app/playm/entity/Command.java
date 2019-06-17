package my.app.playm.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class Command {

    private final String description;
    private final Runnable runnable;

    public void run() {
        runnable.run();
    }
}
