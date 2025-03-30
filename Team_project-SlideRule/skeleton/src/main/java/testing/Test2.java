package testing;

import model.Teacher;

public class Test2 implements ITest {
    /**
     * Létrehoz két oktatót,
     * akik találkoznak.
     * A változó neveket regisztrálja.
     */
    @Override
    public void run() {
        Teacher t1 = new Teacher();
        Logger.register(t1, "t1");
        Teacher t2 = new Teacher();
        Logger.register(t2, "t2");
        t1.meet(t2);
    }
}
