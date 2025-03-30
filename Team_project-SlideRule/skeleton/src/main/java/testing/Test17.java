package testing;

import model.Room;
import model.SlideRule;
import model.Teacher;

public class Test17 implements ITest {
    /**
     * Létrehoz egy oktatót, egy szobát és egy
     * logarlécet. Egy oktató felveszi a logarlécet. Ezután el is dobja
     * azonnal.
     * A váltózóneveket regisztrálja.
     */
    @Override
    public void run() {
        Room r = new Room();
        Logger.register(r, "r");
        Teacher t = new Teacher();
        Logger.register(t, "t");
        SlideRule sr = new SlideRule();
        Logger.register(sr, "sr");
        r.addPerson(t);
        t.setLocation(r);
        r.initItem(sr);
        sr.initLocation(r, null);
        t.addItem(sr);
    }
}
