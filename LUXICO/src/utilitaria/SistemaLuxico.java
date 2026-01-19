package utilitaria;

import java.util.ArrayList;

import negocio.*;
import servicio.Storage;

public class SistemaLuxico {

    private Storage persistencia;
    private DatosLuxico datos;

    // Módulos
    private Autenticacion auth;
    private ClienteManager clientes;
    private ProductoManager productos;
    private PedidoManager pedidos;
    private ReporteManager reportes;

    public SistemaLuxico() {
        persistencia = new Storage();
        datos = persistencia.cargarTodo();

        auth = new Autenticacion(datos.usuarios);
        clientes = new ClienteManager(datos, persistencia);
        productos = new ProductoManager(datos, persistencia);

        pedidos = new PedidoManager(datos, persistencia);
        reportes = new ReporteManager(datos);
    }

    public void guardarTodo() {
        persistencia.guardarTodo(datos);
    }

    public void resetSistema() {
        persistencia.resetSistema(datos);

        auth = new Autenticacion(datos.usuarios);
        clientes = new ClienteManager(datos, persistencia);
        productos = new ProductoManager(datos, persistencia);
        pedidos = new PedidoManager(datos, persistencia);
        reportes = new ReporteManager(datos);
    }

    // =========================
    // LOGIN
    // =========================
    public Usuario login(String user, String pass) {
        return auth.login(user, pass);
    }

    // =========================
    // CLIENTES
    // =========================
    public Cliente buscarCliente(String cedula) {
        return clientes.buscarCliente(cedula);
    }

    public boolean registrarCliente(Cliente c) {
        return clientes.registrarCliente(c);
    }

    public void listarClientes() {
        clientes.listarClientes();
    }

    public void verDetalleCliente(String cedula) {
        clientes.verDetalleCliente(cedula);
    }

    public boolean editarCliente(String cedula, String nuevoNombre, String nuevaDireccion,
                                 String nuevoTelefono, String nuevoCorreo) {
        return clientes.editarCliente(cedula, nuevoNombre, nuevaDireccion, nuevoTelefono, nuevoCorreo);
    }

    public boolean eliminarCliente(String cedula) {
        return clientes.eliminarCliente(cedula);
    }

    // =========================
    // PRODUCTOS
    // =========================
    public boolean agregarProducto(Producto p) {
        return productos.agregarProducto(p);
    }

    public void listarProductos() {
        productos.listarProductos();
    }

    public void listarAlertasStockMinimo() {
        productos.listarAlertasStockMinimo();
    }

    public boolean eliminarProducto(Producto p) {
        return productos.eliminarProducto(p);
    }

    // =========================
    // MOVIMIENTOS
    // =========================
    public boolean registrarMovimiento(Producto producto, TipoMovimiento tipo,
                                       int cantidad, String fecha, String motivo) {
        return productos.registrarMovimiento(producto, tipo, cantidad, fecha, motivo);
    }

    public void listarMovimientos() {
        productos.listarMovimientos();
    }

    // =========================
    // PEDIDOS
    // =========================
    public Pedido buscarPedido(int id) {
        return pedidos.buscarPedido(id);
    }

    public int crearPedido(Cliente cliente, String fechaCreacion) {
        return pedidos.crearPedido(cliente, fechaCreacion);
    }

    public boolean agregarProductoAPedido(int idPedido, Producto producto, int cantidad) {
        return pedidos.agregarProductoAPedido(idPedido, producto, cantidad);
    }

    public void verDetallePedido(int idPedido) {
        pedidos.verDetallePedido(idPedido);
    }

    public void listarPedidos() {
        pedidos.listarPedidos();
    }

    public boolean confirmarPedido(int idPedido) {
        return pedidos.confirmarPedido(idPedido);
    }

    public boolean cambiarEstadoPedido(int idPedido, EstadoPedido nuevoEstado) {
        return pedidos.cambiarEstadoPedido(idPedido, nuevoEstado);
    }

    public boolean prepararPedido(int idPedido) {
        return pedidos.prepararPedido(idPedido);
    }

    // =========================
    // REPORTES
    // =========================
    public void reporteStockCritico() {
        reportes.reporteStockCritico();
    }

    public void reportePedidosPorEstado(EstadoPedido estado) {
        reportes.reportePedidosPorEstado(estado);
    }

    public void reporteVentasResumen() {
        reportes.reporteVentasResumen();
    }

    // =========================
    // GETTERS
    // =========================
    public ArrayList<Cliente> getClientes() {
        return new ArrayList<>(datos.clientes);
    }

    public ArrayList<Producto> getProductos() {
        return new ArrayList<>(datos.productos);
    }

    public ArrayList<Pedido> getPedidos() {
        return new ArrayList<>(datos.pedidos);
    }

    public ArrayList<MovimientoInventario> getMovimientos() {
        return new ArrayList<>(datos.movimientos);
    }
}