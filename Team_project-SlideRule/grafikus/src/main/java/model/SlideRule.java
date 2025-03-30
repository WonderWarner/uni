package model;

/**
 * A SlideRule osztály a Logarléc működését és viselkedését modellezi.
 * A Logarléc a hallgatő győzelmének kulcsa.
 * Oktató és takarítő nem veheti fel.
 * Az Item absztrakt osztály leszármazottja.
 */
public class SlideRule extends Item{

    /**
     * A SlideRule osztály kontruktora.
     * Létrehoz és inicializál egy SlideRule objektumot.
     *
     * @param location A szoba, amiben a tárgy van.
     * @param holder A személy, akinél a tárgy van.
     */
    public SlideRule(Room location, Person holder){
        super(location, holder);
    }

    /**
     * A tárgy aktiválása.
     * Nem történik semmi.
     */
    @Override
    public void activate() { }

    /**
     * Találkozás személlyel.
     * Nem csinál semmit, mert ha földön van nincs funkciója.
     * 
     * @param person A személy, akivel találkozik a tárgy.
     */
    @Override
    public void meet(Person person) { }

    /**
     * Kibukás elleni védelem kérése.
     * A tárgy nem nyújt védelmet a kibukás ellen.
     * 
     * @param killer A támadó személy.
     * @return {@code false} minden esetben.
     */
    @Override
    public boolean saveFromDeath(Person killer) {
        return false;
    }

    /**
     * Mérgező gáz elleni védelem kérése
     * A tárgy nem nyújt védelmet a gáz ellen.
     * 
     * @return {@code false} minden esetben.
     */
    @Override
    public boolean saveFromGas() {
        return false;
    }

    /**
     * A tárgy tartózkodási helyének és birtokosának beállítása.
     * Értesíti a személyt a pickedUpSlideRule függvénnyel, amennyiben az nem {@code null}.
     * 
     * @param room A szoba ahova a tárgyat elhelyezi.
     * @param person A személy akinek a kezébe adja, {@code null} esetén nem adja senki kezébe.
     */
    @Override
    public void setLocation( Room room, Person person ){

        location = room;
        holder = person;

        if(person != null) {
            person.pickedUpSlideRule(this);
        }
    }

    /**
     * Idő telése a tárgyon.
     * Mivel egyszerhasználatos tárgy, így nem történik vele semmi az idő múlásával.
     * @param time Az eltelt idő.
     */
    @Override
    public void timeElapsed(int time) { }
}
