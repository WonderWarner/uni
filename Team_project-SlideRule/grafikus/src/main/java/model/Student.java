package model;

/**
 * A Student osztály a hallgatók működését és viselkedését modellezi.
 * A játékosok által elvégezhető funkciókat valósítja meg.
 * A Person absztrakt osztály leszármazottja.
 */
public class Student extends Person{

    /**
     * A Student osztály konstruktora.
     * Létrehoz és inicializál egy Student objektumot.
     *
     * @param stunRemaining Az idő, ameddig a személy le van bénulva.
     * @param location A szoba, ahol a személy van.
     */
    public Student(int stunRemaining, Room location){
        super(stunRemaining, location);
    }

    /**
     * Egy személlyel való találkozást kezeli le.
     * Köszön a személynek.
     * 
     * @param person A személy, akivel találkozik.
     */
    @Override
    public void meet(Person person) {
        person.greet(this);
    }

    /**
     * A hallgató megtámadását kezeli le. Ha képes magát megvédeni
     * valamely tárgya segítségével, akkor nem történik vele semmi.
     * Ha nem sikerül magát megvédenie, akkor kibukik és törli magát a tartózkodási helyéről.
     * 
     * @param killer Az a személy, aki ki akarja buktatni.
     */
    @Override
    public void kill(Person killer) {
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
    }

    /**
     * Rongy miatti elkábulás
     * Nem történik semmi, mivel a hallgatót nem veszélyezteti a táblatörlő rongy.
     */
    @Override
    public void slip() {}

    /**
     * Logarléc felvétele.
     * Nem történik semmi, nála maradhat a Logarléc.
     * 
     * @param slideRule A logarléc.
     */
    @Override
    public void pickedUpSlideRule(SlideRule slideRule) {}

    /**
     * A hallgató köszönésre reagálása
     * Nem csinál semmit a hallgató.
     * 
     * @param greeter A másik személy.
     */
    @Override
    public void greet(Person greeter) {}

    /**
     * Egy tárgy aktiválása.
     * 
     * @param item A használni kívánt tárgy.
     */
    public void activateItem( Item item ){
        item.activate();
    }
}
