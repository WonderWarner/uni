package model;

import java.util.ArrayList;
import java.util.List;

/**
 * A Room osztály a szoba működését és viselkedését modellezi.
 * Számontartja a szobában lévő személyeket és tárgyakat, illetve a szomszédait.
 * Felel az emberek mozgatásáért, találkozásáért, önmaga osztódásáért, másik szobával való egyesülésért,
 * tárgyak kezeléséért, valamint az idő telésének szimulálásáért és továbbításáért.
 * Az ItemHandler és TimeSensitive interfészeket implementálja.
 */
public class Room implements ItemHandler, TimeSensitive {

    //Szoba kapacitása
    private int capacity;
    //Gázosság állapota
    private boolean gas;
    //A szoba képes-e elátkozott lenni
    private boolean cursed;
    //Az elátkozott szoba jelenleg átkozott-e
    private boolean curseActive;
    //Az elátkozott szoba mennyi idő múlva vált állapotot
    private int changeCurseIn;
    //Az elátkozott Room-ok milyen gyakran vált állapotot
    private static final int CURSESTATEINTERVAL=5;
    //Ragacsosság mértéke
    private int stickiness;
    //Ragacsosság határa minden Roomban, ha ezt az értéket eléri, akkor a tárgyak nem vehetők fel
    private static final int STICKYLIMIT=5;
    //Szobában tartózkodó emberek
    private final List<Person> peopleInRoom = new ArrayList<>();
    //Szobában levő tárgyak
    private final List<Item> itemsInRoom = new ArrayList<>();
    //Szoba szomszédainak listája, csak az odairányt teszi lehetővé
    private final List<Room> neighbours =new ArrayList<>();

    /**
     * A Room osztály konstruktora.
     * Létrehoz és inicializál egy Room objektumot.
     *
     * @param capacity a szoba kapacitása
     * @param gas a szoba gázos-e
     * @param cursed a szoba el van-e átkozva
     * @param stickiness az utolsó takarítás óta a szobába belépő személyek száma
     */
    public Room(int capacity, boolean gas, boolean cursed, int stickiness){
        this.capacity = capacity;
        this.gas = gas;
        this.cursed = cursed;
        this.stickiness = stickiness;
        this.curseActive = false;
        this.changeCurseIn = CURSESTATEINTERVAL;
    }

    /**
     * A szoba szomszédai közé felvesz egy szobát.
     * @param room a szomszédok listájába felvenni kívánt szoba
     */
    public void addNeighbour(Room room) {
        neighbours.add(room);
    }

    /**
     * A szobában elhelyez egy személyt amennyiben a szoba nincs tele.
     * @param person az elhelyezni kívánt személy
     */
    public void addPerson(Person person) {
        if(peopleInRoom.size()<capacity) peopleInRoom.add(person);
    }

    /**
     * Egy személy eltávolítása a szobából.
     * @param person az eltávolítani kívánt személy
     */
    public void removePerson( Person person ){
        peopleInRoom.remove(person);
    }

    /**
     * A szobában elhelyez egy tárgyat.
     * @param item az elhelyezni kívánt tárgy
     */
    public void initItem(Item item) {
        itemsInRoom.add(item);
    }

    /**
     * Egy tárgyat elhelyez a szobában és annak tartózkodási helyét is beállítja.
     * @param item a hozzáadni kívánt tárgy
     */
    @Override
    public void addItem(Item item) {
        itemsInRoom.add(item);
        item.setLocation(this, null);
    }


    /**
     * Egy tárgy felvételének kezdeményezése a szobánál.
     * Amennyiben a ragacsosság még nem érte el a határértékét,
     * a removeItem()-hez hasonlóan eltávolítja a tárgyat, majd igazzal visszatér.
     * Egyébként hamis visszatérési értékkel jelzi a személynek a sikertelen felvételt.
     * @param item a tárgy amit fel akarnak venni
     * @return {@code true} ha a tárgy felvehető {@code false} egyébként
     */
    public boolean pickUpItem(Item item) {
        if(stickiness<STICKYLIMIT) {
            itemsInRoom.remove(item);
            return true;
        }
        return false;
    }

    /**
     * Egy tárgy törlése a szobából.
     * @param item a törölni kívánt tárgy
     */
    @Override
    public void removeItem(Item item) {
        itemsInRoom.remove(item);
    }

    /**
     * Idő telésének szimulálása. Funckiói:
     * Elátkozottsági állapot módosul.
     * Továbbítja az eltelt időt a benne lévő személyeknek és tárgyaknak.
     * A továbbra is szobában tartózkodó tárgyakat összetalálkoztatja minden személlyel és
     * minden személyt kölcsönösen összetalálkoztat egymással.
     * @param time az eltelt idő
     */
    @Override
    public void timeElapsed(int time) {

        if(cursed) {
            changeCurseIn-=time;
            while(changeCurseIn<=0) {
                curseActive = !curseActive;
		changeCurseIn+=CURSESTATEINTERVAL;
            }
        }
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
    }

