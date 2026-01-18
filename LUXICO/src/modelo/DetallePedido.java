package modelo;

import java.io.Serializable;

public class DetallePedido implements Serializable {
    private static final long serialVersionUID = 1L;
    private String codigoProducto;
    private String modelo;
    private String talla;
    private double precioUnitario;
    private int cantidad;

    public DetallePedido(String codigoProducto, String modelo, String talla, double precioUnitario, int cantidad) {
        try {
            if (codigoProducto == null || codigoProducto.trim().isEmpty()) throw new Exception();
            if (modelo == null || modelo.trim().isEmpty()) throw new Exception();
            if (talla == null || talla.trim().isEmpty()) throw new Exception();
            if (precioUnitario <= 0) throw new Exception();
            if (cantidad <= 0) throw new Exception();

            this.codigoProducto = codigoProducto.trim().toUpperCase();
            this.modelo = modelo.trim();
            this.talla = talla.trim();
            this.precioUnitario = precioUnitario;
            this.cantidad = cantidad;

        } catch (Exception e) {
            System.out.println("Error creando detalle del pedido: datos inválidos.");
            this.codigoProducto = "SIN-COD";
            this.modelo = "SIN-MODELO";
            this.talla = "0";
            this.precioUnitario = 0;
            this.cantidad = 0;
        }
    }

    public String getCodigoProducto() { return codigoProducto; }
    public String getModelo() { return modelo; }
    public String getTalla() { return talla; }
    public double getPrecioUnitario() { return precioUnitario; }
    public int getCantidad() { return cantidad; }

    public double getSubtotal() {
        try {
            return precioUnitario * cantidad;
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public String toString() {
        try {
            return codigoProducto + " | " + modelo + " | Talla: " + talla +
                    " | Cant: " + cantidad + " | P.Unit: " + precioUnitario +
                    " | Subtotal: " + getSubtotal();
        } catch (Exception e) {
            return "Detalle inválido";
        }
    }
}