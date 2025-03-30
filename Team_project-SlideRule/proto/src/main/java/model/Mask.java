package model;

/**
 * A Mask osztály az FFP2-es maszk működését és viselkedését modellezi.
 * A hallgató képes manuálisan aktiválni, de egyébként automatikusan is aktiválódik a személyeknek.
 * Ha a hatása elmúlik, akkor a saját felelőssége eldönteni,
 * hogy használható-e újra rövidebb ideig. Ha ez az idő 0-ra csökken, akkor megsemmisül.
 * Az IntervalItem leszármazottja.
 */
public class Mask extends IntervalItem{

    //Minden Mask ezzel az értékkel csökkenti a duration értékét miután lejár az aktiválási ideje
    private static final int DURATION_DECR = 2;
    /*
     * A Mask tartóssága, tárolja hogy következő aktiváláskor mennyi ideig lesz aktív
     * Értéke minden deaktiválással csökken, és ha eléri a nullát, megszűnik a Mask
     */
    private int duration;

    /**
     * A Mask osztály konstruktora.
     * Létrehoz és inicializál egy Mask objektumot.
     * A duration értékét beállítja, hogy a második aktiváláskor mennyi ideig hat majd.
     *
     * @param location a szoba, amiben a tárgy van
     * @param holder a személy, akinél a tárgy van
     * @param activated a tárgy aktiválva van-e
     * @param timeRemaining a hátralévő idő, amíg a tárgy aktív
     */
    public Mask(Room location, Person holder, boolean activated, int timeRemaining){
        super(location, holder, activated, timeRemaining);
        this.duration = timeRemaining - DURATION_DECR;
    }

    /**
     * Találkozás személlyel
     * Nem csinál semmit, mert ha földön van nincs kit megvédenie.
     * @param person a személy, akivel találkozik a tárgy
     */
    @Override
    public void meet(Person person) { }

    /**
     * Kibukás elleni védelem kérése
     * A tárgy nem nyújt védelmet a kibukás ellen.
     * @param killer a támadó személy
     * @return {@code false} minden esetben
     */
    @Override
    public boolean saveFromDeath(Person killer) {
        return false;
    }

    /**
     * Mérgező gáz elleni védelem kérése
     * Amennyiben aktiválva van a maszk, logikai igazzal tér vissza, megvédve a birtokosát.
     * Ha nincs aktiválva, akkor aktiválódik, és szintén logikai igazzal tér vissza.
     * @return {@code true} minden esetben
     */
    @Override
    public boolean saveFromGas() {
        if(!activated){
            this.activate();
        }
        return true;
    }

    /**
     * Idő telése a maszkon
     * Ha aktiválva van a tárgy, akkor a timeRemaining értékét
     * csökkenti time-mal. Ha elérte a 0-t, akkor adott értékkel csökkenti a durationt és
     * visszaállítja az activatedet hamisra. Ha a duration elérte a 0-t akkor aktuális
     * birtokosánál kezdeményezi a tárgy megsemmisítését
     * @param time az eltelt idő
     */
    @Override
    public void timeElapsed(int time) {
        if(activated){
            timeRemaining -= time;
            if(timeRemaining <= 0){
                if(duration <= 0){
                    if(holder != null){
                        holder.removeItem(this);
                    }
                    else{
                        location.removeItem(this);
                    }
                }
		else {
		    activated = false;
		    timeRemaining = duration;
                    duration -= DURATION_DECR;
		}
            }
        }
    }

    /*
     * Tartósság lekérdezése
     */
    public int getDuration() {
        return duration;
    }

    /*
     * Tartósság beállítása
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }
}
