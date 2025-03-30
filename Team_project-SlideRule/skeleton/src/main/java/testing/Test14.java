package testing;

import model.*;

public class Test14 implements ITest {
    /**
     * Létrehoz két szobát, melyek egymással
     * szomszédosak, amikbe egyenként berak egy-egy tárgyat,
     * ezután egyesíti őket. Ha nincs ember egyik szobában se,
     * akkor sikerül az egyesülés. Az eddig a két külön szobában
     * található TVSZ és maszk mind az új szobában lesznek. Ha
     * van ember a bármelyik szobában, akkor nem történik meg az
     * egyesülés.
     * A váltózóneveket regisztrálja.
     */
    @Override
    public void run() {
        Room r1 = new Room();
        Logger.register(r1, "r1");
        Room r2 = new Room();
        Logger.register(r2, "r2");
        Mask m = new Mask();
        Logger.register( m, "m" );
        TVSZ tv = new TVSZ();
        Logger.register( tv, "tv");
        r1.addNeighbour(r2);
        r1.initItem(m);
        m.initLocation(r1, null);
        r2.addNeighbour(r1);
        r2.initItem(tv);
        tv.initLocation(r2, null);
        r1.requestMerge(r2);
    }
}
