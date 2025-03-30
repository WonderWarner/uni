package model;

/**
 * A FalseSlideRule osztály a hamis Logarléc működését és viselkedését modellezi.
 * A hamis Logarléccel nem lehet győzni, ezen kívül ugyanúgy működik, mint a SlideRule
 * A SlideRule osztály leszármazottja.
 */
public class FalseSlideRule extends SlideRule{

    /**
     * A FalseSlideRule osztály kontruktora.
     * Létrehoz és inicializál egy FalseSlideRule objektumot.
     *
     * @param location a szoba, amiben a tárgy van
     * @param holder a személy, akinél a tárgy van
     */
    public FalseSlideRule(Room location, Person holder){
        super(location, holder);
    }

}