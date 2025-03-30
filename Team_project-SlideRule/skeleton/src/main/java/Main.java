import testing.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;


public class Main {
    /**
     * A szkeleton program menüje.
     * Egyszerű konzolos menü, ahol a felhasználó a teszteset sorszámának
     * bevitelével tudja elindítani az egyes teszteseteket.
     */
    private static void testSkeleton() {
        final HashMap<Integer, ITest> tests=new HashMap<>();
        final HashMap<Integer, String> testNames=new HashMap<>();
        tests.put(1, new Test1());
        testNames.put(1, "Entering a room");
        tests.put(2, new Test2());
        testNames.put(2, "Teachers meeting");
        tests.put(3, new Test3());
        testNames.put(3, "Students meeting");
        tests.put(4, new Test4());
        testNames.put(4, "Teacher meets Student");
        tests.put(5, new Test5());
        testNames.put(5, "Add item");
        tests.put(6, new Test6());
        testNames.put(6, "Drop item");
        tests.put(7, new Test7());
        testNames.put(7, "Camembert and Mask use");
        tests.put(8, new Test8());
        testNames.put(8, "Student stunned");
        tests.put(9, new Test9());
        testNames.put(9, "Mask timeElapsed");
        tests.put(10, new Test10());
        testNames.put(10, "Rag timeElapsed on floor");
        tests.put(11, new Test11());
        testNames.put(11, "BeerGlass timeElapsed");
        tests.put(12, new Test12());
        testNames.put(12, "Student saved by TVSZ");
        tests.put(13, new Test13());
        testNames.put(13, "Transistor teleport");
        tests.put(14, new Test14());
        testNames.put(14, "Room merge");
        tests.put(15, new Test15());
        testNames.put(15, "Room split");
        tests.put(16, new Test16());
        testNames.put(16, "Student picked up SlideRule");
        tests.put(17, new Test17());
        testNames.put(17, "Teacher picked up SlideRule");

        System.out.println("Testing the skeleton");
        System.out.println("If you finished with testing, type 'exit'");
        System.out.println("Available test cases:");
        for(int i=1; i<=tests.size(); i++) {
            System.out.println(i+": "+testNames.get(i));
        }
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr);
        while(true) {
            try {
                System.out.print("\tChoose a test case (1-"+tests.size()+"): ");
                String line = br.readLine();
                if(line==null||line.equals("exit")) {
                    System.out.println("Testing finished");
                    break;
                }
                int testNum = Integer.parseInt(line);
                ITest test = tests.get(testNum);
                if (test != null) {
                    Logger.startNew();
                    test.run();
                    System.out.println("Test Over.");
                }
                else {
                    System.out.println("Invalid number, try again!");
                }
            }
            catch(IOException e) {
                System.out.println("IOException happened:");
                e.printStackTrace();
            }
            catch (NumberFormatException e) {
                System.out.println("Not a number, try again!");
            }
        }
    }

    public static void main( String[] args ) {
        testSkeleton();
    }
}
