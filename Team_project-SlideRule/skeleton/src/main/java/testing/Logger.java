package testing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 * A Logger osztály a konzolra kiíratást segítő osztály, a szkeleton helyes működését segíti.
 * Számontartja a változók neveit és a megfelelő behúzási értéket.
 */
public class Logger {
    /**
     * A Logger osztály konstruktora.
     * Létrehoz egy Logger objektumot.
     */
    private Logger(){

    }

    private static final HashMap<Object, String> names = new HashMap<>();
    private static int indent = 0;

    /**
     * A behúzási értéknek megfelelő mennyiségű tabulátort ír a konzolra.
     */
    private static void writeIndent(){
        for( int i = 0 ; i < indent ; i++ ){
            System.out.print( '\t' );
        }
    }

    /**
     * Új logging sessiont kezd.
     * Törli a változóneveket és visszaállítja abehúzási értéket.
     */
    public static void startNew(){
        names.clear();
        indent = 0;
    }

    /**
     * Egy objektum regisztrálása a Loggerbe a megadott változónéven, vagyis eltárolja ezt a párost a names hashmapben.
     * @param object a regisztrálni kívánt objektum
     * @param name a név, amin az objektumot regisztrálja
     */
    public static void register( Object object, String name ){
        writeIndent();
        names.put( object, name);
        System.out.println( name + " created" );
    }

    /**
     * Új objektum létrehozásának kezdetét logolja.
     * A megefelelő behúzás után az objektum konstruktorának nevét írja ki egy jobbra mutató nyíl után.
     * @param object az új objektum
     */
    public static void create( Object object ){
        writeIndent();
        System.out.println( "->" + object.getClass().getSimpleName() + "()" );
        indent++;
    }

    /**
     * Egy függvényhívás logolása.
     * A megfelelő mennyiségú tabulátort kiírja, majd egy jobbra mutató nyilat ír ki.
     * Ezután a megadott objektum nevét kikeresi a names hashmapből, utánaírja a ponttal elválasztva a kapott függvénynevet,
     * végül a függvényhívás paramétereit zárójelekben, veszőkkel elválasztva jeleníti meg.
     * A behúzási értéket növeli eggyel.
     * @param object az objektum, amin a függvényhívás történt
     * @param func a hívott függvény
     * @param args a hívott függvény paraméterei
     */
    public static void enter( Object object, String func, List<Object> args ){
        writeIndent();
        System.out.print( "->" + names.get(object) + "." + func + "(" );
        for( int i = 0 ; i < args.size() ; i++ ){
            if( args.get( i ) == null ){
                System.out.print( "null" );
            } else if( args.get( i ).getClass() == Integer.class ){
                System.out.print( args.get( i ) );
            } else {
                System.out.print(names.get(args.get(i)));
            }

            if( i < args.size() - 1 ){
                System.out.print( ", ");
            }
        }
        System.out.println(")");
        indent++;
    }

    /**
     * Egy függvényhívás logolása.
     * A két kapott paramétert kiegészíti egy üres listával és meghívja a az enter(Object, String, List<Object>) függvényt
     * @param object az objektum, amin a függvényhívás történt
     * @param func a hívott függvény
     */
    public static void enter( Object object, String func ){
        List<Object> list = new ArrayList<>();
        enter( object, func, list );
    }

    /**
     * Egy visszatérés logolása.
     * A behúzási értéket csökkenti eggyel.
     * A megfelelő mennyiségú tabulátort kiírja, majd egy balra mutató nyilat ír ki.
     * Ezután a megadott objektum nevét kikeresi a names hashmapből, utánaírja a ponttal elválasztva a kapott függvénynevet.
     * Végül ha van visszatérési érték (nem null), akkor azt kettősponttal elválasztva jeleníti meg.
     * @param object az objektum, aminek a tagfüggvényéből visszatérés történt
     * @param func a függvény amiből visszatér
     * @param value visszatérési érték
     */
    public static void exit( Object object, String func, String value ){
        indent--;
        writeIndent();
        System.out.print( "<-" + names.get(object) + "." + func );
        if( value != null ){
            System.out.println(": " + value );
        } else {
            System.out.println();
        }
    }

    /**
     * Egy visszatérés logolása.
     * A kapott paramétereket kieégészíti egy null értékkel és meghívja
     * az exit(Object, String, String) függvényt.
     * @param object az objektum, aminek a tagfüggvényéből visszatérés történt
     * @param func a függvény amiből visszatér
     */
    public static void exit( Object object, String func ){
        exit( object, func, null );
    }

    /**
     * Új objektum létrehozásának végét logolja.
     * A megefelelő behúzás után az objektum konstruktorának nevét írja ki egy balra mutató nyíl után.
     * @param object az új objektum
     */
    public static void exitCreate( Object object ){
        indent--;
        writeIndent();
        System.out.println( "<-" + object.getClass().getSimpleName() );
    }

    private static final Scanner in = new Scanner( System.in );

    /**
     * Kérdés feltevése
     * A megfelelő behúzás után a paraméterben kapott eldöntendő kérdést felteszi a felhasználónak.
     * A felhasználó az 'y' és 'n' karakterekkel tud válaszolni a kérdésre.
     * @param question az eldöntendő kérdés
     * @return {@code true} ha a válasz 'y', {@code false} ha a válasz 'n'
     */
    private static boolean askQuestion( String question ){
        writeIndent();
        System.out.print( question + " [y/n] " );
        while( in.hasNext() ) {
            String answer = in.next();
            if( answer.equals( "y" ) ){
                return true;
            } else if( answer.equals( "n" ) ){
                return false;
            } else {
                writeIndent();
                System.out.print( question + " [y/n] " );
            }
        }
        return false;
    }

    /**
     * Olyan kérdés feltételére ad lehetősegt, amiben megjelenik egy változó neve.
     * A paraméterben kapott eldöntendő kérdésben a változónév helyett #-et kicseréli a kapott objektumhoz
     * tartozó változó nevével.
     * Végül meghívja az askQuestion(String) függvényt.
     * @param question az eldöntendő kérdés
     * @param object az objektum, aminek a változóneve szerepel a kérdésben
     * @return {@code true} ha a válasz 'y', {@code false} ha a válasz 'n'
     */
    public static boolean askQuestion( String question, Object object ){
        String name = names.get( object );
        String fullQuestion = question.replace( "#", name );
        return askQuestion( fullQuestion );
    }
}
