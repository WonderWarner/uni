package model;

/**
 * A Cleaner osztály a takarítók működését és viselkedését modellezi.
 * A Person absztrakt osztály leszármazottja.
 */
public class Cleaner extends Person{

    /**
     * A Cleaner osztály konstruktora.
     * Létrehoz és inicializál egy Cleaner objektumot
     *
     * @param stunRemaining az idő, ameddig a személy le van bénulva
     * @param location a szoba, ahol a személy vam
     */
    public Cleaner(int stunRemaining, Room location){
        super(stunRemaining, location);
    }

    /**
     * A takarító mozgását végrehajtó metódus.
     * A takarító mozgási metódusa ugyanazzal kezdődik, mint az eredeti, ősben definiált.
     * Majd amennyiben a location-je módosult, meghívja a location-ön a setGas(false) és a clean() metódusait.
     * @param roomTo az a szoba, ahova át akar lépni
     */
    @Override
    public void enterRoom( Room roomTo ){
        Room originalLocation=location;
        super.enterRoom(roomTo);
        if(originalLocation!=location) {
            location.setGas(false);
            location.clean();
        }
    }

    /**
     * A takarító egy személlyel való találkozást kezeli le.
     * A takarító nem csinál semmit
     * @param person a személy, akivel találkozik
     */
    @Override
    public void meet(Person person) {}

    /**
     * A takarító kibuktatásának megkísérlése
     * A takarító nem tud kibukni, nem történik semmi.
     * @param killer a személy aki megölné
     */
    @Override
    public void kill(Person killer) {}

    /**
     * Rongy miatti elkábulás
     * Nem történik semmi, mivel a takarítót nem veszélyezteti a táblatörlő rongy.
     */
    @Override
    public void slip() {}

    /**
     * Mérgező gáz miatti elkábulás
     * Nem történik semmi, a takarító rezisztens a mérgező gázra.
     */
    @Override
    public void stun() {}

    /**
     * Logarléc felvétele.
     * A takarító kidobja a logarlécet.
     * @param slideRule a logarléc
     */
    @Override
    public void pickedUpSlideRule(SlideRule slideRule) { dropItem(slideRule); }

    /**
     * A takarító köszönésre reagálása.
     * A takarító nem csinál semmit
     * @param greeter a személy aki köszönt neki
     */
    @Override
    public void greet(Person greeter) {}
}