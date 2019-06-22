package my.app.playm.socket;

import com.google.gson.Gson;
import lombok.extern.log4j.Log4j;
import my.app.playm.controller.Data;
import my.app.playm.controller.Dispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Log4j
@Component
public class RangeProcessor implements RequestProcessor {
    private final Gson parser = new Gson();

    @Override
    public void process(String request) {
        RequestRange requestRange = parser.fromJson(request, RequestRange.class);
        log.debug("Requested range - " + requestRange);
        Data.dispatcher.update(requestRange);
    }
}
