package negocio;

import java.io.Serializable;

public class MovimientoInventario implements Serializable {

    private Producto producto;

    private TipoMovimiento tipo;
    private int cantidad;
    private String fecha;
    private String motivo;
    private String codigoProductoSnap;

    public MovimientoInventario(Producto producto, TipoMovimiento tipo, int cantidad, String fecha, String motivo) {
        this.producto = producto;
        this.tipo = tipo;
        this.cantidad = cantidad;
        this.fecha = fecha;
        this.motivo = motivo;

        if (producto != null && producto.getCodigo() != null) this.codigoProductoSnap = producto.getCodigo();
        else this.codigoProductoSnap = "N/A";
    }

    public String getCodigoProducto() {
        if (producto != null && producto.getCodigo() != null) return producto.getCodigo();
        return codigoProductoSnap;
    }

    public TipoMovimiento getTipo() {
        return tipo;
    }

    public int getCantidad() {
        return cantidad;
    }

    public String getFecha() {
        return fecha;
    }

    public String getMotivo() {
        return motivo;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
        if (producto != null && producto.getCodigo() != null) this.codigoProductoSnap = producto.getCodigo();
    }

    @Override
    public String toString() {
        return "Producto: " + getCodigoProducto() +
                " | Tipo: " + tipo +
                " | Cantidad: " + cantidad +
                " | Fecha: " + fecha +
                " | Motivo: " + motivo;
    }
}