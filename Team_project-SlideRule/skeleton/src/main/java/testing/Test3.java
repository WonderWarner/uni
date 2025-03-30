package testing;

import model.Student;

public class Test3 implements ITest {
    /**
     * Létrehoz két
     * hallgatót, akik találkoznak.
     * A változóneveket regisztrálja.
     */
    @Override
    public void run() {
        Student s1 = new Student();
        Logger.register(s1, "s1");
        Student s2 = new Student();
        Logger.register(s2, "s2");
        s1.meet(s2);
    }
}
