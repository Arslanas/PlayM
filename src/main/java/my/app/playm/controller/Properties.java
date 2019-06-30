package my.app.playm.controller;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class Properties {

    @Value("${exportNamespace}")
    private String exportNamespace;
    @Value("${debugVideoSource}")
    private String debugVideoSource;
    @Value("${debugImageSource}")
    private String debugImageSource;
    @Value("${debugSequenceSource}")
    private String debugSequenceSource;
    @Value("${audioSource}")
    private String audioSource;
    @Value("${mayaImagesPath}")
    private String mayaImagesPath;

}
