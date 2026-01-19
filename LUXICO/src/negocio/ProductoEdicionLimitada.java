package negocio;

import java.io.Serializable;

public class ProductoEdicionLimitada extends Producto implements Serializable {
    private static final long serialVersionUID = 1L;
    private int numeroSerie;

    public ProductoEdicionLimitada(String codigo, String modelo, String talla, double precio, int stock, int stockMin, int numeroSerie) {
        super(codigo, modelo, talla, precio, stock, stockMin);
        try {
            if (numeroSerie <= 0) throw new Exception();
            this.numeroSerie = numeroSerie;
        } catch (Exception e) {
            System.out.println("Número de serie inválido, se asigna 1.");
            this.numeroSerie = 1;
        }
    }

    public ProductoEdicionLimitada(String codigo, String modelo, String talla, double precio, int stock, int stockMin) {
        super(codigo, modelo, talla, precio, stock, stockMin);
        try {
            this.numeroSerie = 1;
        } catch (Exception e) {
            this.numeroSerie = 1;
        }
    }

    @Override
    public String getTipo() {
        return "Edición Limitada";
    }

    public int getNumeroSerie() { return numeroSerie; }
}