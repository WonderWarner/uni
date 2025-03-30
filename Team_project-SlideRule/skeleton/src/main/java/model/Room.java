package model;

import testing.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * A Room osztály a szoba működését és viselkedését modellezi.
 * Számontartja a szobában lévő személyeket és tárgyakat, illetve a szomszédait.
 * Az ItemHandler és TimeSensitive interfészeket implementálja.
 */
public class Room implements ItemHandler, TimeSensitive {

    private final List<Person> peopleInRoom = new ArrayList<>();
    private final List<Item> itemsInRoom = new ArrayList<>();

    private final List<Room> neighbours =new ArrayList<>();

    /**
     * A Room osztály konstruktora.
     * Létrehoz és inicializás egy Room objektumot és ennek tényét logolja.
     */
    public Room(){
        Logger.create(this);
        Logger.exitCreate(this);
    }

    /**
     * A szoba szomszédai közé felvesz egy szobát.
     * A függvényhívást és visszatérést logolja.
     * @param room a szomszédok listájába felvenni kívánt szoba
     */
    public void addNeighbour(Room room) {
        Logger.enter(this, "addNeighbour", List.of(room));
        neighbours.add(room);
        Logger.exit(this, "addNeighbour");
    }

    /**
     * A szobában elhelyez egy személyt.
     * A függvényhívást és visszatérést logolja.
     * @param person az elhelyezni kívánt személy
     */
    public void addPerson(Person person) {
        Logger.enter(this, "addPerson", List.of(person));
        peopleInRoom.add(person);
        Logger.exit(this, "addPerson");
    }

    /**
     * A szobában elhelyez egy tárgyat.
     * A függvényhívást és visszatérést logolja.
     * @param item az elhelyezni kívánt tárgy
     */
    public void initItem(Item item) {
        Logger.enter(this, "initItem", List.of(item));
        itemsInRoom.add(item);
        Logger.exit(this, "initItem");
    }

    /**
     * Egy tárgyat elhelyez a szobában és annak tartózkodási helyét is beállítja.
     * A függvényhívást és visszatérést logolja.
     * @param item a hozzáadni kívánt tárgy
     */
    @Override
    public void addItem(Item item) {
        Logger.enter(this, "addItem", List.of(item));
        itemsInRoom.add(item);
        item.setLocation( this, null );
        Logger.exit(this, "addItem");
    }

    /**
     * Egy tárgy törlése a szobából.
     * A függvényhívást és visszatérést logolja.
     * @param item a törölni kívánt tárgy
     */
    @Override
    public void removeItem(Item item) {
        Logger.enter( this, "removeItem", List.of(item));

        itemsInRoom.remove( item );

        Logger.exit( this, "removeItem" );
    }

    /**
     * Továbbítja az eltelt időt a benne lévő személyeknek és tárgyaknak.
     * A szoba belsőmechanizmusában is részt vesz pl.: egy elátkozott szoba ajtajainak el(ő)tűnése az eltelt idő alapján.
     * A továbbra is szobában tartózkodó tárgyakat összetalálkoztatja minden személlyel és
     * minden személyt kölcsönösen összetalálkoztat egymással.
     * Illetve megkérdezi a felhasználótól, hogy a szoba gázos-e. Ha igen, akkor a szobában tartózkodó
     * személyek megbénítását kezdi.
     * A függvényhívást és visszatérést logolja.
     * @param time az eltelt idő
     */
    @Override
    public void timeElapsed(int time) {
        Logger.enter( this, "timeElapsed", List.of( time ) );

        List<Item> itemsInRoomCopy = new ArrayList<>(itemsInRoom);
        for( Item item : itemsInRoomCopy ){
            item.timeElapsed( time );
        }
        for( Person person : peopleInRoom ){
            person.timeElapsed( time );
        }
        for( Person person : peopleInRoom ){
            for( Item item : itemsInRoom ) {
                item.meet( person );
            }
        }
        List<Person> peopleInRoomCopy = new ArrayList<>(peopleInRoom);
        for( int i = 0 ; i < peopleInRoomCopy.size() ; i++ ){
            for( int j = i + 1 ; j < peopleInRoomCopy.size() ; j++ ){
                peopleInRoomCopy.get( i ).meet( peopleInRoomCopy.get( j ) );
            }
        }

        Logger.exit( this, "timeElapsed" );
    }

    /**
     * A paraméterként kapott személyt engedi be a szobába.
     * Amennyiben a szoba kapacitása kimerült nem engedi be a személyt. A visszatérési
     * értéke a beengedés sikeressége. Ha beengedi a személyt, felel az új személy és a
     * szobában tartózkodó személyek kölcsönös találkozásáért, illetve az új személy és
     * szobában levő tárgyak találkozásáért. Ha a szoba mérgezett, felel a belépő játékos
     * elkábításáért.
     * Azt, hogy a szoba tele van-e, és hogy el van-e gázosítva a felhasználótól megkérdezi.
     * A függvényhívást és visszatérést logolja.
     * @param person a belépő személy
     * @return {@code true} ha sikeresen belépett a szobába {@code false} egyébként
     */
    public boolean acceptPerson( Person person ){
        Logger.enter( this, "acceptPerson", List.of( person ) );

        if( Logger.askQuestion( "Is # full?", this) ){
            Logger.exit( this, "acceptPerson", "false" );
            return false;
        }
        person.setLocation( this );

        if( Logger.askQuestion( "Is # gassed?", this ) ){
            person.stun();
        }

        for( Item item : itemsInRoom ){
            item.meet( person );
        }

        peopleInRoom.add( person );

        for( Person personInRoom : peopleInRoom ){
            if(personInRoom==person) continue;
            person.meet( personInRoom );
        }

        Logger.exit( this, "acceptPerson", "true" );
        return true;
    }

