package testing;

import model.*;


public class Test11 implements ITest {
    /**
     * Létrehoz egy szobát elhelyez benne egy hallgatót és oktatót. A hallgató kezébe ad
     * egy söröspoharat. A felhasználó eldöntheti, hogy a
     * söröspohár aktív-e illetve, hogy a még van-e még
     * használható-e. Ha nem használható, akkor a eltűnik a játékos
     * kezéből és az oktató megöli a hallgatót. Ha aktív és
     * használható, akkor megvédi a hallgatót a bukástól.
     * A váltózóneveket regisztrálja.
     */
    @Override
    public void run() {
        Teacher t = new Teacher();
        Logger.register(t, "t");
        Student s = new Student();
        Logger.register(s, "s");
        Room r = new Room();
        Logger.register(r, "r");
        BeerGlass b = new BeerGlass();
        Logger.register(b, "b");
        r.addPerson(s);
        s.setLocation(r);
        r.addPerson(t);
        t.setLocation(r);
        s.initItem(b);
        b.initLocation(r, s);
        r.timeElapsed(1);
    }
}