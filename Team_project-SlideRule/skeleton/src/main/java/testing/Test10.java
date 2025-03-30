package testing;

import model.Rag;
import model.Room;
import model.Teacher;


public class Test10 implements ITest {
    /**
     * Létrehoz egy szobát és elhelyez benne egy táblatörlő rongyot és egy oktatót. Az
     * időtelésére, ha a rongy aktív és nincsen kiszáradva, akkor az
     * oktatót megbénítja. Ha a rongy kiszárad, akkor eltűnik a
     * szobából. A felhasználó tudja eldönteni, hogy a rongy aktív-e
     * illetve, hogy ki van-e száradva.
     * A váltózóneveket regisztrálja.
     */
    @Override
    public void run() {
        Teacher t = new Teacher();
        Logger.register(t, "t");
        Room r = new Room();
        Logger.register(r, "r");
        Rag rag = new Rag();
        Logger.register(rag, "rag");
        r.addPerson(t);
        t.setLocation(r);
        r.initItem(rag);
        rag.initLocation(r, null);
        r.timeElapsed(1);
    }
}