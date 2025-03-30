package model;

import testing.Logger;

import java.util.List;

/**
 * A Student osztály a hallgatók működését és viselkedését modellezi.
 * A játékosok által elvégezhető funkciókat valósítja meg.
 * A Person absztrakt osztály leszármazottja.
 */
public class Student extends Person{

    /**
     * A Student osztály konstruktora.
     * Létrehoz és inicializál egy Student objektumot és ennek tényét logolja.
     */
    public Student(){
        Logger.create(this);
        Logger.exitCreate(this);
    }

    /**
     * Egy személlyel való találkozást kezeli le. Köszön a szmélynek.
     * A függvényhívást és visszatérést logolja.
     * @param person a személy, akivel találkozik
     */
    @Override
    public void meet(Person person) {
        Logger.enter( this, "meet", List.of(person) );

        person.greet(this);

        Logger.exit( this, "meet" );
    }

    /**
     * A hallgató megtámadását kezeli le. Ha képes magát megvédeni
     * valamely tárgya segítségével, akkor nem történik vele semmi.
     * Ha nem sikerül magát megvédenie, akkor kibukik és törli magát a tartózkodási helyéről.
     * A függvényhívást és visszatérést logolja.
     * @param killer az a személy, aki ki akarja buktatni
     */
    @Override
    public void kill(Person killer) {
        Logger.enter( this, "kill", List.of(killer) );

        boolean saved = false;
        for( Item item : itemsInHand ){
            if(item.saveFromDeath( killer ) ){
                saved = true;
                break;
            }
        }
        if( !saved ){
            location.removePerson(this);
        }

        Logger.exit( this, "kill" );
    }

    /**
     * Nem történik semmi, mivel a hallgatót nem veszélyezteti a táblatörlő rongy.
     * A függvényhívást és visszatérést logolja.
     */
    @Override
    public void slip() {
        Logger.enter(this, "slip");
        Logger.exit(this, "slip");
    }

    /**
     * Nem történik semmi.
     * A függvényhívást és visszatérést logolja.
     * @param slideRule a logarléc
     */
    @Override
    public void pickedUpSlideRule(SlideRule slideRule) {
        Logger.enter(this, "pickedUpSlideRule");
        Logger.exit(this, "pickedUpSlideRule");
    }

    /**
     * Nem csinál semmit a hallgató.
     * A függvényhívást és visszatérést logolja.
     * @param greeter a másik személy
     */
    @Override
    public void greet(Person greeter) {
        Logger.enter(this, "greet");
        Logger.exit(this, "greet");
    }

    /**
     * Egy tárgy használata.
     * A függvényhívást és visszatérést logolja.
     * @param item a használni kívánt tárgy
     */
    public void activateItem( Item item ){
        Logger.enter(this, "activateItem", List.of(item));
        item.activate();
        Logger.exit(this, "activateItem");
    }
}
