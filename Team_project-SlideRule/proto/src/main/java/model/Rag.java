package model;

/**
 * A Rag osztály a nedves táblatörlő rongy működését és viselkedését modellezi.
 * A rongy aodtt ideig bénítja a vele egy helységben lévő oktatókat. A hallgató tudja aktiválni.
 * Ha a hatása lejár, akkor megsemmisül.
 * Az IntervalItem absztrakt osztály leszármazottja.
 */
public class Rag extends IntervalItem{
    /**
     * A Rag osztály kontruktora.
     * Létrehoz és inicializál egy Rag objektumot.
     *
     * @param location a szoba, amiben a tárgy van
     * @param holder a személy, akinél a tárgy van
     * @param activated a tárgy aktiválva van-e
     * @param timeRemaining a hátralévő idő, amíg a tárgy aktív
     */
    public Rag(Room location, Person holder, boolean activated, int timeRemaining){
        super(location, holder, activated, timeRemaining);
    }

    /**
     * Egy személlyel való találkozást lekezelése.
     * Az aktivált rongy kezdeményezi a személy megbénítását.
     * @param person a személy, akivel találkozik a tárgy
     */
    @Override
    public void meet(Person person) {
        if(activated){
            person.slip();
        }
    }

    /**
     * Kibukás elleni védelem kérése
     * Amennyiben aktiválva van a rongy, logikai igazzal
     * tér vissza, megvédve a birtokosát és a támadó személyre megbénítást kezdeményez.
     * Egyéb esetben logikai hamissal tér vissza.
     * @param killer a támadó személy
     * @return {@code true} ha aktiválva van, {@code false} egyébként
     */
    @Override
    public boolean saveFromDeath(Person killer) {
        if(activated){
            killer.slip();
            return true;
        }
        return false;
    }

    /**
     * Mérgező gáz elleni védelem kérése
     *  A tárgy nem nyújt védelmet a gáz ellen.
     * @return {@code false} minden esetben
     */
    @Override
    public boolean saveFromGas() {
        return false;
    }

    /**
     * Idő telése a tárgyra
     * Ha aktiválva van a tárgy, akkor a timeRemaining értékét
     * csökkenti time-mal. Ha elérte a 0-t, akkor aktuális birtokosánál kezdeményezi a tárgy
     * megsemmisítését.
     * @param time az eltelt idő
     */
    @Override
    public void timeElapsed(int time) {
        if(activated) {
            timeRemaining -= time;
            if(timeRemaining <= 0){
                if(holder != null){
                    holder.removeItem(this);
                }
                else{
                    location.removeItem(this);
                }
            }
        }
    }
}
