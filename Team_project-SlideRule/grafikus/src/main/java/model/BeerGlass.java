package model;

/**
 * Az BeerGlass osztály a szent söröspoharak működését és viselkedését modellezi.
 * A söröspohár védelmet nyújt egy  adott ideig az oktatókkal szemben, ha aktiválva van.
 * Használatkor a hallgató elejt egy véletlen tárgyat a kezéből. Ha a hatása elmúlik, akkor eltűnik.
 * Az osztály az IntervalItem absztakt osztályból származik.
 */
public class BeerGlass extends IntervalItem {

    /**
     * A BeerGlass osztály konstruktora.
     * Létrehoz és inicializál egy BeerGlass objektumot.
     *
     * @param location A szoba, amiben a tárgy van.
     * @param holder A személy, akinél a tárgy van.
     * @param activated A tárgy aktiválva van-e.
     * @param timeRemaining A hátralévő idő, amíg a tárgy aktív.
     */
    public BeerGlass(Room location, Person holder, boolean activated, int timeRemaining){
        super(location, holder, activated, timeRemaining);
    }

    /**
     * Egy személlyel való találkozást kezeli, ha a földön van.
     * Nincsen semmilyen hatása, mert ha a földön van nem tud senkit megvédeni.
     * 
     * @param person Az a személy, akivel találkozik.
     */
    @Override
    public void meet(Person person) { }

    /**
     * Kibukás elleni védelem kérése.
     * Amennyiben aktiválva van a söröspohár, logikai igazzal tér vissza, megvédve a birtokosát.
     * A söröspohár használata miatt a birtokosa elejt egy véletlenszerű tárgyat.
     * Egyéb esetben logikai hamissal tér vissza.
     * 
     * @param killer Az a személy, aki megtámadta a BeerGlass objektum tulajdonosát.
     * @return {@code true} ha aktiválva van, {@code false} egyébként.
     */
    @Override
    public boolean saveFromDeath(Person killer) {
        if(activated){
            holder.dropRandomItem();
            return true;
        }
        return false;
    }

    /**
     * Mérgező gáz elleni védelem kérése.
     * A BeerGlass nem nyújt védelmet a gáz ellen.
     * 
     * @return {@code false} minden esetben.
     */
    @Override
    public boolean saveFromGas() {
        return false;
    }

    /**
     * Idő telése a tárgyon.
     * Ha aktiválva van a tárgy, akkor a kapott paraméterrel csökkenti az objektum timeRemaining tagváltozóját.
     * Ha a timeRemaining elérte a 0-t, akkor kezdeményezi aktuális birtokosánál a tárgy megsemmisítését.
     * 
     * @param time Az eltelt idő.
     */
    @Override
    public void timeElapsed(int time) {
        if(activated) {
            timeRemaining -= time;
            if(timeRemaining <= 0){
                if(holder != null){
                    holder.removeItem(this);
                }
                else{
                    location.removeItem(this);
                }
            }
        }
    }
}
