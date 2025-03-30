package testing;

import model.*;

public class Test13 implements ITest {
    /**
     * Létrehoz egy hallgatót és két szobát, illetve két tranzisztort. A
     * tranzisztorokat összepárosítja és az egyiket elhelyezi az egyik
     * szobában, míg a másikat a hallgató kezébe adja és hallgatót
     * elhelyezi a másik szobában. A hallgatónál lévő tranzisztort
     * aktiválva a hallgató eldobja a tranzisztorát a szobában és
     * átteleportál a másik szobába, ahol a másik tranzisztor van. A
     * felhasználó felelőssége a teszteset helyes működése
     * érdekében a páros működés kiválasztása.
     * A váltózóneveket regisztrálja.
     */
    @Override
    public void run() {
        Room r1 = new Room();
        Logger.register(r1, "r1");
        Room r2 = new Room();
        Logger.register(r2, "r2");
        Student s = new Student();
        Logger.register( s, "s");
        Transistor t1 = new Transistor();
        Logger.register(t1, "t1");
        Transistor t2 = new Transistor();
        Logger.register(t2, "t2");
        r1.initItem(t2);
        t2.initLocation(r1, null);
        r2.addPerson(s);
        s.setLocation(r2);
        s.initItem(t1);
        t1.initLocation(r2, s);
        t1.setPair(t2);
        t2.setPair(t1);
        t1.activate();
    }
}
