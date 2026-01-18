package modelo;

import java.util.ArrayList;
import java.io.Serializable;

public class Pedido implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String cedulaCliente;
    private String nombreCliente;
    private String fechaCreacion;
    private EstadoPedido estado;
    private ArrayList<DetallePedido> detalles;
    private double total;

    public Pedido(int id, String cedulaCliente, String nombreCliente, String fechaCreacion) {
        try {
            if (id <= 0) throw new Exception();
            if (cedulaCliente == null || cedulaCliente.trim().isEmpty()) throw new Exception();
            if (nombreCliente == null || nombreCliente.trim().isEmpty()) throw new Exception();
            if (fechaCreacion == null || fechaCreacion.trim().isEmpty()) fechaCreacion = "SIN-FECHA";

            this.id = id;
            this.cedulaCliente = cedulaCliente.trim();
            this.nombreCliente = nombreCliente.trim();
            this.fechaCreacion = fechaCreacion.trim();
            this.estado = EstadoPedido.PENDIENTE;
            this.detalles = new ArrayList<>();
            this.total = 0;

        } catch (Exception e) {
            System.out.println("Error creando pedido: datos inválidos.");
            this.id = 0;
            this.cedulaCliente = "0000000000";
            this.nombreCliente = "SinCliente";
            this.fechaCreacion = "SIN-FECHA";
            this.estado = EstadoPedido.PENDIENTE;
            this.detalles = new ArrayList<>();
            this.total = 0;
        }
    }

    public int getId() { return id; }
    public String getCedulaCliente() { return cedulaCliente; }
    public String getNombreCliente() { return nombreCliente; }
    public String getFechaCreacion() { return fechaCreacion; }
    public EstadoPedido getEstado() { return estado; }
    public ArrayList<DetallePedido> getDetalles() { return detalles; }
    public double getTotal() { return total; }

    public boolean agregarDetalle(DetallePedido d) {
        try {
            if (d == null) throw new NullPointerException();
            if (estado != EstadoPedido.PENDIENTE) {
                System.out.println("No se puede modificar un pedido que no esté en PENDIENTE.");
                return false;
            }
            detalles.add(d);
            recalcularTotal();
            return true;

        } catch (NullPointerException npe) {
            System.out.println("Error: detalle no inicializado.");
            return false;
        } catch (Exception e) {
            System.out.println("Error agregando detalle al pedido.");
            return false;
        }
    }

    public void recalcularTotal() {
        try {
            double suma = 0;
            for (int i = 0; i < detalles.size(); i++) {
                suma += detalles.get(i).getSubtotal();
            }
            total = suma;
        } catch (Exception e) {
            total = 0;
        }
    }

    public boolean setEstado(EstadoPedido nuevo) {
        try {
            if (nuevo == null) throw new NullPointerException();
            this.estado = nuevo;
            return true;
        } catch (Exception e) {
            System.out.println("Error cambiando estado del pedido.");
            return false;
        }
    }

    @Override
    public String toString() {
        try {
            return "Pedido #" + id + " | Cliente: " + nombreCliente + " (" + cedulaCliente + ")" +
                    " | Fecha: " + fechaCreacion + " | Estado: " + estado + " | Total: " + total;
        } catch (Exception e) {
            return "Pedido inválido";
        }
    }
}