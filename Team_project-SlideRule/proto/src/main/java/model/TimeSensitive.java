package model;

/**
 * Ezen az interfészen keresztül értesülnek a játék objektumai az idő teléséről.
 */
public interface TimeSensitive {
    /**
     * Ezen keresztül értesülnek az interfészt megvalósító
     * objektumok azzal, hogy a játékban time idő eltelt.
     * @param time az eltelt idő
     */
    public void timeElapsed( int time );
}
