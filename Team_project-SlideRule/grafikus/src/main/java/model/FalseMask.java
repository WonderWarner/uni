package model;

/**
 * A FalseMask osztály a hamis FFP2-es maszk működését és viselkedését modellezi.
 * A Maskhoz hasonlóan működik, azt leszámítva, hogy nem nyújt védelmet a mérgező gázzal szemben.
 * A Mask leszármazottja.
 */
public class FalseMask extends Mask {

    /**
     * A FalseMask osztály konstruktora.
     * Létrehoz és inicializál egy FalseMask objektumot.
     *
     * @param location A szoba, amiben a tárgy van.
     * @param holder A személy, akinél a tárgy van.
     * @param activated A tárgy aktiválva van-e?
     * @param timeRemaining A hátralévő idő, amíg a tárgy aktív.
     */
    public FalseMask(Room location, Person holder, boolean activated, int timeRemaining){
        super(location, holder, activated, timeRemaining);
    }

    /**
     * Mérgező gáz elleni védelem kérése
     * Meghívja ősének azonos nevű metódusát az esetleges aktiválás miatt.
     * Nem nyújt védelmet a gáz ellen.
     * 
     * @return {@code false} minden esetben.
     */
    @Override
    public boolean saveFromGas() {
        super.saveFromGas();
        return false;
    }
}
