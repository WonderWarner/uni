package model;

import testing.Logger;

import java.util.stream.Stream;

/**
 * Az Item absztrakt osztály a különböző tárgyak
 * számára nyújt alaposztályt, amiből származhatnak.
 * Számon tartja, hogy a tárgy melyik szobában van,
 * illetve, ha egy személy kezében van, akkor azt is, hogy kinél.
 * A TimeSensitive interfészt implementálja.
 */
public abstract class Item implements TimeSensitive {

    protected Room location;
    protected Person holder;

    /**
     * A tárgy kezdeti helyének, illetve birtokosának beéllítására szolgál.
     * A függvényhívást és visszatérést logolja.
     * @param room a szoba ahova a tárgyat elhelyezi
     * @param person a személy akinek a kezébe adja, {@code null} esetén nem adja senki kezébe
     */
    public void initLocation( Room room, Person person ){
        Stream<Object> args = Stream.of( room, person);
        Logger.enter( this, "initLocation", args.toList() );

        location = room;
        holder = person;

        Logger.exit( this, "initLocation" );
    }

    /**
     * Az tárgy aktiválása.
     */
    public abstract void activate();

    /**
     * Ha a tárgy a földön van egy szpbában,
     * a szobába belépő új személyekkel való találkozást kezeli.
     * @param person a személy, akivel találkozik a tárgy
     */
    public abstract void meet( Person person );

    /**
     * Átálítja a tárgy tartózkodási helyét és birtokosát.
     * A függvényhívást és visszatérést logolja.
     * @param room a szoba ahova a tárgyat elhelyezi
     * @param person a személy akinek a kezébe adja, {@code null} esetén nem adja senki kezébe
     */
    public void setLocation( Room room, Person person ){
        Stream<Object> args = Stream.of( room, person );
        Logger.enter( this, "setLocation", args.toList() );

        location = room;
        holder = person;

        Logger.exit( this, "setLocation" );
    }

    /**
     * A visszatérési értéke logikai igazzal tér vissza,
     * ha tárgy a kibukással szemben védelmet biztosít,
     * egyébként logikai hamissal tér vissza.
     * @param killer a támadó személy
     * @return {@code true} ha védelmet biztosít, {@code false} egyébként
     */
    public abstract boolean saveFromDeath( Person killer );

    /**
     * A metódus visszatérési értéke logikai igaz, ha a tárgy a mérgező gázzal
     * védelmet biztosít és logikai hamis, ha nem.
     * @return {@code true} ha védelmet biztosít, {@code false} egyébként
     */
    public abstract  boolean saveFromGas();
}
