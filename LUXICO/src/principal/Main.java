package principal;

import negocio.SistemaLuxico;
import vista.Menu;

public class Main {
    public static void main(String[] args) {
        try {
            SistemaLuxico sistema = new SistemaLuxico();
            Menu menu = new Menu(sistema);
            menu.iniciar();
        } catch (Exception e) {
            System.out.println("Error fatal. Se cerró el programa.");
        }
    }
}