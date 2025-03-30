package testing;

import model.Room;
import model.Student;
import model.Mask;


public class Test9 implements ITest {
    /**
     * Létrehoz egy hallgatót és egy maszkot, amit a hallgató kezébe ad. Ezután a
     * maszkot aktiváltatja és megfelelő számú alkalommal
     * meghívja rajta a timeElapsed-et, ami ennek hatására lejár és
     * törlődik.
     * A váltózóneveket regisztrálja.
     */
    @Override
    public void run() {
        Student s = new Student();
        Logger.register(s, "s");
        Room r = new Room();
        Logger.register(r, "r");
        Mask m = new Mask();
        Logger.register(m, "m");
        r.addPerson(s);
        s.setLocation(r);
        s.initItem(m);
        m.initLocation(r, s);
        r.timeElapsed(1);
    }
}