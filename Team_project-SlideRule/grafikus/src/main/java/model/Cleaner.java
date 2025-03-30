package model;

/**
 * A Cleaner osztály a takarítók működését és viselkedését modellezi.
 * A Person absztrakt osztály leszármazottja.
 */
public class Cleaner extends Person {

    /**
     * A Cleaner osztály konstruktora.
     * Létrehoz és inicializál egy Cleaner objektumot.
     *
     * @param stunRemaining Az idő, ameddig a személy le van bénulva.
     * @param location A szoba, ahol a személy van.
     */
    public Cleaner(int stunRemaining, Room location){
        super(stunRemaining, location);
    }

    /**
     * A takarító mozgását végrehajtó metódus.
     * A takarító mozgási metódusa ugyanazzal kezdődik, mint az eredeti, ősben definiált.
     * Majd amennyiben a location-je módosult, meghívja a location-ön a {@code setGas(false)} és a {@code clean()} metódusait.
     * 
     * @param roomTo Az a szoba, ahova át akar lépni.
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
     * A takarító nem csinál semmit.
     * 
     * @param person A személy, akivel találkozik.
     */
    @Override
    public void meet(Person person) {}

    /**
     * A takarító kibuktatásának megkísérlése.
     * A takarító nem tud kibukni, nem történik semmi.
     * 
     * @param killer A személy aki megölné.
     */
    @Override
    public void kill(Person killer) {}

    /**
     * Rongy miatti elkábulás.
     * Nem történik semmi, mivel a takarítót nem veszélyezteti a táblatörlő rongy.
     */
    @Override
    public void slip() {}

    /**
     * Mérgező gáz miatti elkábulás.
     * Nem történik semmi, a takarító rezisztens a mérgező gázra.
     */
    @Override
    public void stun() {}

    /**
     * Logarléc felvétele.
     * A takarító kidobja a logarlécet.
     * 
     * @param slideRule A logarléc.
     */
    @Override
    public void pickedUpSlideRule(SlideRule slideRule) { dropItem(slideRule); }

    /**
     * A takarító köszönésre reagálása.
     * A takarító nem csinál semmit.
     * 
     * @param greeter A személy aki köszönt neki.
     */
    @Override
    public void greet(Person greeter) {}
}