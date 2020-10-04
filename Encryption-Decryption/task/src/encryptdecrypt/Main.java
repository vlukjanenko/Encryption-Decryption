package encryptdecrypt;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Main {

    public  static int ftAtoi(String str) {

        int     sign = 1;
        int     i = 0;
        long    result = 0;

        if (str.length() > 0 && (str.charAt(0) == '-' || str.charAt(0) == '+')) {
            sign = 44 - str.charAt(0);
            i++;
        }
        while (i < str.length() && str.charAt(i) >= '0' && str.charAt(i) <= '9') {
            result = result * 10 + str.charAt(i) - '0';
            i++;
        }
        return (int) result * sign;
    }

    public static String enc(String source, int key, String mode, String alg) {

        char[] chars = source.toCharArray();
        String result = "";

        if (alg.equals("unicode")) {
            if (mode.equals("dec")) {
                key = -key;
            }
            for (char item : chars) {
                if (item >= ' ' && item <= '~') {
                    char shiftItem = (char) (((item - (key >= 0 ? ' ' : '~') + key) % 95) + (key >= 0 ? ' ' : '~'));
                    result += shiftItem;

                }
            }
        } else if (alg.equals("shift")) {
            if (mode.equals("dec")) {
                key = -key;
            }
                for (char item : chars) {
                    if (item >= 'a' && item <= 'z') {
                        char shiftItem = (char) (((item - (key >= 0 ? 'a' : 'z') + key) % 26) + (key >= 0 ? 'a' : 'z'));
                        result += shiftItem;
                    } else if (item >= 'A' && item <= 'Z') {
                        char shiftItem = (char) (((item - (key >= 0 ? 'A' : 'Z') + key) % 26) + (key >= 0 ? 'A' : 'Z'));
                        result += shiftItem;
                    } else {
                        result += item;
                    }
                }
            }
            return  result;
    }

    public static String findParam(String param, String[] args){

        int i = 0;

        while (i < args.length) {
            if (param.equals(args[i])) {
                return args[i + 1];
            }
            i++;
        }
        if (param.equals("-mode")) {
            return "enc";
        }
        if (param.equals("-alg")) {
            return "shift";
        }
        return "";
    }

    public static void main(String[] args) throws FileNotFoundException {

        String  source = findParam("-data", args);
        String  result = "";
        String  mode = findParam("-mode", args);
        String  in = findParam("-in", args);
        String  out = findParam("-out", args);
        String  alg = findParam("-alg", args);
        int     key = ftAtoi(findParam("-key", args));
        if (source.isEmpty() && !in.isEmpty()) {
            File file = new File(in);
            try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNext()) {
                    source = scanner.nextLine();
                    result = result + "\n" + enc(source, key, mode, alg);
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                return;
            }
        }
        if (!out.isEmpty()) {
            File file = new File(out);
            try (PrintWriter printWriter = new PrintWriter(file)) {
                printWriter.println(result);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                return;
            }
            return;
        }
        result = enc(source, key, mode, alg);
        System.out.println(result);
    }
}
