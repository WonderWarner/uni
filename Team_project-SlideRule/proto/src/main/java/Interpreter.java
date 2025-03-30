import model.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * A Interpreter osztály, amely végrehajtja a parancsokat és kezeli az objektumokat.
 */
public class Interpreter {

    /**
     * A parancsok interfésze, amely egy execute metódust tartalmaz.
     */
    interface Command {
        void execute(String[] args);
    }

    // A parancsok és az objektumok tárolására szolgáló HashMap-ek
    private final HashMap<String, Command> commands = new HashMap<>();
    private final HashMap<String, Object> objects = new HashMap<>();

    // Konstans a hibás parancs üzenetéhez
    private static final String INCORRECT_COMMAND = "Incorrect command";

    /**
     * Konstruktor az Interpreter osztály inicializálásához.
     * Inicializálja a parancsokat és elindítja a végrehajtást.
     */
    Interpreter(){

        // Parancs új objektum létrehozásához (pl. szoba, Hallgató, tanár stb.)
        commands.put("create", args -> {
            // Ellenőrizze, hogy a megfelelő számú argumentumot megadták-e
            if (args.length != 3) {
                // Ha nem megfelelő a szám, kiírja a hibát és kilép
                System.out.println(INCORRECT_COMMAND);
                return;
            }
            Object newObject = null;
            // A második argumentum alapján válassza ki, milyen típusú objektumot hozzon létre
            switch (args[1]) {
                case "room":
                    // Szoba létrehozása
                    newObject = new Room(3, false, false, 0);
                    break;
                case "student":
                    // Hallgató létrehozása
                    newObject = new Student(0, null);
                    break;
                case "teacher":
                    // Tanár létrehozása
                    newObject = new Teacher(0, null);
                    break;
                case "cleaner":
                    // Takarító létrehozása
                    newObject = new Cleaner(0, null);
                    break;
                case "rag":
                    // Rongy létrehozása
                    newObject = new Rag(null, null, false, 5);
                    break;
                case "beerglass":
                    // Söröspohár létrehozása
                    newObject = new BeerGlass(null, null, false, 5);
                    break;
                case "mask":
                    // Maszk létrehozása
                    newObject = new Mask(null, null, false, 6);
                    break;
                case "falsemask":
                    // Hamis maszk létrehozása
                    newObject = new FalseMask(null, null, false, 6);
                    break;
                case "sliderule":
                    // Logarléc létrehozása
                    newObject = new SlideRule(null, null);
                    break;
                case "falsesliderule":
                    // Hamis logarléc létrehozása
                    newObject = new FalseSlideRule(null, null);
                    break;
                case "tvsz":
                    // TVSZ létrehozása
                    newObject = new TVSZ(null, null);
                    break;
                case "falsetvsz":
                    // Hamis TVSZ létrehozása
                    newObject = new FalseTVSZ(null, null);
                    break;
                case "airfresher":
                    // Légtisztító létrehozása
                    newObject = new AirFresher(null, null);
                    break;
                case "transistor":
                    // Tranzisztor létrehozása
                    newObject = new Transistor(null, null);
                    break;
                case "camembert":
                    // Camembert sajt létrehozása
                    newObject = new Camembert(null, null);
                    break;
                default:
                    // Ha az objektum típusa nem megfelelő, kiírja a hibát és kilép
                    System.out.println(INCORRECT_COMMAND);
                    break;
            }
            // Ellenőrizze, hogy az objektum neve még nem létezik-e
            if (commands.containsKey(args[2])) {
                // Ha már létezik az objektum név, kiírja a hibát és kilép
                System.out.println("Error: Name" + args[ 2 ] + "already exists");
                return;
            }
            // Hozzáadja az új objektumot a tárolóhoz a megadott névvel
            objects.put(args[2], newObject);
        });


        // Parancs egy személy hozzáadásához egy szobához
        commands.put("addperson", args -> {
            // Ellenőrzi, hogy a megfelelő számú argumentumot szolgáltatták-e
            if (args.length != 3) {
                System.out.println(INCORRECT_COMMAND);
                return;
            }
            // Lekéri a személy és a szoba objektumokat
            Object personObj = objects.get(args[1]);
            Object roomObj = objects.get(args[2]);
            // Ellenőrzi, hogy az objektumok megfelelő típusúak-e
            if (!(personObj instanceof Person person) || !(roomObj instanceof Room room)) {
                System.out.println(INCORRECT_COMMAND);
                return;
            }
            // Hozzáadja a személyt a szobához és beállítja az ő tartózkodási helyét
            room.addPerson(person);
            person.setLocation(room);
        });


        // Parancs új tárgy hozzáadásához egy helyhez (szobához vagy személyhez)
        commands.put("add", args -> {
            // Ellenőrizze, hogy a megfelelő számú argumentumot megadták-e
            if (args.length != 3) {
                // Ha nem megfelelő a szám, kiírja a hibát és kilép
                System.out.println(INCORRECT_COMMAND);
                return;
            }
            // Az első argumentum alapján meghatározza az új tárgyat
            Object itemObj = objects.get(args[1]);
            // A második argumentum alapján meghatározza a célpontot (szobát vagy személyt)
            Object targetObj = objects.get(args[2]);
            // Ellenőrizze, hogy a tárgy megfelelő típusú
            if (!(itemObj instanceof Item item)) {
                // Ha nem, kiírja a hibát és kilép
                System.out.println(INCORRECT_COMMAND);
                return;
            }
            if (targetObj instanceof Room room) {
                // Ha a célpont egy szoba
                // Inicializálja a tárgyat a szobában
                room.initItem(item);
                // Beállítja a tárgy helyét a szobában, null személyre
                item.setLocation(room, null);
            } else if (targetObj instanceof Person person) {
                // Ha a célpont egy személy
                // Inicializálja a tárgyat a személyhez
                person.initItem(item);
                // Beállítja a tárgy helyét a személyhez
                item.setLocation(person.getLocation(), person);
            } else {
                // Ha a célpont nem megfelelő, kiírja a hibát és kilép
                System.out.println(INCORRECT_COMMAND);
                return;
            }
        });


        // Parancs a tárgy eltávolításához egy szobából vagy egy személytől
        commands.put("remove", args -> {
            // Ellenőrizze, hogy a megfelelő számú argumentumot megadták-e
            if (args.length != 3) {
                System.out.println(INCORRECT_COMMAND);
                return;
            }
            // Kérje le a tárgyat és a célt
            Object itemObj = objects.get(args[1]);
            Object targetObj = objects.get(args[2]);
            // Ellenőrizze, hogy a tárgy objektum megfelelő típusú-e
            if (!(itemObj instanceof Item item)) {
                System.out.println(INCORRECT_COMMAND);
                return;
            }
            // Az elem eltávolításának megvalósítása a céltól (szoba vagy személy)
            if (targetObj instanceof Room room) {
                room.getItemsInRoom().remove(item);
                item.setLocation(null, null);
            } else if (targetObj instanceof Person person) {
                person.getItemsInHand().remove(item);
                item.setLocation(null, null);
            } else {
                System.out.println(INCORRECT_COMMAND);
                return;
            }
        });

        // Parancs a szoba tulajdonságainak beállításához
        commands.put("setroom", args -> {
            // Ellenőrizze, hogy a megfelelő számú argumentumot megadták-e
            if (args.length != 4) {
                System.out.println(INCORRECT_COMMAND);
                return;
            }
            // Kérje le a szoba objektumot
            Object roomObj = objects.get(args[1]);
            // Ellenőrizze, hogy a szoba objektum megfelelő típusú-e
            if (!(roomObj instanceof Room room)) {
                System.out.println(INCORRECT_COMMAND);
                return;
            }
            // Válassza ki a szobára vonatkozó különböző tulajdonságokat a megadott argumentumok alapján
            switch (args[2]) {
                // Esetek a különböző tulajdonságokhoz
                case "gas":
                    // Állítsa be a szoba gáz tulajdonságát
                    if (args[3].equals("true")) {
                        room.setGas(true);
                    } else if (args[3].equals("false")) {
                        room.setGas(false);
                    } else {
                        System.out.println(INCORRECT_COMMAND);
                        return;
                    }
                    break;
                case "cursed":
                    // Állítsa be a szoba átkozott tulajdonságát
                    if (args[3].equals("true")) {
                        room.setCursed(true);
                    } else if (args[3].equals("false")) {
                        room.setCursed(false);
                    } else {
                        System.out.println(INCORRECT_COMMAND);
                        return;
                    }
                    break;
                case "curseactive":
                    // Állítsa be a szoba átok aktív tulajdonságát
                    if (args[3].equals("true")) {
                        room.setCurseActive(true);
                    } else if (args[3].equals("false")) {
                        room.setCurseActive(false);
                    } else {
                        System.out.println(INCORRECT_COMMAND);
                        return;
                    }
                    break;
                case "capacity":
                    // Állítsa be a szoba befogadóképesség tulajdonságát
                    int newCapacity;
                    try {
                        newCapacity = Integer.parseInt(args[3]);
                    } catch (NumberFormatException exception) {
                        System.out.println(INCORRECT_COMMAND);
                        return;
                    }
                    room.setCapacity(newCapacity);
                    break;
                case "stickiness":
                    // Állítsa be a szoba ragacsosság tulajdonságát
                    int newStickiness;
                    try {
                        newStickiness = Integer.parseInt(args[3]);
                    } catch (NumberFormatException exception) {
                        System.out.println(INCORRECT_COMMAND);
                        return;
                    }
                    room.setStickiness(newStickiness);
                    break;
                default:
                    System.out.println(INCORRECT_COMMAND);
                    return;
            }
        });

        // Parancs a személyhez tartozó stun beállításához
        commands.put("setstun", args -> {
            // Ellenőrizze, hogy a megfelelő számú argumentumot megadták-e
            if (args.length != 3) {
                System.out.println(INCORRECT_COMMAND);
                return;
            }
            // Kérje le a személy objektumot
            Object personObj = objects.get(args[1]);
            // Ellenőrizze, hogy a személy objektum megfelelő típusú-e
            if (!(personObj instanceof Person person)) {
                System.out.println(INCORRECT_COMMAND);
                return;
            }
            // A személyhez tartozó stun beállításának megvalósítása
            int newStun;
            // Ellenőrizze, hogy a harmadik argumentum szám-e
            try {
                newStun = Integer.parseInt(args[2]);
            } catch (NumberFormatException exception) {
                System.out.println(INCORRECT_COMMAND);
                return;
            }
            person.setStunRemaining(newStun);
        });

        // Parancs a szomszédos szobák hozzáadásához
        commands.put("neighbour", args -> {
            // Ellenőrizze, hogy a megfelelő számú argumentumot megadták-e
            if (args.length != 3) {
                System.out.println(INCORRECT_COMMAND);
                return;
            }
            // Kérje le a két szoba objektumot
            Object room1Obj = objects.get(args[1]);
            Object room2Obj = objects.get(args[2]);
            // Ellenőrizze, hogy a szoba objektumok megfelelő típusúak-e
            if (!(room1Obj instanceof Room room1) || !(room2Obj instanceof Room room2)) {
                System.out.println(INCORRECT_COMMAND);
                return;
            }
            // Ellenőrizze, hogy a szobák különbözőek
            if( room1 == room2 ){
                System.out.println( "Error: Room objects must differ." );
                return;
            }
            // Ellenőrizze, hogy a szobák nem szomszédosak
            if( room1.getNeighbours().contains( room2) ){
                System.out.println( "Error: Rooms " + args[1] +" and " + args[ 2 ] + "are already neighbours." );
                return;
            }
            // Adjuk hozzá az első szobát a második szomszédaihoz
            room1.addNeighbour(room2);
        });

        // Parancs a párosított tranzisztorok beállításához
        commands.put("setpair", args -> {
            // Ellenőrizze, hogy a megfelelő számú argumentumot megadták-e
            if (args.length != 3) {
                System.out.println(INCORRECT_COMMAND);
                return;
            }
            // Kérje le a két tranzisztor objektumot
            Object transistor1Obj = objects.get(args[1]);
            Object transistor2Obj = objects.get(args[2]);
            // Ellenőrizze, hogy a tranzisztor objektumok megfelelő típusúak-e
            if (!(transistor1Obj instanceof Transistor transistor1) || !(transistor2Obj instanceof Transistor transistor2)) {
                System.out.println(INCORRECT_COMMAND);
                return;
            }
            // Ellenőrizze, hogy a két tranzisztor különböző-e
            if (transistor1 == transistor2) {
                System.out.println("Error: A tranzisztor objektumoknak különbözőnek kell lenniük.");
                return;
            }
            // Ellenőrizze, hogy a két tranzisztor még nincs párosítva
            if (transistor1.getPair() != null || transistor2.getPair() != null) {
                System.out.println("Error: A tranzisztor már párosítva van.");
                return;
            }
            // Állítsa be a tranzisztorok párosítását egymással
            transistor1.setPair(transistor2);
            transistor2.setPair(transistor1);
        });

        // Parancs a Maszk tartósság és a TVSZ maradék használat tulajdonságok beállításához
        commands.put("setremaining", args -> {
            // Ellenőrizze, hogy a megfelelő számú argumentumot megadták-e
            if (args.length != 3) {
                System.out.println(INCORRECT_COMMAND);
                return;
            }
            // Kérje le az új értéket
            int newValue;
            try {
                newValue = Integer.parseInt(args[2]);
            } catch (NumberFormatException exception) {
                System.out.println(INCORRECT_COMMAND);
                return;
            }
            // Kérje le az objektumot
            Object targetObj = objects.get(args[1]);
            // Ellenőrizze, hogy a megfelelő típusú objektumot kapta-e
            if (targetObj instanceof Mask mask) {
                // Állítsa be a maszk tartósságát
                mask.setDuration(newValue);
            } else if (targetObj instanceof TVSZ tvsz) {
                // Állítsa be a TVSZ maradék használatát
                tvsz.setUsesRemaining(newValue);
            } else {
                System.out.println(INCORRECT_COMMAND);
                return;
            }
        });

        // Parancs adott idejű tárgy tulajdonságainak beállításához
        commands.put("setinterval", args -> {
            // Ellenőrizze, hogy a megfelelő számú argumentumot megadták-e
            if (args.length != 4) {
                // Ha nem megfelelő a szám, kiírja a hibát és kilép
                System.out.println(INCORRECT_COMMAND);
                return;
            }
            // Az adott idejű tárgy objektum lekérése
            Object intervalItemObj = objects.get(args[2]);
            // Ellenőrizze, hogy az objektum valóban adott idejű tárgy-e
            if (!(intervalItemObj instanceof IntervalItem intervalItem)) {
                // Ha nem, kiírja a hibát és kilép
                System.out.println(INCORRECT_COMMAND);
                return;
            }
            // Az első argumentum alapján meghatározza a tulajdonságot, amit be kell állítani
            switch (args[1]) {
                case "activated":
                    // Aktiválás beállítása az igaz vagy hamis érték alapján
                    intervalItem.setActivated(Boolean.parseBoolean(args[3]));
                    break;
                case "timeremaining":
                    // Hátralévő idő beállítása
                    int newTimeRemaining;
                    try {
                        newTimeRemaining = Integer.parseInt(args[3]);
                    } catch (NumberFormatException exception) {
                        // Ha a számértéket nem lehet konvertálni, kiírja a hibát és kilép
                        System.out.println(INCORRECT_COMMAND);
                        return;
                    }
                    intervalItem.setTimeRemaining(newTimeRemaining);
                    break;
                default:
                    // Ha az argumentum nem megfelelő, kiírja a hibát és kilép
                    System.out.println(INCORRECT_COMMAND);
                    return;
            }
        });

        // Parancs egy személy véletlenszám-generátorának seed-jének beállításához
        commands.put("setseed", args -> {
            // Ellenőrizze, hogy a megfelelő számú argumentumot megadták-e
            if (args.length != 3) {
                // Ha nem megfelelő a szám, kiírja a hibát és kilép
                System.out.println(INCORRECT_COMMAND);
                return;
            }
            // A személy objektum lekérése
            Object personObj = objects.get(args[1]);
            // Ellenőrizze, hogy az objektum valóban egy személy-e
            if (!(personObj instanceof Person person)) {
                // Ha nem, kiírja a hibát és kilép
                System.out.println(INCORRECT_COMMAND);
                return;
            }
            // Az új seed beállítása
            int newSeed;
            try {
                newSeed = Integer.parseInt(args[2]);
            } catch (NumberFormatException exception) {
                // Ha a számértéket nem lehet konvertálni, kiírja a hibát és kilép
                System.out.println(INCORRECT_COMMAND);
                return;
            }
            person.setSeed(newSeed);
        });


        // Parancs fájlból való betöltéshez
        commands.put("load", args -> {
            // Ellenőrizze, hogy a megfelelő számú argumentumot megadták-e
            if (args.length != 2) {
                // Ha nem megfelelő a szám, kiírja a hibát és kilép
                System.out.println(INCORRECT_COMMAND);
                return;
            }
            // Fájl beolvasása
            Scanner file = null;
            try {
                file = new Scanner(new File(args[1]));
            } catch (FileNotFoundException e) {
                // Ha a fájl nem található, kiírja a hibát és kilép
                System.out.println(INCORRECT_COMMAND);
                return;
            }
            // Soronként olvassa be a fájlt és hajtsa végre a parancsokat
            while (file.hasNextLine()) {
                String line = file.nextLine();
                execute(line);
            }
        });

        // Parancs egy tárgy eldobásához egy személytől
        commands.put("drop", args -> {
            // Ellenőrizze, hogy a megfelelő számú argumentumot megadták-e
            if (args.length != 3) {
                // Ha nem megfelelő a szám, kiírja a hibát és kilép
                System.out.println(INCORRECT_COMMAND);
                return;
            }
            // A tárgy objektum lekérése
            Object itemObj = objects.get(args[1]);
            // A személy objektum lekérése
            Object personObj = objects.get(args[2]);
            // Ellenőrizze, hogy mindkét objektum típusa megfelelelő
            if (!(itemObj instanceof Item item) || !(personObj instanceof Person person)) {
                // Ha nem, kiírja a hibát és kilép
                System.out.println(INCORRECT_COMMAND);
                return;
            }
            // Ellenőrzi, hogy a tárgy a személy birtokában van-e
            if (!person.getItemsInHand().contains(item)) {
                // Ha nem, kiírja a hibát és kilép
                System.out.println("Error: Item " + args[ 1 ] +" is not held by " + args[ 2 ]);
                return;
            }
            // Tárgy eldobása a személytől
            person.dropItem(item);
        });

        // Parancs egy tárgy felvételéhez egy személy által
        commands.put("pickup", args -> {
            // Ellenőrizze, hogy a megfelelő számú argumentumot megadták-e
            if (args.length != 3) {
                // Ha nem megfelelő a szám, kiírja a hibát és kilép
                System.out.println(INCORRECT_COMMAND);
                return;
            }
            // A tárgy objektum lekérése
            Object itemObj = objects.get(args[1]);
            // A személy objektum lekérése
            Object personObj = objects.get(args[2]);
            // Ellenőrizze, hogy mindkét objektum típusa megfelelő
            if (!(itemObj instanceof Item item) || !(personObj instanceof Person person)) {
                // Ha nem, kiírja a hibát és kilép
                System.out.println(INCORRECT_COMMAND);
                return;
            }
            // Ellenőrzi, hogy a tárgy és a személy ugyanabban a szobában van-e
            if (!person.getLocation().getItemsInRoom().contains(item)) {
                // Ha nem, kiírja a hibát és kilép
                System.out.println("Error: Item "+ args[1] + " and Person " + args[2] +" are not in the same room");
                return;
            }
            // Tárgy felvétele a személy által
            person.addItem(item);
        });


        // Parancs egy személy általi belépéshez egy szobába
        commands.put("enter", args -> {
            // Ellenőrizze, hogy a megfelelő számú argumentumot megadták-e
            if (args.length != 3) {
                // Ha nem megfelelő a szám, kiírja a hibát és kilép
                System.out.println(INCORRECT_COMMAND);
                return;
            }
            // A szoba objektum lekérése
            Object roomObj = objects.get(args[1]);
            // A személy objektum lekérése
            Object personObj = objects.get(args[2]);
            // Ellenőrizze, hogy mindkét objektum típusa megfelelő
            if (!(roomObj instanceof Room room) || !(personObj instanceof Person person)) {
                // Ha nem, kiírja a hibát és kilép
                System.out.println(INCORRECT_COMMAND);
                return;
            }
            // Ellenőrzi, hogy a személy jelenlegi helye szomszédos-e a megadott szobával
            if (!person.getLocation().getNeighbours().contains(room)) {
                // Ha nem, kiírja a hibát és kilép
                System.out.println("Error: Rooms are not neighbours.");
                return;
            }
            // Személy belépése a szobába
            person.enterRoom(room);
        });

        // Parancs két szoba egyesítéséhez
        commands.put("merge", args -> {
            // Ellenőrizze, hogy a megfelelő számú argumentumot megadták-e
            if (args.length != 4) {
                // Ha nem megfelelő a szám, kiírja a hibát és kilép
                System.out.println(INCORRECT_COMMAND);
                return;
            }
            // Az első szoba objektum lekérése
            Object room1Obj = objects.get(args[1]);
            // A második szoba objektum lekérése
            Object room2Obj = objects.get(args[2]);
            // Ellenőrizze, hogy mindkét objektum típusa megfelelő
            if (!(room1Obj instanceof Room room1) || !(room2Obj instanceof Room room2)) {
                // Ha nem, kiírja a hibát és kilép
                System.out.println(INCORRECT_COMMAND);
                return;
            }
            // Ellenőrizze, hogy a két szoba különbözik-e
            if (room1 == room2) {
                // Ha nem, kiírja a hibát és kilép
                System.out.println("Error: Room objects must differ.");
                return;
            }
            // Ellenőrizze, hogy a két szoba egymás szomszédjai-e
            if (!room1.getNeighbours().contains(room2) || !room2.getNeighbours().contains(room1)) {
                // Ha nem, kiírja a hibát és kilép
                System.out.println("Error: Rooms " + args[1] + " and " + args[2] + " are not neighbours.");
                return;
            }
            // Ellenőrizze, hogy a megadott név már létezik-e
            if (objects.containsKey(args[3])) {
                // Ha igen, kiírja a hibát és kilép
                System.out.println("Error: Name "+ args[3] + " already exists.");
                return;
            }
            // Két szoba egyesítése
            Room newRoom = room1.requestMerge(room2);
            // Ha a művelet sikeres volt, adjon hozzá egy új szoba objektumot az objektumok táblához
            if (newRoom != null) {
                objects.put(args[3], newRoom);
            }
        });

        // Parancs egy szoba felosztásához
        commands.put("split", args -> {
            // Ellenőrizze, hogy a megfelelő számú argumentumot megadták-e
            if (args.length != 3) {
                // Ha nem megfelelő a szám, kiírja a hibát és kilép
                System.out.println(INCORRECT_COMMAND);
                return;
            }
            // A szoba objektum lekérése
            Object roomObj = objects.get(args[1]);
            // Ellenőrizze, hogy az objektum valóban egy szoba-e
            if (!(roomObj instanceof Room room)) {
                // Ha nem, kiírja a hibát és kilép
                System.out.println(INCORRECT_COMMAND);
                return;
            }
            // Ellenőrizze, hogy a megadott név már létezik-e
            if (objects.containsKey(args[2])) {
                // Ha igen, kiírja a hibát és kilép
                System.out.println("Error: Name "+ args[2] + " already exists.");
                return;
            }
            // Szoba felosztása
            Room newRoom = room.split();
            // Ha a művelet sikeres volt, adjon hozzá egy új szoba objektumot az objektumok táblához
            if (newRoom != null) {
                objects.put(args[2], newRoom);
            }
        });


        // Parancs egy tárgy aktiválásához egy Hallgató által
        commands.put("activate", args -> {
            // Ellenőrizze, hogy a megfelelő számú argumentumot megadták-e
            if (args.length != 3) {
                // Ha nem megfelelő a szám, kiírja a hibát és kilép
                System.out.println(INCORRECT_COMMAND);
                return;
            }
            // A tárgy objektum lekérése
            Object itemObj = objects.get(args[1]);
            // A Hallgató objektum lekérése
            Object studentObj = objects.get(args[2]);
            // Ellenőrizze, hogy mindkét objektum típusa megfelelő
            if (!(itemObj instanceof Item item) || !(studentObj instanceof Student student)) {
                // Ha nem, kiírja a hibát és kilép
                System.out.println(INCORRECT_COMMAND);
                return;
            }
            // Ellenőrizze, hogy a tárgy a Hallgató kezében van-e
            if (!student.getItemsInHand().contains(item)) {
                // Ha nem, kiírja a hibát és kilép
                System.out.println( "Error: Item " + args[ 1 ] +" is not held by " + args[ 2 ] );
                return;
            }
            // Tárgy aktiválása a Hallgató által
            student.activateItem(item);
        });


        // Parancs az idő múlásának szimulálásához
        commands.put("elapsetime", args -> {
            // Ellenőrizze, hogy a megfelelő számú argumentumot megadták-e
            if (args.length != 2) {
                System.out.println(INCORRECT_COMMAND);
                return;
            }
            // Kérje le az eltelt idő értékét
            int time;
            try {
                time = Integer.parseInt(args[1]);
            } catch (NumberFormatException exception) {
                System.out.println(INCORRECT_COMMAND);
                return;
            }
            // Menjen végig az összes tárolt objektumon és szimulálja az eltelt időt a szobákban
            for (Object iter : objects.values()) {
                if (iter instanceof Room room) {
                    room.timeElapsed(time);
                }
            }
        });


        // Parancs a játék állapotának lekérdezéséhez
        commands.put("status", args -> {
            // Ellenőrizze, hogy nem adtak meg argumentumot
            if (args.length == 1) {
                // Ellenőrizze, hogy van-e Hallgató, aki nyert a játékban
                boolean win = false;
                // Ellenőrizze, hogy van-e játék folyamatban
                boolean inProgress = false;
                for (Object object : objects.values()) {
                    if (object instanceof Room room) {
                        for (Person person : room.getPeopleInRoom()) {
                            if (person instanceof Student student) {
                                inProgress = true;
                                for (Item item : student.getItemsInHand()) {
                                    // Ellenőrizze, hogy a Hallgató rendelkezik-e a győzelmet jelentő tárggyal (SlideRule)
                                    if (item instanceof SlideRule && !(item instanceof FalseSlideRule)) {
                                        win = true;
                                    }
                                }
                            }
                        }
                    }
                }
                // Állapotsor kiírása a játék állapotának függvényében
                if (win) {
                    System.out.println("Win");
                }
                else if (inProgress) {
                    System.out.println("Game in progress");
                }
                else {
                    System.out.println("Game over");
                }
                return;
            }
            // Ellenőrizze, hogy a megfelelő számú argumentumot megadták-e
            if (args.length != 2) {
                System.out.println(INCORRECT_COMMAND);
                return;
            }
            // Kérje le az objektumot
            Object object = objects.get(args[1]);
            // Ellenőrizze, hogy milyen típusú objektumról van szó, és írja ki az állapotát
            if (object instanceof Room room) {
                // Szobák állapotának kiírása
                System.out.println(getName(room));
                printList("neighbours", room.getNeighbours());
                printList("peopleInRoom", room.getPeopleInRoom());
                printList("itemsInRoom", room.getItemsInRoom());
                System.out.println("gas: " + room.isGas());
                System.out.println("cursed: " + room.isCursed());
                System.out.println("capacity: " + room.getCapacity());
                System.out.println("curseActive: " + room.isCurseActive());
                System.out.println("stickiness: " + room.getStickiness());
            } else if (object instanceof Person person) {
                // Személy állapotának kiírása
                System.out.println(getName(person));
                System.out.println("location: " + getName(person.getLocation()));
                printList("itemsInHand", person.getItemsInHand());
                System.out.println("stunRemaining: " + person.getStunRemaining());
            } else if (object instanceof Transistor transistor) {
                // Tranzisztor állapotának kiírása
                System.out.println(getName(transistor));
                System.out.println("location: " + getName(transistor.getLocation()));
                System.out.println("holder: " + getName(transistor.getHolder()));
                System.out.println("pair: " + getName(transistor.getPair()));
            } else if (object instanceof TVSZ tvsz) {
                // TVSZ állapotának kiírása
                System.out.println(getName(tvsz));
                System.out.println("location: " + getName(tvsz.getLocation()));
                System.out.println("holder: " + getName(tvsz.getHolder()));
                System.out.println("usesRemaining: " + tvsz.getUsesRemaining());
            } else if (object instanceof Mask mask) {
                // Maszk állapotának kiírása
                System.out.println(getName(mask));
                System.out.println("location: " + getName(mask.getLocation()));
                System.out.println("holder: " + getName(mask.getHolder()));
                System.out.println("timeRemaining: " + mask.getTimeRemaining());
                System.out.println("duration: " + mask.getDuration());
            } else if (object instanceof IntervalItem intervalItem) {
                // Adott idejű tárgy állapotának kiírása
                System.out.println(getName(intervalItem));
                System.out.println("location: " + getName(intervalItem.getLocation()));
                System.out.println("holder: " + getName(intervalItem.getHolder()));
                System.out.println("timeRemaining: " + intervalItem.getTimeRemaining());
            } else if (object instanceof Item item) {
                // Tárgy állapotának kiírása
                System.out.println(getName(item));
                System.out.println("location: " + getName(item.getLocation()));
                System.out.println("holder: " + getName(item.getHolder()));
            } else {
                System.out.println(INCORRECT_COMMAND);
                return;
            }
        });

        // Parancs a programból való kilépéshez
        commands.put("exit", args -> {
            // Kilépés a programból
            System.exit(1);
        });
    }

