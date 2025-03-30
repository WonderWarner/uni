package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A Person absztrakt osztály a játék karaktereinek működéséért felelős, főbb adatait tárolja.
 * Tárolja a személy tartózkodási szobáját és tárgyait.
 * Az osztály megvalósítja az ItemHandler és TimeSensitive interfészeket.
 */
public abstract class Person implements ItemHandler, TimeSensitive{

    //Ennyi ideig van még elkábulva a személy. Ha nincs elkábulva, értéke 0
    protected int stunRemaining;
    //Az elkábulás pillanatától ennyi ideig marad elkábulva a személy
    protected static final int STUNSTART=3;
    //Személy tartózkodási helye
    protected Room location;
    //Személy tárgyai
    protected final List<Item> itemsInHand = new ArrayList<>();
    //Ennyi tárgy lehet legfeljebb minden egyes személynél
    protected static final int ITEMSINHANDLIMIT=5;
    //Söröspohár hatására eldobandó véletlen tárgy indexének generálására használt objektum
    protected static Random random=new Random();

    /**
     * A Person osztály konstruktora.
     * Inicializálja egy Person objektum kábultságát és helyzetét.
     *
     * @param stunRemaining az idő, ameddig a személy le van bénulva
     * @param location a szoba, ahol a személy vam
     */
    public Person(int stunRemaining, Room location) {
        this.stunRemaining=stunRemaining;
        this.location=location;
    }

    /**
     * A személy tartózkodási helyének beállítása.
     * @param room a tartózkodási hely
     */
    public void setLocation( Room room ){
        location = room;
    }

    /**
     * Az inicializálás során a személy kezébe ad egy tárgyat.
     * @param item a tárgy, amit a kezébe ad
     */
    public void initItem( Item item ) {
        if(itemsInHand.size()<ITEMSINHANDLIMIT) itemsInHand.add(item);
    }

    /**
     * Egy tárgy felvétele, amennyiben a lehetséges
     * @param item a felvenni kívánt tárgy
     */
    @Override
    public void addItem(Item item) {
        if(stunRemaining==0&&itemsInHand.size()<ITEMSINHANDLIMIT&&location.pickUpItem(item)) {
            itemsInHand.add(item);
            item.setLocation(location, this);
        }
    }

    /**
     * Egy tárgy törlése a személy kezéből.
     * @param item a törölni kívánt tárgy
     */
    @Override
    public void removeItem(Item item) {
        itemsInHand.remove(item);
    }

    /**
     * Időtelés szimulálása.
     * A személy továbbítja az eltelt időt (time) a nála lévő tárgyaknak.
     * Amennyiben kábult a személy, csökkenti a hátralévő kábulási időt.
     * @param time az eltelt idő
     */
    @Override
    public void timeElapsed(int time) {
        if(stunRemaining > 0) {
            stunRemaining -= time;
            if (stunRemaining < 0) stunRemaining = 0;
        }
        List<Item> itemsInHandCopy = new ArrayList<>(itemsInHand);
        for(Item item: itemsInHandCopy) {
            item.timeElapsed(time);
        }
    }

    /**
     * A személy mozgását végrehajtó metódus.
     * Ha a személy nincs elkábulva, továbbítja a jelenlegi szobájának az átlépés igényét.
     * A két szoba felelőssége, hogy a személyt beengedi-e.
     * Amennyiben sikeresen átlép a másik szobába, frissíti a tárgyainak tartózkodási helyét is.
     * @param roomTo az a szoba, ahova át akar lépni
     */
    public void enterRoom( Room roomTo ){
        if(stunRemaining==0) {
            boolean moveSuccess=location.movePerson(this, roomTo);
            if(moveSuccess) {
                for(Item item: itemsInHand) {
                    item.setLocation(location, this);
                }
            }
        }
    }

    /**
     * Egy tárgy eldobása. A személy kezéből eltávolítja a tárgyat és hozzáadja a szobához.
     * @param item az eldobni kívánt tárgy
     */
    public void dropItem( Item item ){
        itemsInHand.remove( item );
        location.addItem( item );
    }

    /**
     * Egy véletlen tárgy eldobása.
     * Az ember kezéből egy véletlenszerűen választott tárgyat eldob a szobába.
     * A véletlenszerű kiválasztás pszeudo-random módon történik, a seedje állítható teszteléshez.
     */
    public void dropRandomItem(){
        int index=random.nextInt(itemsInHand.size());
        dropItem(itemsInHand.get(index));
    }

    /**
     * Egy személlyel való találkozást kezeli le.
     * @param person a személy, akivel találkozik
     */
    public abstract void meet( Person person );

    /**
     * A személy mérgező gáz általi megbénulását hajtja végre. Először is
     * végigkérdezi a tárgyait, hogy képesek-e megóvni őt a mérgező gáz általi veszélytől.
     * Ha legalább egy megvédi, akkor nem történik a személlyel semmi. Ha egy sem védi
     * meg, akkor eldobja az összes tárgyát és meghatározott időre elkábul.
     */
    public void stun(){
        boolean saved = false;
        for( Item item : itemsInHand ){
            if(item.saveFromGas() ){
                saved = true;
                break;
            }
        }

        if( !saved ){
            stunRemaining=STUNSTART;
            List<Item> itemsInHandCopy = new ArrayList<>(itemsInHand);
            for( Item item : itemsInHandCopy ){
                dropItem( item );
            }
        }
    }

    /**
     * A személy kibuktatását/halálát hajtja végre
     * @param killer az a személy, aki ki akarja buktatni
     */
    public abstract void kill(Person killer);

    /**
     * A személy táblatörlő rongy általi megbénulását hajtja végre.
     */
    public abstract void slip();

    /**
     * A személy felvette a Logarlécet.
     * @param slideRule a logarléc
     */
    public abstract void pickedUpSlideRule( SlideRule slideRule );

    /**
     * A személynek köszönt a másik személy.
     * @param greeter a másik személy
     */
    public abstract void greet( Person greeter );

    /*
     * hátralévő kábulási idő lekérdezése
     */
    public int getStunRemaining() {
        return stunRemaining;
    }

    /*
     * hátralévő kábulási idő beállítása
     */
    public void setStunRemaining(int stunRemaining) {
        this.stunRemaining = stunRemaining;
    }

    /*
     * tartózkodási szoba lekérdezése
     */
    public Room getLocation(){
        return location;
    }

    /*
     * kézben lévő tárgyak lekérdezése
     */
    public List<Item> getItemsInHand() {
        return itemsInHand;
    }

    /*
     * random objektum seedjének beállítása teszteléshez
     */
    public void setSeed(long seed) {
        random.setSeed(seed);
    }

}
