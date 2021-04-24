import java.util.*;

/*
 * Author: Koply
 * https://github.com/MusaBrt/legendary-tribble
 * -- 24.04.2021 --
 */
public class Main {

    static final Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {
        new Main().run();
    }

    public void run() {
        while (true) {
            o("value: ");
            String first = sc.nextLine();
            if (first.equalsIgnoreCase("exit")) {
                break;
            }
            
            o("operator: ");
            String operator = sc.nextLine();
            
            o("otherValue: ");
            String second = sc.nextLine();
            
            Type entryType = detectType(first);
            Type operatorType = detectOperatorType(operator);

            handleResults(first, second, operator, entryType, operatorType);
        }
    }

    Type detectType(String entry) {
        entry = entry.toLowerCase();
        if (entry.contains("true") || entry.contains("false")) {
            return Type.BOOL;
        }
        try {
            Double.parseDouble(entry);
            return Type.DECIMAL;
        } catch (Exception ignored) {
            return Type.STRING;
        }
    }

    // so much basic, needs to error handler
    Type detectOperatorType(String operator) {
        if (operator.contains(">") || operator.contains("<")) return Type.DECIMAL;
        else if (operator.contains("|") || operator.contains("&")) return Type.BOOL;
        return Type.STRING;
    }

    void handleResults(String first, String second, String operator, Type entryType, Type operatorType) {
        Process handler = getHandler(entryType, operatorType);
        // fuck
        Object firstWrapper;
        Object secondWrapper;
        boolean result = false;
        switch (entryType) {
            case DECIMAL:
                firstWrapper = Double.parseDouble(first);
                secondWrapper = Double.parseDouble(second);
                break;
            case BOOL:
                firstWrapper = Boolean.parseBoolean(first);
                secondWrapper = Boolean.parseBoolean(second);
                break;
            case STRING:
                firstWrapper = first;
                secondWrapper = second;
                break;
            default:
                o("\nPanic! Unexpected entryType");
                return;
        }
        result = handler.run(firstWrapper, secondWrapper, operator);
        o("Result: " + result + "\n");
    }

    /**
     * logic operators:      ||    -   &&     == bool        (ONLY)      [   bool   as operatorType ]
     * compare operators:    >= >  -  < <=    == decimal     (ONLY)      [  decimal as operatorType ]
     * equality operators:   ==    -   !=     == string, decimal, bool   [  string  as operatorType ]
     */
    Process getHandler(Type entryType, Type operatorType) {
        if (operatorType == Type.DECIMAL && entryType != Type.DECIMAL) return null;
        if (operatorType == Type.BOOL && entryType != Type.BOOL) return null;

        if (operatorType == Type.BOOL) {
            Process<Boolean> boolHandler = (Boolean x, Boolean y, String operator) -> {
                if (operator.contains("|")) {
                    return x || y;
                } else {
                    return x && y;
                }
            };
            return boolHandler;
        } else if (operatorType == Type.DECIMAL) {
            Process<Double> decimalHandler = (Double x, Double y, String operator) -> {
                switch (operator) {
                    case "<":
                        return x<y;
                    case ">":
                        return x>y;
                    case "<=":
                        return x<=y;
                    case ">=":
                        return x>=y;
                    default:
                        o("\nPanic! Unexpected operator string."); // this must be serr but why not
                        return false;
                }
            };
            return decimalHandler;
        } else if (operatorType == Type.STRING) {
            // equality operators:   ==    -   !=     == string, decimal, bool   [  string  ]
            Process<Object> stringHandler = (Object x, Object y, String operator) -> {
                boolean lel = x.equals(y);
                return operator.contains("!") ? !lel : lel;
            };
            return stringHandler;
        }

        return null;
    }


    static void o(Object x) { System.out.print(x.toString()); }

}

enum Type { BOOL, DECIMAL, STRING, WTFISTHIS; }

interface Process<T> {
    boolean run(T x, T y, String operator);
}
