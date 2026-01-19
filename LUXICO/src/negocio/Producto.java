package negocio;

import java.io.Serializable;

public abstract class Producto implements Serializable {
    private static final long serialVersionUID = 1L;
    protected String codigo;
    protected String modelo;
    protected String talla;
    protected double precio;
    protected int stock;
    protected int stockMin;

    public Producto(String codigo, String modelo, String talla, double precio, int stock, int stockMin) {
        try {
            if (codigo == null || codigo.trim().isEmpty()) throw new Exception();
            if (modelo == null || modelo.trim().isEmpty()) throw new Exception();
            if (talla == null || talla.trim().isEmpty()) throw new Exception();
            if (precio <= 0) throw new Exception();
            if (stock < 0 || stockMin < 0) throw new Exception();

            this.codigo = codigo.trim().toUpperCase();
            this.modelo = modelo.trim();
            this.talla = talla.trim();
            this.precio = precio;
            this.stock = stock;
            this.stockMin = stockMin;

        } catch (Exception e) {
            System.out.println("Error creando producto: datos inválidos.");
            this.codigo = "SIN-COD";
            this.modelo = "SIN-MODELO";
            this.talla = "0";
            this.precio = 0;
            this.stock = 0;
            this.stockMin = 0;
        }
    }

    public abstract String getTipo();

    public boolean descontarStock(int cantidad) {
        try {
            if (cantidad <= 0) throw new Exception();
            if (cantidad > stock) throw new Exception();

            stock -= cantidad;
            return true;

        } catch (Exception e) {
            System.out.println("Error: stock insuficiente o cantidad inválida.");
            return false;
        }
    }

    public boolean aumentarStock(int cantidad) {
        try {
            if (cantidad <= 0) throw new Exception();
            stock += cantidad;
            return true;
        } catch (Exception e) {
            System.out.println("Error: cantidad inválida para aumentar stock.");
            return false;
        }
    }

    public boolean estaBajoMinimo() {
        return stock <= stockMin;
    }

    public String getCodigo() { return codigo; }
    public String getModelo() { return modelo; }
    public String getTalla() { return talla; }
    public double getPrecio() { return precio; }
    public int getStock() { return stock; }
    public int getStockMin() {
        try {
            return stockMin;
        } catch (Exception e) {
            return 0;
        }
    }

    public boolean setPrecio(double precio) {
        try {
            if (precio <= 0) throw new Exception();
            this.precio = precio;
            return true;
        } catch (Exception e) {
            System.out.println("Error: precio inválido.");
            return false;
        }
    }

    public boolean setStockMin(int stockMin) {
        try {
            if (stockMin < 0) throw new Exception();
            this.stockMin = stockMin;
            return true;
        } catch (Exception e) {
            System.out.println("Error: stock mínimo inválido.");
            return false;
        }
    }

    public boolean setModelo(String modelo) {
        try {
            if (modelo == null || modelo.trim().isEmpty()) throw new Exception();
            this.modelo = modelo.trim();
            return true;
        } catch (Exception e) {
            System.out.println("Error: modelo inválido.");
            return false;
        }
    }

    public boolean setTalla(String talla) {
        try {
            if (talla == null || talla.trim().isEmpty()) throw new Exception();
            this.talla = talla.trim();
            return true;
        } catch (Exception e) {
            System.out.println("Error: talla inválida.");
            return false;
        }
    }
}