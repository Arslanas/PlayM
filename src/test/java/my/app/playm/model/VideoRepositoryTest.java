package my.app.playm.model;

import my.app.playm.PlayMTest;
import my.app.playm.controller.Dispatcher;
import my.app.playm.entity.frame.Frame;
import my.app.playm.model.repo.VideoRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class VideoRepositoryTest extends PlayMTest {
    @Autowired
    private Dispatcher dispatcher;
    @Autowired
    private VideoRepository videoRepository;

}
