package testing;

import model.*;

public class Test1 implements ITest {
    /**
     * Létrehoz egy hallgatót, egy söröspoharat, egy TVSZ-t, egy maszkot egy
     * tanárt és két szobát, melyek szomszédosak, majd a hallgatót
     * és a tanárt elhelyezi egy-egy szobában. A söröspoharat és a
     * maszkot a hallgató kezébe adja, a TVSZ-t a tanár szobájába
     * rakja. Ezután az egyik szobából a hallgatót átlépteti a
     * másikba, amennyiben a hallgató nincs elkábítva és a jelenlegi
     * szobája jelenleg nem elátkozott. Itt megkérdezi a
     * felhasználót, hogy a szoba tele van-e, és ha igen, a hallgató
     * nem tud belépni, ha nem, megkérdezi, aktiválva van-e a
     * söröspohár, és ha nem, a hallgató megbukik a tanár által.
     * Egyébként átlép.
     * A változók neveit regisztrálja.
     */
    @Override
    public void run() {
        Student s = new Student();
        Logger.register( s, "s");
        Teacher t = new Teacher();
        Logger.register( t,"t");
        Room r2 = new Room();
        Logger.register(r2, "r2");
        Room r1 = new Room();
        Logger.register(r1, "r1");
        TVSZ tv = new TVSZ();
        Logger.register( tv, "tv");
        Mask m = new Mask();
        Logger.register( m, "m" );
        BeerGlass b = new BeerGlass();
        Logger.register(b, "b");
        r2.addPerson(s);
        s.setLocation(r2);
        r2.addNeighbour(r1);
        r1.addNeighbour(r2);
        r1.initItem(tv);
        tv.initLocation(r1, null);
        r1.addPerson(t);
        t.setLocation(r1);
        s.initItem(b);
        b.initLocation(r2, s);
        s.initItem(m);
        m.initLocation(r2, s);
        s.enterRoom(r1);
    }
}