    /**
     * Visszaadja az objektum nevét.
     * @param object Az objektum, amelynek a nevét lekéri.
     * @return Az objektum neve, vagy null, ha nem található.
     */
    private String getName( Object object ){
        if( object == null ){
            return "NULL";
        }
        for( Map.Entry<String,Object> iter : objects.entrySet() ){
            if( object.equals( iter.getValue() ) ){
                return iter.getKey();
            }
        }
        return null;
    }

    /**
     * Segédmetódus a lista elemek kiírásához.
     * @param name A lista neve.
     * @param list A kiírandó lista.
     */
    private void printList(String name, Collection<?> list){
        System.out.print( name + ": ");
        for( Object i : list ){
            System.out.print(getName(i) + ", ");
        }
        System.out.println();
    }

    /**
     * Metódus a parancs végrehajtásához egy sor alapján.
     * @param line A végrehajtandó parancs sor.
     */
    public void execute(String line ){
        if(line.isBlank()) {
            return;
        }
        String[] args = line.split(" ");
        Command command = commands.get( args[ 0 ]);
        if( command == null ){
            System.out.println(INCORRECT_COMMAND);
            return;
        }
        command.execute(args);
    }

    /**
     * A program belépési pontja.
     * Inicializálja az Interpreter-t, és elindítja a parancsok végrehajtását a felhasználói bemenetről.
     * @param args A parancssori argumentumok.
     */
    public static void main( String[] args ){
        Interpreter interpreter = new Interpreter();
        Scanner in = new Scanner(System.in);
        while (in.hasNextLine()){
            String line = in.nextLine();
            interpreter.execute(line);
        }
        in.close();
    }

}
