package testing;

import model.Room;
import model.SlideRule;
import model.Student;

public class Test16 implements ITest {
    /**
     * Létrehoz egy hallgatót és egy szobát, ezután a szobába berakja a hallgatót
     * és egy logarlécet. A hallgató felveszi és ezzel megnyerve a
     * játékot.
     * A váltózóneveket regisztrálja.
     */
    @Override
    public void run() {
        Room r = new Room();
        Logger.register(r, "r");
        Student s = new Student();
        Logger.register(s, "s");
        SlideRule sr = new SlideRule();
        Logger.register(sr, "sr");
        r.addPerson(s);
        s.setLocation(r);
        r.initItem(sr);
        sr.initLocation(r, null);
        s.addItem(sr);
    }
}
