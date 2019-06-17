package my.app.playm.model.moment;

public interface MomentRepository {

    Moment get();

    void save(Moment moment);

    void restoreState();

    void saveState();

}
