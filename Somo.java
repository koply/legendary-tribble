import java.util.*;

/*
 * Author: Koply (16.05.2021)
 */
public class Somo {
    void println(Object o)  { System.out.println(o); }
    
    public static void main(String[] args) {
        new Somo().run();
    }

    private static final Scanner sc = new Scanner(System.in);
    public void run() {
        println("Hello World!");

        // first literal - operator - second literal
        println("Enter first.");
        String first = sc.nextLine().trim().toLowerCase();

        println("Enter operator.");
        String operator = sc.nextLine().trim().toLowerCase();

        println("Enter second.");
        String second = sc.nextLine().trim().toLowerCase();

        long before = System.nanoTime();

        char operatorFirst = operator.charAt(0);
        boolean operatorSecond = operator.length() != 1;
        boolean or = false;
        boolean greater = false;
        boolean not = false;

        boolean result = false;
        switch (operatorFirst) {
            case '|':
                or = true;
            case '&': 
                boolean firb = first.equals("true");
                boolean secb = second.equals("true");

                result = or ? firb||secb : firb&&secb;
                break;
            case '>':
                greater = true;
            case '<':
                Double fi = parseDecimal(first);
                Double seco = parseDecimal(second);
                if (fi == null || seco == null) return;

                boolean equality = operatorSecond ? operator.charAt(1) == '=' : false;
                result = greater
                         ? (equality ? fi >= seco : fi > seco)
                         : (equality ? fi <= seco : fi < seco);
                break;
            case '!':
                not = true;
            case '=':
                boolean temp = first.equals(second);
                result = not ? !temp : temp;
                break;
        }

        println(result);
        println( (System.nanoTime()-before) + " ns");
    }

    Double parseDecimal(String som) {
        try {
            return Double.parseDouble(som);
        } catch (Exception ignored) {
            return null;
        }
    }
    // bool - decimal - string
    // bool operates -> || &&
    // decimal opers -> < <= >= >
    // bool, decimal, string operas -> == !=
}