    /**
     * A paraméterként kapott személyt engedi be a szobába.
     * Amennyiben a szoba kapacitása kimerült nem engedi be a személyt. A visszatérési
     * értéke a beengedés sikeressége. Ha beengedi a személyt, felel az új személy és a
     * szobában tartózkodó személyek kölcsönös találkozásáért, illetve az új személy és
     * szobában levő tárgyak találkozásáért. Ha a szoba mérgezett, felel a belépő játékos
     * elkábításáért.
     * Felel a ragacsosság növeléséért.
     * @param person a belépő személy
     * @return {@code true} ha sikeresen belépett a szobába {@code false} egyébként
     */
    public boolean acceptPerson( Person person ){
        if(peopleInRoom.size()==capacity) {
            return false;
        }

        person.setLocation(this);
        peopleInRoom.add( person );
        stickiness++;

        if(gas) person.stun();

        for(int i=0; i<itemsInRoom.size(); i++) {
            itemsInRoom.get(i).meet(person);
        }

        for( int i = 0 ; i < peopleInRoom.size() ; i++ ){
            if(peopleInRoom.get( i )!=person)
                person.meet( peopleInRoom.get( i ) );
        }

        return true;
    }

    /**
     *Ha a szoba jelenleg nincs aktívan elátkozva, a
     * paraméterként kapott személy a paraméterként kapott szobába léptetésének igényét továbbítja. A kapott szoba
     * értesíti ennek sikerességéről és ő is ezzel tér vissza. Ha igazzal tér vissza, akkor
     * eltávolítja a személyt önmagából. Ha a szoba aktívan elátkozott, rögtön hamissal tér vissza.
     * @param person a személy, aki át akar lépni
     * @param roomTo a szoba, ahova át szeretne lépni
     * @return {@code true} ha sikeresen átlépett, {@code false} egyébként
     */
    public boolean movePerson( Person person, Room roomTo ){
        if(curseActive) return false;
        boolean moveSucceed=roomTo.acceptPerson(person);
        if(moveSucceed) {
            peopleInRoom.remove(person);
        }
        return moveSucceed;
    }

    /**
     * A szoba takarítása.
     * Stickiness nullázása. A legutóbb érkezett ember (ez a takarító,
     * aki a szobába jövetelkor hívta a függvényt) kivételével
     * összes szobában tartózkodó embert átteszi egy másik szobába, amennyiben teheti.
     * A szomszédok listájában elölről indul, és ameddig tudja tenni az embereket, addig oda teszi
     * (meghívja az adott emberre az enterRoom(r3) metódust az adott r3 szomszédot átadva),
     * ha pedig nem tudja, akkor a következő szomszéddal próbálkozik.
     * Ha az összes szomszédon végig ment és még mindig maradt ember a szobában, akkor ők ott maradhatnak.
     */
    public void clean() {
        stickiness=0;
        for(Room neighbour: neighbours) {
            List<Person> peopleInRoomCopy=new ArrayList<Person>(peopleInRoom);
            for(int i=0; i<peopleInRoomCopy.size()-1; i++) {
                peopleInRoomCopy.get(i).enterRoom(neighbour);
            }
        }
    }

    /**
     * Ezzel a függvénnyel kerül indítványozásra a meghívott szobánál, hogy
     * kettéosztódjon. Amennyiben tartózkodik benne személy NULL-t ad vissza. Különben
     * pedig létrehoz egy új szobát, aminek neighbours-ei a saját neighbours-ei fele és saját
     * maga, az itemsInRoom a saját itemsInRoom-jainak szintén fele lesz. Az átadott
     * szomszédokat és tárgyakat saját magából eltávolítja, az új szobát beállítja saját
     * szomszédjának is. Az átadott tárgyak location-je frissül. A gas és
     * cursed értékeiből, ha mindkettő logikai igaz volt, akkor az erediben a gas hamis lesz,
     * és az újban lesz a gas igaz. Ha a két értékből nem volt mindkettő logikai igaz, akkor az új
     * szoba mindkét értéke hamis lesz. Az új szoba capacity-je és stickiness-e a régiével egyezik meg.
     * @return az új {@code Room} objektum, ha sikeres volt a szétválás, {@code null} egyébként
     */
    public Room split(){

        if(!peopleInRoom.isEmpty()) {
            return null;
        }

        boolean newGas=false;
        boolean newCursed=false;
        if(gas&&cursed) {
            gas=false;
            newGas=true;
        }
        Room newRoom = new Room(capacity, newGas, newCursed, stickiness);

        int desiredItemSize = itemsInRoom.size() / 2;
        while( itemsInRoom.size() > desiredItemSize ){
            newRoom.addItem( itemsInRoom.get(0) );
            itemsInRoom.remove( 0 );
        }

        int desiredNeighbourSize = neighbours.size() / 2;
        while( neighbours.size() > desiredNeighbourSize ){
            Room neighbour=neighbours.get(0);
            if(neighbour.neighbours.contains(this)) {
                neighbour.neighbours.remove(this);
                neighbour.neighbours.add(newRoom);
            }
            newRoom.neighbours.add( neighbour );
            neighbours.remove(0);
        }
        neighbours.add(newRoom);
        newRoom.neighbours.add(this);

        return newRoom;
    }

