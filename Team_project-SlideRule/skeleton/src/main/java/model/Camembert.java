package model;

import testing.Logger;

import java.util.List;

/**
 * A Camembert osztály a káposztás camembert működését és viselkedését modellezi.
 * Aktiválásakor a szobát, amiben van mérgeszó gázzal árasztja el és eltűnik.
 * Az Item absztrakt osztályból származik.
 */
public class Camembert extends Item{
    /**
     * A Camembert osztály konstruktora.
     * Létrehoz és inicializál egy Camembert objektumot és ennek tényét logolja.
     *
     */
    public Camembert(){
        Logger.create(this);
        Logger.exitCreate(this);
    }

    /**
     * A camembert aktiválása.
     * Igényli birtokosánál az önmegsemmisítést.
     * Tartózkodási szobáján meghívja a location.createGas() metódust és elgázosítja azt.
     * A függvényhívást és visszatérést logolja.
     */
    @Override
    public void activate() {
        Logger.enter(this, "activate");
        holder.removeItem(this);
        location.createGas();
        Logger.exit(this, "activate");
    }

    /**
     * Egy személlyel való találkozást kezeli, ha a földön van.
     * Nincsen semmilyen hatása.
     * A függvényhívást és visszatérést logolja.
     * @param person Az a személy, akivel találkozik
     */
    @Override
    public void meet(Person person) {
        Logger.enter( this, "meet", List.of(person) );
        Logger.exit( this, "meet");
    }

    /**
     * A tárgy nem nyújt védelmet támadás ellen.
     * A függvényhívást és visszatérést logolja.
     * @param killer az a személy, aki megtámadta a BeerGlass objektum tulajdonosát
     * @return {@code false} minden esetben
     */
    @Override
    public boolean saveFromDeath(Person killer) {
        Logger.enter( this, "saveFromDeath", List.of(killer) );
        Logger.exit( this, "saveFromDeath", "false" );
        return false;
    }

    /**
     * A tárgy nem nyújt védelmet gáz ellen.
     * A függvényhívást és visszatérést logolja.
     * @return {@code false} minden esetben
     */
    @Override
    public boolean saveFromGas() {
        Logger.enter( this, "saveFromGas");
        Logger.exit( this, "saveFromGas", "false" );
        return false;
    }

    /**
     * Az idő műlása nincs rá hastással, mert egyszerhasználatos tárgy.
     * A függvényhívást és visszatérést logolja.
     * @param time az eltelt idő
     */
    @Override
    public void timeElapsed(int time) {
        Logger.enter(this, "timeElapsed", List.of(time));
        Logger.enter(this, "timeElapsed");
    }
}
