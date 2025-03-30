package testing;

import model.Room;
import model.Student;
import model.Teacher;

public class Test4 implements ITest {
    /**
     * Létrehoz egy hallgatót, egy oktatót és egy szobát, ahova mindkettőjüket
     * belerakja. Az oktató ezután megbuktatja a hallgatót.
     * A változóneveket regisztrálja.
     */
    @Override
    public void run() {
        Teacher t = new Teacher();
        Logger.register(t, "t");
        Student s = new Student();
        Logger.register(s, "s");
        Room r = new Room();
        Logger.register(r, "r");
        r.addPerson(s);
        s.setLocation(r);
        r.addPerson(t);
        t.setLocation(r);
        t.meet(s);
    }
}
