package vista;

import java.util.Scanner;

public class Input {
    private Scanner sc;

    public Input() {
        sc = new Scanner(System.in);
    }

    public String leerTexto(String msg) {
        try {
            System.out.print(msg);
            return sc.nextLine();
        } catch (Exception e) {
            System.out.println("Error leyendo texto.");
            return "";
        }
    }

    public int leerEntero(String msg) {
        try {
            System.out.print(msg);
            return Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException nfe) {
            System.out.println("Error: ingresa solo números.");
            return -1;
        } catch (Exception e) {
            System.out.println("Error leyendo entero.");
            return -1;
        }
    }

    public double leerDouble(String msg) {
        try {
            System.out.print(msg);
            return Double.parseDouble(sc.nextLine());
        } catch (NumberFormatException nfe) {
            System.out.println("Error: ingresa un número válido (ej: 10.5).");
            return -1;
        } catch (Exception e) {
            System.out.println("Error leyendo double.");
            return -1;
        }
    }
}