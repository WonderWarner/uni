package model;

import testing.Logger;

import java.util.List;

/**
 * A Mask osztály az FFP2-es maszk működését és viselkedését modellezi.
 * A hallgató képes manuálian aktiválni, de egyébként automatikusan is aktiválódik a személyeknek.
 * Ha a hatása elmúlik, akkor a saját felelőssége eldönteni,
 * hogy használható-e újra rövidebb ideig. Ha ez az idő 0-ra csökken, akkor megsemmisül.
 * Az IntervalItem leszármazottja.
 */
public class Mask extends IntervalItem{
    /**
     * A Mask osztály konstriuktora.
     * Létrehoz és inicializál egy Mask objektumot és ennek tényét logolja.
     */
    public Mask(){
        Logger.create(this);
        Logger.exitCreate(this);
    }

    /**
     * Nem csinál semmit, mert ha földön van nincs kit megvédenie.
     * A függvényhívást és visszatérést logolja.
     * @param person a személy, akivel találkozik a tárgy
     */
    @Override
    public void meet(Person person) {
        Logger.enter( this, "meet", List.of(person) );
        Logger.exit( this, "meet");
    }

    /**
     * A tárgy nem nyújt védelmet a kibukás ellen.
     * @param killer a támadó személy
     * @return {@code false} minden esetben
     */
    @Override
    public boolean saveFromDeath(Person killer) {
        Logger.enter( this, "saveFromDeath", List.of(killer) );
        Logger.exit( this, "saveFromDeath", "false" );
        return false;
    }

    /**
     * Amennyiben aktiválva van a maszk, logikai igazzal tér vissza,
     * megvédve a birtokosát. Ha nincs aktiválva, akkor aktiválódik, és szintén logikai
     * igazzal tér vissza
     * @return {@code true} minden esetben
     */
    @Override
    public boolean saveFromGas() {
        Logger.enter( this, "saveFromGas" );
        boolean isActivated = Logger.askQuestion( "Is # activated?", this );
        if(!isActivated)
            activate();
        Logger.exit( this, "saveFromGas", "true" );
        return true;
    }

    /**
     * Ha aktiválva van a tárgy, akkor a timeRemaining értékét
     * csökkenti time-mal. Ha elérte a 0-t, akkor adott értékkel csökkenti a durationt és
     * visszabillenti az activated flag-et 0-ra. Ha a duration elérte a 0-t akkor aktuális
     * birtokosánál kezdeményezi a tárgy megsemmisítését
     * A felhasználótól kérdezi meg, hogy aktiválva van-e, illetve hogy a duration és timeRemaining
     * értékek elértéke a 0-t.
     * @param time az eltelt idő
     */
    @Override
    public void timeElapsed(int time) {
        Logger.enter(this, "timeElapsed", List.of(time));
        boolean isActivated = Logger.askQuestion( "Is # activated?", this );
        if(isActivated) {
            boolean isOver = Logger.askQuestion("Are both duration and timeRemaining of # zero?", this);
            if(isOver) {
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
