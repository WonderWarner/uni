package model;

/**
 * A Camembert osztály a káposztás camembert működését és viselkedését modellezi.
 * Aktiválásakor a szobát, amiben van mérgező gázzal árasztja el és eltűnik.
 * Az Item absztrakt osztályból származik.
 */
public class Camembert extends Item {
    /**
     * A Camembert osztály konstruktora.
     * Létrehoz és inicializál egy Camembert objektumot.
     *
     * @param location A szoba, amiben a tárgy van.
     * @param holder A személy, akinél a tárgy van.
     */
    public Camembert(Room location, Person holder){
        super(location, holder);
    }

    /**
     * A camembert aktiválása.
     * Igényli birtokosánál az önmegsemmisítést {@code (holder.removeItem())}.
     * Tartózkodási szobáján meghívja a {@code location.setGas(true)} metódust és elgázosítja azt.
     */
    @Override
    public void activate() {
        holder.removeItem(this);
        location.setGas(true);
    }

    /**
     * Egy személlyel való találkozást kezeli, ha a földön van.
     * Nincsen semmilyen hatása.
     * 
     * @param person Az a személy, akivel találkozik.
     */
    @Override
    public void meet(Person person) {}

    /**
     * Kibukás elleni védelem kérése.
     * A tárgy nem nyújt védelmet támadás ellen.
     * 
     * @param killer Az a személy, aki megtámadta a Camembert objektum tulajdonosát.
     * @return {@code false} minden esetben.
     */
    @Override
    public boolean saveFromDeath(Person killer) {
        return false;
    }

    /**
     * Mérgező gáz elleni védelem kérése.
     * A tárgy nem nyújt védelmet gáz ellen.
     * 
     * @return {@code false} minden esetben.
     */
    @Override
    public boolean saveFromGas() {
        return false;
    }

    /**
     * Idő múlása tárgyon.
     * Az idő műlása nincs rá hastással, mert egyszerhasználatos tárgy.
     * 
     * @param time Az eltelt idő.
     */
    @Override
    public void timeElapsed(int time) { }
}
