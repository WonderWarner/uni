package testing;

import model.Room;
import model.Student;
import model.TVSZ;

public class Test5 implements ITest {

    /**
     * Létrehoz egy hallgatót és egy TVSZ-t, majd hozzáadja egy újonnan
     * létrehozott szobához. Ezután a hallgató felveszi a TVSZ-t. A
     * felvett tárgy eltűnik a szobából, és a hallgató kezébe kerül, ha
     * van a hallgatónál hely és nincsen mégbénulva. Ezt a két
     * feltételt a felhasználó döntheti el. Ha valamelyik feltétel nem
     * áll fenn, akkor az tárgy a szobában marad.
     * A változóneveket regisztrálja.
     */
    @Override
    public void run() {
        Room r = new Room();
        Logger.register(r, "r");
        Student s = new Student();
        Logger.register(s, "s");
        TVSZ tv = new TVSZ();
        Logger.register(tv, "tv");
        r.addPerson(s);
        s.setLocation(r);
        r.initItem(tv);
        tv.initLocation(r, null);
        s.addItem(tv);
    }
}
