package model;

/**
 * Az IntervalItem egy absztrakt osztály
 * olyan tárgyak viselkedésének és működésének modellezésére,
 * amik egy adott ideig fejtik ki hatásukat. Az osztály
 * felelőssége tudni, hogy a tárgy aktiválva van-e és meddig érvényes.
 * Az Item absztrakt osztály leszármazottja.
 */
public abstract class IntervalItem extends Item {

    /**
     * A tárgy aktiválva van-e vagy sem?
     */
    protected boolean activated;
    /**
     * Mennyi idő múlva változik a tárgy állapota aktiváltról.
     */
    protected int timeRemaining;

    /**
     * Az IntervalItem osztály konstruktora.
     * Inicializál egy Item objektumot a megadott értékekkel.
     *
     * @param location A szoba, amiben a tárgy van.
     * @param holder A személy, akinél a tárgy van.
     * @param activated A tárgy aktiválva van-e?
     * @param timeRemaining A hátralévő idő, amíg a tárgy aktív.
     */
    public IntervalItem(Room location, Person holder, boolean activated, int timeRemaining){
        super(location, holder);
        this.activated = activated;
        this.timeRemaining = timeRemaining;
    }

    /**
     * A tárgy aktiválása.
     */
    @Override
    public void activate() {
        activated = true;
    }

    /*
     * Aktiváltság lekérdezése.
     */
    public boolean isActivated(){
        return activated;
    }

    /*
     * Aktiváltság beállítása.
     */
    public void setActivated(boolean activated){
        this.activated = activated;
    }

    /*
     * Hátralévő idő lekérdezése.
     */
    public int getTimeRemaining() {
        return timeRemaining;
    }

    /*
     * Hátralévő idő beállítása.
     */
    public void setTimeRemaining( int timeRemaining ){
        this.timeRemaining = timeRemaining;
    }
}
