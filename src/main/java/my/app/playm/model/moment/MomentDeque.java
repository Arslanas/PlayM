package my.app.playm.model.moment;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Deque;
import java.util.LinkedList;

@Log4j
@Component
public class MomentDeque implements MomentRepository {
    @Autowired
    MomentOriginator frameRepo;

    private final Deque<Moment> deque = new LinkedList<>();

    @Override
    public Moment get() {
        return deque.poll();
    }

    @Override
    public void save(Moment moment) {
        deque.push(moment);
    }

    @Override
    public void saveState() {
        Moment moment = new Moment();
        frameRepo.saveMoment(moment);
        save(moment);
    }

    @Override
    public void restoreState() {
        Moment moment = get();
        if (moment == null) return;
        frameRepo.restoreMoment(moment);
    }
}