    /**
     * Ezzel a függvénnyel kerül indítványozásra a meghívott szobánál, hogy
     * kettéosztódjon. Amennyiben tartózkodik benne személy NULL-t ad vissza. Különben
     * pedig létrehoz egy új szobát, aminek neighbours-ei a saját neighbours-ei fele és saját
     * maga, az itemsInRoom a saját itemsInRoom-jainak szintén fele lesz. Az átadott
     * szomszédokat és tárgyakat saját magából eltávolítja, az új szobát beállítja saját
     * szomszédjának is. Az átadott tárgyak location-jét frissíti setLocation()-nel. A gas és
     * cursed értékeiből, ha mindkettő logikai igaz volt, akkor az erediben a gas hamis lesz,
     * és az újban lesz a gas igaz. Ha a két értékből nem volt mindkettő logikai igaz, akkor az új
     * szoba mindkét értéke hamis lesz. Az új szoba capacity-je a régiével egyezik meg.
     * A felhasználótól kérdezi meg, hogy vannak-e a szobában.
     * A függvényhívást és visszatérést logolja.
     * @return az új {@code Room} objektum, ha sikeres volt a szétválás, {@code null} egyébként
     */
    public Room split(){
        Logger.enter( this, "split" );

        if( Logger.askQuestion( "Is anyone in #?", this ) ){
            Logger.exit( this, "split", "null");
            return null;
        }
        Room r2 = new Room();
        Logger.register( r2, "r2" );

        int desiredSize = itemsInRoom.size() / 2;

        while( itemsInRoom.size() > desiredSize ){
            r2.addItem( itemsInRoom.get( 0 ) );
            itemsInRoom.remove( 0 );
        }

        Logger.exit( this, "split", "r2" );
        return r2;
    }

    public Room requestMerge( Room room2 ){
        Logger.enter( this, "requestMerge", List.of(room2));
        if( Logger.askQuestion( "Is anyone in #?", this ) ){
            Logger.exit( this, "requestMerge", "null");
            return null;
        }
        Room r3 = room2.merge(this);
        if(r3==null) {
            Logger.exit( this, "requestMerge", "null");
            return null;
        }
        for(Item item: this.itemsInRoom) {
            r3.addItem(item);
        }
        Logger.exit( this, "requestMerge", "r3");
        return r3;
    }

    /**
     * Két szoba egyesítését elvégző belső függvény.
     * Ha nincs benne egy személy sem, létrehoz egy új szobát.
     * Az új szoba szomszédjait beállítja a sajátjai alapján és az összes benne lévő tárgyat áthelyezi az új szobába.
     * Azt, hogy van-e a szobában személy, a felhasználótól kérdezi meg.
     * A függvényhívást és visszatérést logolja.
     * @param room1 az a szoba, ami össze akar olvasódni
     * @return {@code Room} ha sikeres az összeolvadás, {@code null} egyébként
     */
    private Room merge( Room room1 ){
        Logger.enter( this, "merge", List.of(room1));
        if( Logger.askQuestion( "Is anyone in #?", this ) ){
            Logger.exit( this, "merge", "null");
            return null;
        }
        Room r3 = new Room();
        Logger.register(r3, "r3");
        for(Item item: itemsInRoom) {
            r3.addItem(item);
        }
        Logger.exit( this, "merge", "r3");
        return r3;
    }

    /**
     * Mérgező gázzal tölti fel a szobát.
     * A függvényhívást és visszatérést logolja.
     */
    public void createGas(){
        Logger.enter( this, "createGas" );

        for( Person person : peopleInRoom ){
            person.stun();
        }

        Logger.exit( this, "createGas" );
    }

    /**
     * Egy személy eltávolítása a szobából.
     * A függvényhívást és visszatérést logolja.
     * @param person az eltávolítani kívánt személy
     */
    public void removePerson( Person person ){
        Logger.enter( this, "removePerson", List.of( person ) );

        peopleInRoom.remove( person );

        Logger.exit( this, "removePerson" );
    }

    /**
     *Ha a szoba jelenleg nincs aktívan elátkozva, a
     * paraméterként kapott személy a paraméterként kapott szobába léptetésének igényét továbbítja. A kapott szoba
     * értesíti ennek sikerességéről és ő is ezzel tér vissza. Ha igazzal tér vissza, akkor
     * eltávolítja a személyt önmagából. Ha a szoba aktívan elátkozott, rögtön hamissal tér
     * vissza.
     * Azt, hogy a szoba el van-e átkozva a felhasználótól kérdezi meg.
     * A függvényhívást és visszatérést logolja.
     * @param person a személy, aki át akar lépni
     * @param roomTo a szoba, ahova át szeretne lépni
     * @return {@code true} ha sikeresen átlépett, {@code false} egyébként
     */
    public boolean movePerson( Person person, Room roomTo ){
        Logger.enter( this, "movePerson", List.of(person, roomTo ) );

        if( Logger.askQuestion( "Is curse active on #?", this ) ){
            Logger.exit( this, "movePerson", "false" );
            return false;
        }
        boolean success = roomTo.acceptPerson( person );

        if( success ){
            removePerson( person );
        }
        Logger.exit( this, "movePerson", success ? "true" : "false" );
        return success;
    }
}
