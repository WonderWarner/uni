package testing;

import model.*;

public class Test15 implements ITest {
    /**
     * Létrehoz egy szobát, amibe belerak egy maszkot
     * és egy táblatörlő rongyot. Ezután meghívja a szobán a split()
     * metódust. Ha nincs ember a szobában, akkor a szoba
     * kettéválik. Az új szobák szomszédosak lesznek, az eredeti
     * szobában lévő tárgyak megoszlanak a két új szoba között. A
     * táblatörlő rongy a második szobába kerül, a maszk az elsőben
     * marad. Ha van ember a szobában, akkor nem fog osztódni.
     * A váltózóneveket regisztrálja.
     */
    @Override
    public void run() {
        Room r1 = new Room();
        Logger.register(r1, "r1");
        Rag r = new Rag();
        Logger.register( r, "r");
        Mask m = new Mask();
        Logger.register( m, "m" );
        r1.initItem(r);
        r.initLocation(r1, null);
        r1.initItem(m);
        m.initLocation(r1, null);
        r1.split();
    }
}
