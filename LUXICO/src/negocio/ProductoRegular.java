package negocio;

import java.io.Serializable;

public class ProductoRegular extends Producto implements Serializable {
    private static final long serialVersionUID = 1L;
    public ProductoRegular(String codigo, String modelo, String talla, double precio, int stock, int stockMin) {
        super(codigo, modelo, talla, precio, stock, stockMin);
    }

    @Override
    public String getTipo() {
        return "Regular";
    }
}