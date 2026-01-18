package modelo;

import java.io.Serializable;

public class MovimientoInventario implements Serializable{
    private static final long serialVersionUID = 1L;
    private String codigoProducto;
    private TipoMovimiento tipo;
    private int cantidad;
    private String fecha;       // texto simple para no complicar (ej: 11/01/2026)
    private String motivo;      // opcional

    public MovimientoInventario(String codigoProducto, TipoMovimiento tipo, int cantidad, String fecha, String motivo) {
        try {
            if (codigoProducto == null || codigoProducto.trim().isEmpty()) throw new Exception();
            if (tipo == null) throw new NullPointerException();
            if (cantidad <= 0) throw new Exception();
            if (fecha == null || fecha.trim().isEmpty()) fecha = "SIN-FECHA";
            if (motivo == null) motivo = "";

            this.codigoProducto = codigoProducto.trim().toUpperCase();
            this.tipo = tipo;
            this.cantidad = cantidad;
            this.fecha = fecha.trim();
            this.motivo = motivo.trim();

        } catch (NullPointerException npe) {
            System.out.println("Error: tipo de movimiento no inicializado.");
            this.codigoProducto = "SIN-COD";
            this.tipo = TipoMovimiento.ENTRADA;
            this.cantidad = 0;
            this.fecha = "SIN-FECHA";
            this.motivo = "";
        } catch (Exception e) {
            System.out.println("Error creando MovimientoInventario: datos inválidos.");
            this.codigoProducto = "SIN-COD";
            this.tipo = TipoMovimiento.ENTRADA;
            this.cantidad = 0;
            this.fecha = "SIN-FECHA";
            this.motivo = "";
        }
    }

    public String getCodigoProducto() { return codigoProducto; }
    public TipoMovimiento getTipo() { return tipo; }
    public int getCantidad() { return cantidad; }
    public String getFecha() { return fecha; }
    public String getMotivo() { return motivo; }

    @Override
    public String toString() {
        try {
            return "[" + fecha + "] " + tipo + " | " + codigoProducto + " | Cant: " + cantidad +
                    (motivo.isEmpty() ? "" : " | Motivo: " + motivo);
        } catch (Exception e) {
            return "Movimiento inválido";
        }
    }
}