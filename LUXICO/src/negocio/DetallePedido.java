package negocio;

import java.io.Serializable;

public class DetallePedido implements Serializable {

    private Producto producto;
    private int cantidad;
    private String codigoProductoSnap;
    private String modeloSnap;
    private String tallaSnap;
    private double precioUnitario;

    public DetallePedido(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;

        if (producto != null) {
            this.codigoProductoSnap = producto.getCodigo();
            this.modeloSnap = producto.getModelo();
            this.tallaSnap = producto.getTalla();
            this.precioUnitario = producto.getPrecio();
        } else {
            this.codigoProductoSnap = "N/A";
            this.modeloSnap = "N/A";
            this.tallaSnap = "N/A";
            this.precioUnitario = 0;
        }
    }

    public String getCodigoProducto() {
        if (producto != null && producto.getCodigo() != null) return producto.getCodigo();
        return codigoProductoSnap;
    }

    public String getModelo() {
        if (producto != null && producto.getModelo() != null) return producto.getModelo();
        return modeloSnap;
    }

    public String getTalla() {
        if (producto != null && producto.getTalla() != null) return producto.getTalla();
        return tallaSnap;
    }

    public double getPrecio() {
        return precioUnitario;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
        if (producto != null) {
            this.codigoProductoSnap = producto.getCodigo();
            this.modeloSnap = producto.getModelo();
            this.tallaSnap = producto.getTalla();
        }
    }

    public double getSubtotal() {
        return getCantidad() * getPrecio();
    }

    @Override
    public String toString() {
        return getCodigoProducto() + " | " + getModelo() + " | Talla: " + getTalla()
                + " | Cant: " + cantidad + " | PrecioUnit: " + precioUnitario
                + " | Subtotal: " + getSubtotal();
    }
}