    /**
     * Két szoba egyesítését kezdeményező függvény.
     * Ha nincs benne egy személy sem, akkor az ő részéről rendben van az egyesülés,
     * továbbítja a kérést a másik félnek a merge(this) metódushívással.
     * Ha a másik szoba is beleegyezett, létrehozta az új szobát.
     * Az aktuális szoba átadja az újnak a tárgyait és szomszédait.
     * @param room2 az a szoba, aminek továbbítani kell az összeolvadási kérést.
     * @return {@code Room} ha sikeres az összeolvadás, {@code null} egyébként
     */
    public Room requestMerge( Room room2 ){
        if(!peopleInRoom.isEmpty()) {
            return null;
        }
        Room newRoom = room2.merge(this);
        if( newRoom != null ) {

            for(Item item: itemsInRoom) {
                newRoom.addItem(item);
            }
            while(!neighbours.isEmpty()) {
                Room neighbour=neighbours.get(0);
                if(neighbour != room2) {
                    if(neighbour.neighbours.contains(this)) {
                        neighbour.neighbours.remove(this);
                        neighbour.neighbours.add(newRoom);
                    }
                    newRoom.neighbours.add(neighbour);
                }
                neighbours.remove(0);
            }
        }

        return newRoom;
    }

    /**
     * Két szoba egyesítését elvégző belső függvény.
     * Ha nincs benne egy személy sem, létrehoz egy új szobát.
     * Az új szoba állapota a két szoba állapotai alapján lesznek beállítva.
     * Az új szoba kapacitása és ragacsossága a két szoba értékeiből a nagyobbik lesz.
     * Az új szoba elátkozottság és mérgező gáz állapota a két szoba értékeinek logikai VAGY kapcsolatából keletkezik.
     * Az új szoba szomszédjait beállítja a sajátjai alapján és az összes benne lévő tárgyat áthelyezi az új szobába.
     * @param room1 az a szoba, ami össze akar olvadni
     * @return {@code Room} ha sikeres az összeolvadás, {@code null} egyébként
     */
    private Room merge( Room room1 ){

        if(!peopleInRoom.isEmpty()) {
            return null;
        }

        int newCapacity= Math.max(room1.capacity, capacity);
        boolean newGas=room1.gas||gas;
        boolean newCurse=room1.cursed||cursed;
        int newStickiness=Math.max(room1.stickiness, stickiness);
        Room newRoom = new Room(newCapacity, newGas, newCurse, newStickiness);

        for(Item item: itemsInRoom) {
            newRoom.addItem(item);
        }

        while(!neighbours.isEmpty()) {
            Room neighbour=neighbours.get(0);
            if(neighbour != room1) {
                if(neighbour.neighbours.contains(this)) {
                    neighbour.neighbours.remove(this);
                    neighbour.neighbours.add(newRoom);
                }
                newRoom.neighbours.add(neighbour);
            }
            neighbours.remove(0);
        }

        return newRoom;
    }

    /**
     * A szoba gázosítása vagy annak megszüntetése. Amennyiben a kapott paraméter
     * igaz,felel a szobában tartózkodó személyek elkábításáért stun() hívásával.
     * Felel a gázosság állapotának beállításáért.
     * @param gas a gas attribútum új értéke
     */
    public void setGas(boolean gas){
        this.gas=gas;
        if(gas) {
            for(Person person: peopleInRoom) {
                person.stun();
            }
        }
    }

    /*
     * gázosság lekérdezése
     */
    public boolean isGas() {
        return gas;
    }

    /*
     * elátkozottság lekérdezése
     */
    public boolean isCursed() {
        return cursed;
    }

    /*
     * elátkozottság beállítása
     */
    public void setCursed(boolean cursed) {
        this.cursed = cursed;
    }

    /*
     * kapacitás lekérdezése
     */
    public int getCapacity() {
        return capacity;
    }

    /*
     * kapacitás beállítása
     */
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    /*
     * aktív elátkozottság lekérdezése
     */
    public boolean isCurseActive() {
        return curseActive;
    }

    /*
     * aktív elátkozottság beállítása
     */
    public void setCurseActive(boolean curseActive) {
        this.curseActive = curseActive;
    }

    /*
     * ragacsosság lekérdezése
     */
    public int getStickiness() {
        return stickiness;
    }

    /*
     * ragacsosság beállítása
     */
    public void setStickiness(int stickiness) {
        this.stickiness = stickiness;
    }

    /*
     * szobában tartózkodó személyek lekérdezése
     */
    public List<Person> getPeopleInRoom() {
        return peopleInRoom;
    }

    /*
     * szobában levő tárgyak lekérdezése
     */
    public List<Item> getItemsInRoom() {
        return itemsInRoom;
    }

    /*
     * szomszéd szobák lekérdezése
     */
    public List<Room> getNeighbours() {
        return neighbours;
    }
}
