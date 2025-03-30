package testing;

import model.*;


public class Test12 implements ITest {
    /**
     * Létrehoz egy oktató és egy hallgatót. A hallgató kezébe ad egy TVSZ példányt.
     * Az oktató találkozik a hallgatóval. Ha az oktató nincs
     * megbénulva (amit a felhasználó dönt el), akkor megpróbálja a
     * hallgatót megölni. A hallgató megvédi magát a TVSZ-szel. A
     * felhasználó eldöntheti, hogy ez volt-e az utolsó használata a
     * tárgynak. Ha igen, akkor a tárgy eltűnik a hallgató kezéből.
     * A váltózóneveket regisztrálja.
     */
    @Override
    public void run() {
        Teacher t = new Teacher();
        Logger.register(t, "t");
        Student s = new Student();
        Logger.register(s, "s");
        TVSZ tv = new TVSZ();
        Logger.register(tv, "tv");
        s.initItem(tv);
        tv.initLocation(null, s);
       t.meet(s);
    }
}