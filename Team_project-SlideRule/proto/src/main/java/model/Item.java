package model;

/**
 * Az Item absztrakt osztály a különböző tárgyak
 * számára nyújt alaposztályt, amiből származhatnak.
 * Számon tartja, hogy a tárgy melyik szobában van,
 * illetve, ha egy személy kezében van, akkor azt is, hogy kinél.
 * Felel az aktiválás lehetőségéért és a védelemnyújtásért.
 * A TimeSensitive interfészt implementálja.
 */
public abstract class Item implements TimeSensitive {

    //A tárgy tartózkodási szobája, melyet akkor is számon tartunk ha egy személy kezében van
    protected Room location;
    //A tárgy birtokosa. Amennyiben a földön van, értéke null
    protected Person holder;

    /**
     * Az Item osztály konstruktora.
     * Inicializál egy Item objektumot birtokossal és elhelyezkedéssel.
     *
     * @param location a szoba, amiben a tárgy van
     * @param holder a személy, akinél a tárgy van
     */
    public Item(Room location, Person holder){
        this.location = location;
        this.holder = holder;
    }

    /**
     * A tárgy aktiválása.
     */
    public abstract void activate();

    /**
     * Ha a tárgy a földön van egy szobában,
     * a szobába belépő új személyekkel való találkozást kezeli.
     * @param person a személy, akivel találkozik a tárgy
     */
    public abstract void meet( Person person );

    /**
     * Átállítja a tárgy tartózkodási helyét és birtokosát.
     * @param room a szoba ahova a tárgyat elhelyezi
     * @param person a személy akinek a kezébe adja, {@code null} esetén nem adja senki kezébe
     */
    public void setLocation( Room room, Person person ){
        this.location = room;
        this.holder = person;
    }

    /**
     * Tárgy megkérése, hogy védje meg birtokosát a kibukás ellen.
     * A visszatérési értéke logikai igazzal tér vissza,
     * ha tárgy a kibukással szemben védelmet biztosít,
     * egyébként logikai hamissal tér vissza.
     * @param killer a támadó személy
     * @return {@code true} ha védelmet biztosít, {@code false} egyébként
     */
    public abstract boolean saveFromDeath( Person killer );

    /**
     * Tárgy megkérése, hogy védje meg birtokosát mérgező gáz ellen.
     * A metódus visszatérési értéke logikai igaz, ha a tárgy a mérgező gázzal
     * védelmet biztosít és logikai hamis, ha nem.
     * @return {@code true} ha védelmet biztosít, {@code false} egyébként
     */
    public abstract  boolean saveFromGas();

    /*
     * Tárgy tartózkodási helyének lekérdezése
     */
    public Room getLocation(){
        return location;
    }

    /*
     * Tárgy birtokosának lekérdezése
     */
    public Person getHolder(){
        return holder;
    }

}
