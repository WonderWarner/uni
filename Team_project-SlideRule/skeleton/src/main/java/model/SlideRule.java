package model;

import testing.Logger;

import java.util.List;
import java.util.stream.Stream;

/**
 * A SlideRule osztály a Logarléc működését és viselkedését modellezi.
 * A Logarléc a hallgatő győzelmének kulcsa. Amint egy hallgató felvétel után aktiválódik, a hallgatók győznek.
 * Az Item absztrakt osztály leszármazottja.
 */
public class SlideRule extends Item{

    /**
     * A SlideRule osztály kontruktora.
     * Létrehoz és inicializál egy SlideRule objektumot és ennek tényét logolja.
     */
    public SlideRule(){
        Logger.create(this);
        Logger.exitCreate(this);
    }

    /**
     * Nem történik semmi.
     * A függvényhívást és visszatérést logolja.
     */
    @Override
    public void activate() {
        Logger.enter( this, "activate");
        Logger.exit( this, "activate");
    }

    /**
     * Nem csinál semmit, mert ha földön van nincs funkciója.
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
     * A tárgy nem nyújt védelmet a gáz ellen.
     * @return {@code false} minden esetben
     */
    @Override
    public boolean saveFromGas() {
        Logger.enter( this, "saveFromGas");
        Logger.exit( this, "saveFromGas", "false" );
        return false;
    }

    /**
     * A tárgy tartózkodási helyének és birtokosának beállítása.
     * @param room a szoba ahova a tárgyat elhelyezi
     * @param person a személy akinek a kezébe adja, {@code null} esetén nem adja senki kezébe
     */
    @Override
    public void setLocation( Room room, Person person ){
        Stream<Object> args = Stream.of( room, person );
        Logger.enter( this, "setLocation", args.toList() );

        location = room;
        holder = person;
        if(person!=null){
            person.pickedUpSlideRule(this);
        }

        Logger.exit( this, "setLocation" );
    }

    /**
     * Mivel egyszerhasználatos tárgy, így nem történik vele semmi az idő múlásával.
     * @param time az eltelt idő
     */
    @Override
    public void timeElapsed(int time) {
        Logger.enter(this, "timeElapsed", List.of(time));
        Logger.enter(this, "timeElapsed");
    }
}
