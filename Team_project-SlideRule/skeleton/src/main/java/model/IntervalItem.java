package model;

import testing.Logger;

/**
 * Az IntervalItem egy absztrakt alaposztály
 * olyan tárgyak viselkedésének és működésének modellezésére,
 * amik egy adott ideig fejtik ki hatásukat. Az osztály
 * felelőssége tudni, hogy a tárgy aktiválva van-e és meddig érvényes.
 * Az Item absztrakt osztály leszármazottja.
 */
public abstract class IntervalItem extends Item {

    protected boolean activated;
    protected int timeRemaining;

    /**
     * A tárgya aktiválása.
     * A függvényhívást és visszatérést logolja.
     */
    @Override
    public void activate() {
        Logger.enter(this, "activate");
        Logger.exit(this, "activate");
    }
}
