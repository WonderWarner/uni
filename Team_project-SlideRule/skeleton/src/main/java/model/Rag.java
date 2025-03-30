package model;

import testing.Logger;

import java.util.List;

/**
 * A Rag osztály a nedves táblatörlő rongy működését és viselkedését modellezi.
 * A rongy aodtt ideig bénítja a vele egy helységben lévő oktatókat. A hallgató tudja aktiválni.
 * Ha a hatása lejár, akkor megsemmisül.
 * Az IntervalItem absztrakt osztály leszármazottja.
 */
public class Rag extends IntervalItem{
    /**
     * A Rag osztály kontruktora.
     * Létrehoz és inicializál egy Rag objektumot és ennek tényét logolja.
     */
    public Rag(){
        Logger.create(this);
        Logger.exitCreate(this);
    }

    /**
     * Egy személlyel való találkozást kezeli le. Kezdeményezi a személy megbénítását.
     * A függvényhívást és visszatérést logolja.
     * @param person a személy, akivel találkozik a tárgy
     */
    @Override
    public void meet(Person person) {
        Logger.enter( this, "meet", List.of(person) );
        boolean success = Logger.askQuestion( "Is # activated?", this );
        if(success)
            person.slip();
        Logger.exit( this, "meet");
    }

    /**
     * Amennyiben aktiválva van a rongy, logikai igazzal
     * tér vissza, megvédve a birtokosát és a támadó személyre megbénítást kezdeményez. Egyéb
     * esetben logikai hamissal tér vissza.
     * A függvényhívást és visszatérést logolja.
     * @param killer a támadó személy
     * @return {@code true} ha aktiválva van, {@code false} egyébként
     */
    @Override
    public boolean saveFromDeath(Person killer) {
        Logger.enter( this, "saveFromDeath", List.of(killer) );
        boolean success = Logger.askQuestion( "Is # activated?", this );
        if(success)
            killer.slip();
        Logger.exit( this, "saveFromDeath", success ? "true" : "false" );
        return success;
    }

    /**
     *  A tárgy nem nyújt védelmet a gáz ellen.
     * @return {@code false} minden esetben
     */
    @Override
    public boolean saveFromGas() {
        Logger.enter(this, "saveFromGas");
        Logger.exit(this, "saveFromGas", "false");
        return false;
    }

    /**
     * Ha aktiválva van a tárgy, akkor a timeRemaining értékét
     * csökkenti time-mal. Ha elérte a 0-t, akkor aktuális birtokosánál kezdeményezi a tárgy
     * megsemmisítését.
     * A felhasználótól kérdezi meg, hogy a timeRemaining elérte-e a 0-t.
     * A függvényhívást és visszatérést logolja.
     * @param time az eltelt idő
     */
    @Override
    public void timeElapsed(int time) {
        Logger.enter(this, "timeElapsed", List.of(time));
        boolean isActivated = Logger.askQuestion( "Is # activated?", this );
        if(isActivated) {
            boolean isTimeRemaining = Logger.askQuestion("Is there any time remaining for #?", this);
            if(!isTimeRemaining) {
                if (holder != null) {
                    holder.removeItem(this);
                }
                else {
                    location.removeItem(this);
                }
            }
        }
        Logger.exit(this, "timeElapsed");
    }
}
