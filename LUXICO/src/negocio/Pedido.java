package negocio;

import java.io.Serializable;
import java.util.ArrayList;

public class Pedido implements Serializable {

    private int id;
    private Cliente cliente;
    private String fechaCreacion;
    private EstadoPedido estado;
    private ArrayList<DetallePedido> detalles;

    public Pedido(int id, Cliente cliente, String fechaCreacion) {
        this.id = id;
        this.cliente = cliente;
        this.fechaCreacion = fechaCreacion;
        this.estado = EstadoPedido.PENDIENTE;
        this.detalles = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    public ArrayList<DetallePedido> getDetalles() {
        return detalles;
    }

    public boolean agregarDetalle(DetallePedido det) {
        try {
            if (det == null) return false;
            for (int i = 0; i < detalles.size(); i++) {
                DetallePedido d = detalles.get(i);
                if (d.getCodigoProducto().equals(det.getCodigoProducto())) {
                    d.setCantidad(d.getCantidad() + det.getCantidad()); // requiere setCantidad en DetallePedido
                    return true;
                }
            }

            detalles.add(det);
            return true;

        } catch (Exception e) {
            return false;
        }
    }

    public double getTotal() {
        double total = 0;

        for (int i = 0; i < detalles.size(); i++) {
            DetallePedido d = detalles.get(i);
            total += d.getPrecioUnitario() * d.getCantidad();
        }

        return total;
    }

    @Override
    public String toString() {
        String nombre = (cliente != null) ? cliente.getNombre() : "N/A";
        String cedula = (cliente != null) ? cliente.getCedula() : "N/A";

        return "Pedido ID: " + id +
                " | Cliente: " + nombre + " (" + cedula + ")" +
                " | Fecha: " + fechaCreacion +
                " | Estado: " + estado +
                " | Total: $" + getTotal();
    }
}