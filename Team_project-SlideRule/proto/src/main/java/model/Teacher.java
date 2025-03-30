package model;

/**
 * A Teacher osztály az oktatók működését és viselkedését modellezi.
 * A Person absztrakt osztály leszármazottja.
 */
public class Teacher extends Person{

    /**
     * A Teacher osztály konstruktora.
     * Létrehoz és inicializál egy Teacher objektumot
     *
     * @param stunRemaining az idő, ameddig a személy le van bénulva
     * @param location a szoba, ahol a személy vam
     */
    public Teacher(int stunRemaining, Room location){
        super(stunRemaining, location);
    }

    /**
     * A tanár egy személlyel való találkozást kezeli le.
     * A személyt, akivel találkozik, megpróbálja megölni, amennyiben nincs lebénulva.
     * @param person a személy, akivel találkozik
     */
    @Override
    public void meet(Person person) {
        if(stunRemaining==0) person.kill(this);
    }

    /**
     * Tanárt megölik.
     * Nem történik semmi, mert az oktató nem halhat meg.
     * @param killer az a személy, aki ki akarja buktatni
     */
    @Override
    public void kill(Person killer) {}

    /**
     * Táblatörlő rongy kábítása.
     * Az oktató lebénul a táblatörlő rongy hatására.
     */
    @Override
    public void slip() {
        stunRemaining=STUNSTART;
    }

    /**
     * Logarléc felvétele.
     * Az oktató kidobja a logarlécet.
     * @param slideRule a logarléc
     */
    @Override
    public void pickedUpSlideRule(SlideRule slideRule) {
        dropItem(slideRule);
    }

    /**
     * Köszönnek a tanárnak
     * A köszönést kezeli le. A személyt, aki köszön neki, megpróbálja megölni, amennyiben nincs lebénulva.
     * @param greeter a másik személy
     */
    @Override
    public void greet(Person greeter) {
        if(stunRemaining==0) greeter.kill(this);
    }
}
