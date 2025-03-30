package model;

import java.util.HashMap;

/**
 * A Transistor osztály modellezi a tranzisztorok műkösését és viselkedését a játékban.
 * Ahhoz hogy egy tranzisztort teleportálásra lehessen használni
 * kettő összepárosított tranzisztorra van szükség. A tranzisztor a párját
 * a pair tagváltozójában tartja nyilván. Ha két tranzisztor összekapcsolódott, már nem kapcsolódhatnak szét.
 * Az Item absztrakt osztály leszármazottja.
 */
public class Transistor extends Item{

    /**
     * A tranzisztor párja.
     * Ha nincs párosítva, értéke {@code null}.
     */
    private Transistor pair=null;

    /**
     * A tranzisztorok személyenkénti párosodási kérelmének tárolója.
     */
    private static final HashMap<Person,Transistor> pairingRequests = new HashMap<>();

    /**
     * A Transistor osztály konstruktora.
     * Létrehoz és inicializál egy Transistor objektumot.
     *
     * @param location A szoba, amiben a tárgy van.
     * @param holder A személy, akinél a tárgy van.
     */
    public Transistor(Room location, Person holder){
        super(location, holder);
    }

    /**
     * A paraméterben kapott tranzisztorral összepárosítja az objektumot.
     * 
     * @param transistor A párosítani kívánt tranzisztor.
     */
    public void setPair(Transistor transistor) {
        this.pair = transistor;
    }

    /**
     * Tranzisztor aktiválása: párosítás és teleportálás kivitelezése
     * Két tranzisztor összekapcsolását illetve azok használatát is lehetővé
     * tevő metódus. Amennyiben egy t1 tranzisztort úgy aktiválunk, hogy a hallgatónál még
     * nincs másik aktivált tranzisztor és a t1.pair NULL, akkor a t1 párosítási szándékát
     * eltároljuk. Amennyiben egy t2 tranzisztort úgy aktiválunk, hogy a t2.pair értéke
     * NULL, de a hallgató aktuálisan is rendelkezik egy már párosítási szándékra bejegyzett
     * t1 tranzisztorral, akkor a t1-et és a t2-t összekapcsolja, és kiveszi őket a párosítási
     * jegyzékből. Amennyiben egy t1 tranzisztort úgy aktiválunk, hogy a t1.pair értéke nem
     * NULL, akkor t1-et eldobja a játékos az eredeti szobájában és a t1 birtokosát átlépteti
     * az {@code enterRoom()} metódus segítségével, t2 tartózkodási helyébe, amit paraméterként ad
     * át a személy metódusába.
     */
    @Override
    public void activate() {
        if(pair != null){
            Person personToMove = holder;
            personToMove.dropItem( this );
            personToMove.enterRoom( pair.location );
        } else{
            Transistor candidate = pairingRequests.get(holder);
            if(candidate == null || candidate.holder != holder || candidate == this){
                pairingRequests.put(holder, this);
            }else{
                pair = candidate;
                candidate.pair = this;
                pairingRequests.remove(holder);
            }
        }
    }

    /**
     * Találkozás emberrel.
     * Nem csinál semmit.
     * @param person A személy, akivel találkozik a tárgy.
     */
    @Override
    public void meet(Person person) { }

    /**
     * Kibukás elleni védelem kérése.
     * Nem véd meg a kibukástól.
     * @param killer A támadó személy.
     * @return {@code false} minden esetben.
     */
    @Override
    public boolean saveFromDeath(Person killer) {
        return false;
    }

    /**
     * Mérgező gáz elleni védelem kérése.
     * Nem véd meg a gáztól.
     * @return {@code false} minden esetben.
     */
    @Override
    public boolean saveFromGas() {
        return false;
    }

    /**
     * Idő telése a tárgyon.
     * Nem történik vele semmi.
     * @param time Az eltelt idő.
     */
    @Override
    public void timeElapsed(int time) {
    }

    /*
     * A tranzisztor párjának lekérdezése.
     */
    public Transistor getPair() {
        return pair;
    }
}
