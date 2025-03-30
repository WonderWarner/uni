package model;

/**
 * Az AirFresher osztály a légfrissítő működését és viselkedését modellezi.
 * Ez az egyszerhasználatos tárgy aktiváláskor megszünteti a szoba gázosságát.
 * Az osztály az Item absztakt osztályból származik.
 */
public class AirFresher extends Item {

    /**
     * A AirFresher osztály kontruktora.
     * Létrehoz és inicializál egy AirFresher objektumot.
     *
     * @param location A szoba, amiben a tárgy van.
     * @param holder A személy, akinél a tárgy van.
     */
    public AirFresher(Room location, Person holder){
        super(location, holder);
    }

    /**
     * A légfrissítő aktiválása.
     * Tartózkodási szobáján meghívja a {@code location.setGas(false)} metódust és felfrissíti azt.
     * Igényli birtokosánál az önmegsemmisítést {@code (holder.removeItem())}.
     */
    @Override
    public void activate() {
        location.setGas(false);
        holder.removeItem(this);
    }

    /**
     * Egy személlyel való találkozást kezeli, ha a földön van.
     * Nincsen semmilyen hatása a földön.
     * 
     * @param person Az a személy, akivel találkozik.
     */
    @Override
    public void meet(Person person) { }

    /**
     * Kibukás elleni védelem kérése.
     * A tárgy nem nyújt védelmet támadás ellen.
     * 
     * @param killer Az a személy, aki megtámadta a AirFresher objektum tulajdonosát.
     * @return {@code false} minden esetben.
     */
    @Override
    public boolean saveFromDeath(Person killer) {
        return false;
    }

    /**
     * Mérgező gáz elleni védelem kérése.
     * A AirFresher nem nyújt védelmet a gáz ellen.
     * 
     * @return {@code false} minden esetben.
     */
    @Override
    public boolean saveFromGas() {
        return false;
    }

    /**
     * Idő telése a tárgyon.
     * Mivel egyszerhasználatos tárgy, így nem történik vele
     * semmi az idő múlásával.
     * 
     * @param time Az eltelt idő.
     */
    @Override
    public void timeElapsed(int time) { }
}
