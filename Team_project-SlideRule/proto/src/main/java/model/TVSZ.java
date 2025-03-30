package model;

/**
 * A TVSZ osztály a TVSZ denevérbőrre írt pédányának működését
 * és viselkedését modellezi. Háromszor képes megvédeni a hallgatót az oktató támadásától.
 * Ha elhasználódik, akkor megsemmisül.
 * Az Item absztrakt osztály leszármazottja.
 */
public class TVSZ extends Item{

    /*
     * A tárgy ennyiszer véd meg a kibukástól, ha eléri a nullát, megszűnik
     * Kezdőértéke 3
     */
    private int usesRemaining;

    /**
     * A TVSZ osztály konstruktora.
     * Létrehoz és inicializás egy TVSZ objektumot.
     *
     * @param location a szoba, amiben a tárgy van
     * @param holder a személy, akinél a tárgy van
     */
    public TVSZ(Room location, Person holder){
        super(location, holder);
        usesRemaining = 3;
    }

    /**
     * A TVSZ-t manuálisan nem lehet aktiválni, nem történik vele semmi.
     */
    @Override
    public void activate() { }

    /**
     * Találkozás személlyel
     * Nem csinál semmit. a földön nem tud megvédeni senkit.
     * @param person a személy, akivel találkozik a tárgy
     */
    @Override
    public void meet(Person person) { }

    /**
     * Kibukás elleni védelem kérése
     * A tárgy megvédi a birtokosát és csökkenti a
     * usesRemaining-et 1-gyel. Amennyiben a változó értéke 0 lesz megsemmisül.
     * Logikai igazzal tér vissza.
     * @param killer a támadó személy
     * @return {@code true} minden esetben
     */
    @Override
    public boolean saveFromDeath(Person killer) {
        if(--usesRemaining == 0){
            if(holder != null){
                holder.removeItem(this);
            } else{
                location.removeItem(this);
            }
        }
        return true;
    }

    /**
     * Mérgező gáz elleni védelem kérése
     * A tárgy nem nyújt védelmet a gáz ellen.
     * @return {@code false} minden esetben
     */
    @Override
    public boolean saveFromGas() {
        return false;
    }

    /**
     * Idő telése a TVSZ-en
     * Mivel egyszerhasználatos tárgy, így nem történik vele
     * semmi az idő múlásával.
     * @param time az eltelt idő
     */
    @Override
    public void timeElapsed(int time) { }

    /*
     * Hátralevő használati alkalmak lekérdezése
     */
    public int getUsesRemaining(){
        return usesRemaining;
    }

    /*
     * Hátralevő használati alkalmak beállítása
     */
    public void setUsesRemaining(int usesRemaining) {
        this.usesRemaining = usesRemaining;
    }
}
