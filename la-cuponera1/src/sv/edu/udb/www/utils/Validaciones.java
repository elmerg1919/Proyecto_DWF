package sv.edu.udb.www.utils;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validaciones {

    private static int entero;
    private static double decimal;

    public static boolean esEntero(int cadena) {
        try {
            entero = Integer.parseInt(Integer.toString(cadena).trim());
            return true;
        } catch (Exception a) {
            return false;
        }
    }

    
    public static boolean esEnteroPositivo(int cadena) {
        try {
            entero = Integer.parseInt(Integer.toString(cadena));
            if (entero <= 0) {
                return false;
            }
            return true;
        } catch (Exception a) {
            return false;
        }
    }

    public static boolean isEmpty(String mensaje) {
        return mensaje.trim().equals("");
    }
    
    
    public static boolean esDecimal(String cadena) {
        try {
            decimal = Double.parseDouble(cadena.trim());
            return true;
        } catch (Exception a) {
            return false;
        }
    }

    public static boolean esDecimalPositivo(String cadena) {
        try {
            decimal = Double.parseDouble(cadena.trim());
            if (decimal <= 0) {
                return false;
            }
            return true;
        } catch (Exception a) {
            return false;
        }
    }

    public static boolean esTelefono(int cadena) {
        Pattern pat = Pattern.compile("[267][0-9]{7}");
        Matcher mat = pat.matcher(Integer.toString(cadena));
        return mat.matches();
    }

    public static boolean esCodigoEmpresa(String cadena) {
        Pattern pat = Pattern.compile("EMP[0-9]{3}");
        Matcher mat = pat.matcher(cadena);
        return mat.matches();
    }
    
    public static boolean esCorreo(String cadena) {
        Pattern pat = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher mat = pat.matcher(cadena);
        return mat.matches();
    }
    
    public static boolean esDui(String cadena) {
        Pattern pat = Pattern.compile("[0-9]{8}-[0-9]{1}");
        Matcher mat = pat.matcher(cadena);
        return mat.matches();
    }
    
    public static boolean esCodigoLibro(String cadena) {
        Pattern pat = Pattern.compile("LIB[0-9]{6}");
        Matcher mat = pat.matcher(cadena);
        return mat.matches();
    }

}
