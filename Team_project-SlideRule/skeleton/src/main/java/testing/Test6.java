package testing;

import model.Room;
import model.Student;
import model.TVSZ;

public class Test6 implements ITest {
    /**
     * Létrehoz egy hallgatót, egy szobát és egy maszkot, amit a hallgató kezébe
     * ad. Ezután a hallgatót a szobába rakja, és eldobatja a tárgyat.
     * Az eldobott tárgy a hallgató kezéből eltűnik és a szobába
     * kerül.
     * A változóneveket regisztrálja.
     */
    @Override
    public void run() {
        Room r = new Room();
        Logger.register(r, "r");
        Student s = new Student();
        Logger.register(s, "s");
        TVSZ tv = new TVSZ();
        Logger.register(tv, "tv");
        r.addPerson(s);
        s.setLocation(r);
        s.initItem(tv);
        tv.initLocation(r, s);
        s.dropItem(tv);
    }
}
