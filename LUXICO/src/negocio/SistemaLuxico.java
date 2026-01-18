package negocio;

import modelo.*;
import java.util.ArrayList;
import servicio.GestorArchivos;

public class SistemaLuxico {
    private GestorArchivos gestor;

    private final String RUTA_USUARIOS = "usuarios.dat";
    private final String RUTA_PRODUCTOS = "productos.dat";
    private final String RUTA_CLIENTES = "clientes.dat";
    private final String RUTA_PEDIDOS = "pedidos.dat";
    private final String RUTA_MOVIMIENTOS = "movimientos.dat";
    private final String RUTA_CONTADOR = "contador.dat";

    private ArrayList<Usuario> usuarios;
    private ArrayList<Producto> productos;
    private ArrayList<MovimientoInventario> movimientos;
    private ArrayList<Cliente> clientes;
    private ArrayList<Pedido> pedidos;
    private int contadorPedidos;

    public SistemaLuxico() {
        try {
            gestor = new GestorArchivos();

            Object objU = gestor.cargarObjeto(RUTA_USUARIOS);
            if (objU != null) usuarios = (ArrayList<Usuario>) objU;
            else usuarios = new ArrayList<>();

            Object objP = gestor.cargarObjeto(RUTA_PRODUCTOS);
            if (objP != null) productos = (ArrayList<Producto>) objP;
            else productos = new ArrayList<>();

            Object objC = gestor.cargarObjeto(RUTA_CLIENTES);
            if (objC != null) clientes = (ArrayList<Cliente>) objC;
            else clientes = new ArrayList<>();

            Object objPe = gestor.cargarObjeto(RUTA_PEDIDOS);
            if (objPe != null) pedidos = (ArrayList<Pedido>) objPe;
            else pedidos = new ArrayList<>();

            Object objM = gestor.cargarObjeto(RUTA_MOVIMIENTOS);
            if (objM != null) movimientos = (ArrayList<MovimientoInventario>) objM;
            else movimientos = new ArrayList<>();

            Object objCont = gestor.cargarObjeto(RUTA_CONTADOR);
            if (objCont != null) contadorPedidos = (Integer) objCont;
            else contadorPedidos = 1;

            // Si es primera vez (no hay usuarios), crea defaults
            if (usuarios.size() == 0) {
                usuarios.add(new Admin("admin", "admin123", "Administrador"));
                usuarios.add(new Bodega("bodega", "bodega123", "Usuario Bodega"));
                usuarios.add(new Ventas("ventas", "ventas123", "Usuario Ventas"));
                guardarTodo(); // ✅ guarda primera vez
            }

        } catch (Exception e) {
            System.out.println("Error inicializando sistema con archivos, se inicia vacío.");

            // ✅ IMPORTANTE: para que guardarTodo() nunca falle por gestor null
            gestor = new GestorArchivos();

            usuarios = new ArrayList<>();
            productos = new ArrayList<>();
            clientes = new ArrayList<>();
            pedidos = new ArrayList<>();
            movimientos = new ArrayList<>();
            contadorPedidos = 1;
        }
    }

    public void guardarTodo() {
        try {
            gestor.guardarObjeto(RUTA_USUARIOS, usuarios);
            gestor.guardarObjeto(RUTA_PRODUCTOS, productos);
            gestor.guardarObjeto(RUTA_CLIENTES, clientes);
            gestor.guardarObjeto(RUTA_PEDIDOS, pedidos);
            gestor.guardarObjeto(RUTA_MOVIMIENTOS, movimientos);
            gestor.guardarObjeto(RUTA_CONTADOR, contadorPedidos);
        } catch (Exception e) {
            System.out.println("Error guardando todos los datos.");
        }
    }

    public void resetSistema() {
        try {
            gestor.eliminarArchivo(RUTA_USUARIOS);
            gestor.eliminarArchivo(RUTA_PRODUCTOS);
            gestor.eliminarArchivo(RUTA_CLIENTES);
            gestor.eliminarArchivo(RUTA_PEDIDOS);
            gestor.eliminarArchivo(RUTA_MOVIMIENTOS);
            gestor.eliminarArchivo(RUTA_CONTADOR);

            // Reiniciar listas en memoria
            usuarios = new ArrayList<>();
            productos = new ArrayList<>();
            clientes = new ArrayList<>();
            pedidos = new ArrayList<>();
            movimientos = new ArrayList<>();
            contadorPedidos = 1;

            // Crear usuarios por defecto
            usuarios.add(new Admin("admin", "admin123", "Administrador"));
            usuarios.add(new Bodega("bodega", "bodega123", "Usuario Bodega"));
            usuarios.add(new Ventas("ventas", "ventas123", "Usuario Ventas"));

            guardarTodo();

            System.out.println("Sistema reiniciado. Datos borrados y usuarios por defecto creados.");

        } catch (Exception e) {
            System.out.println("Error reiniciando el sistema.");
        }
    }

    // ------------------ LOGIN ------------------
    public Usuario login(String user, String pass) {
        try {
            if (user == null || pass == null) throw new Exception();

            for (int i = 0; i < usuarios.size(); i++) {
                Usuario u = usuarios.get(i);
                if (u.getUsuario().equals(user) && u.getClave().equals(pass)) {
                    return u;
                }
            }
            System.out.println("Credenciales incorrectas o usuario no encontrado.");
            return null;

        } catch (Exception e) {
            System.out.println("Error en login: datos inválidos.");
            return null;
        }
    }

    // ------------------ PRODUCTOS ------------------
    public boolean agregarProducto(Producto p) {
        try {
            if (p == null) throw new NullPointerException();

            for (int i = 0; i < productos.size(); i++) {
                if (productos.get(i).getCodigo().equals(p.getCodigo())) {
                    System.out.println("Error: ya existe un producto con ese código.");
                    return false;
                }
            }

            productos.add(p);
            guardarTodo(); // ✅
            System.out.println("Producto registrado correctamente.");
            return true;

        } catch (NullPointerException npe) {
            System.out.println("Error: producto no inicializado.");
            return false;
        } catch (Exception e) {
            System.out.println("Error registrando producto.");
            return false;
        }
    }

    public void listarProductos() {
        try {
            if (productos.size() == 0) {
                System.out.println("No hay productos registrados.");
                return;
            }
            for (int i = 0; i < productos.size(); i++) {
                Producto p = productos.get(i);
                System.out.println((i + 1) + ") " + p.getCodigo() + " | " + p.getModelo()
                        + " | Talla: " + p.getTalla()
                        + " | Stock: " + p.getStock()
                        + " | Tipo: " + p.getTipo());
            }
        } catch (Exception e) {
            System.out.println("Error al listar productos.");
        }
    }

    public Producto buscarProducto(String codigo) {
        try {
            if (codigo == null || codigo.trim().isEmpty()) throw new Exception();
            codigo = codigo.trim().toUpperCase();

            for (int i = 0; i < productos.size(); i++) {
                if (productos.get(i).getCodigo().equals(codigo)) {
                    return productos.get(i);
                }
            }
            return null;

        } catch (Exception e) {
            System.out.println("Código inválido.");
            return null;
        }
    }

    // ------------------ INVENTARIO (MOVIMIENTOS) ------------------
    public boolean registrarMovimiento(String codigoProducto, TipoMovimiento tipo, int cantidad, String fecha, String motivo) {
        try {
            if (codigoProducto == null || codigoProducto.trim().isEmpty()) throw new Exception();
            if (tipo == null) throw new NullPointerException();
            if (cantidad <= 0) throw new Exception();

            Producto p = buscarProducto(codigoProducto);
            if (p == null) {
                System.out.println("Error: producto no encontrado.");
                return false;
            }

            boolean ok = false;

            if (tipo == TipoMovimiento.ENTRADA) {
                ok = p.aumentarStock(cantidad);
            } else if (tipo == TipoMovimiento.SALIDA) {
                ok = p.descontarStock(cantidad);
            } else if (tipo == TipoMovimiento.DEVOLUCION) {
                ok = p.aumentarStock(cantidad);
            }

            if (!ok) {
                System.out.println("No se pudo registrar el movimiento (revise stock/cantidad).");
                return false;
            }

            MovimientoInventario mov = new MovimientoInventario(codigoProducto, tipo, cantidad, fecha, motivo);
            movimientos.add(mov);

            guardarTodo(); // ✅
            System.out.println("Movimiento registrado correctamente.");
            return true;

        } catch (NullPointerException npe) {
            System.out.println("Error: tipo de movimiento no válido.");
            return false;
        } catch (Exception e) {
            System.out.println("Error registrando movimiento: datos inválidos.");
            return false;
        }
    }

    public void listarMovimientos() {
        try {
            if (movimientos.size() == 0) {
                System.out.println("No hay movimientos registrados.");
                return;
            }
            System.out.println("=== HISTORIAL DE MOVIMIENTOS ===");
            for (int i = 0; i < movimientos.size(); i++) {
                System.out.println((i + 1) + ") " + movimientos.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error al listar movimientos.");
        }
    }

    public void verDetalleProducto(String codigo) {
        try {
            Producto p = buscarProducto(codigo);
            if (p == null) {
                System.out.println("Producto no encontrado.");
                return;
            }

            System.out.println("=== DETALLE PRODUCTO ===");
            System.out.println("Código: " + p.getCodigo());
            System.out.println("Modelo: " + p.getModelo());
            System.out.println("Talla: " + p.getTalla());
            System.out.println("Tipo: " + p.getTipo());
            System.out.println("Precio: " + p.getPrecio());
            System.out.println("Stock: " + p.getStock());
            System.out.println("Bajo mínimo: " + (p.estaBajoMinimo() ? "SI" : "NO"));

        } catch (Exception e) {
            System.out.println("Error mostrando detalle del producto.");
        }
    }

    public boolean editarProducto(String codigo, String nuevoModelo, String nuevaTalla, double nuevoPrecio, int nuevoStockMin) {
        try {
            Producto p = buscarProducto(codigo);
            if (p == null) {
                System.out.println("Producto no encontrado.");
                return false;
            }

            boolean ok = true;

            if (nuevoModelo != null && !nuevoModelo.trim().isEmpty()) ok = p.setModelo(nuevoModelo) && ok;
            if (nuevaTalla != null && !nuevaTalla.trim().isEmpty()) ok = p.setTalla(nuevaTalla) && ok;
            if (nuevoPrecio > 0) ok = p.setPrecio(nuevoPrecio) && ok;
            if (nuevoStockMin >= 0) ok = p.setStockMin(nuevoStockMin) && ok;

            guardarTodo(); // ✅ (guardas cambios aunque sean parciales)

            if (ok) System.out.println("Producto actualizado correctamente.");
            else System.out.println("Producto actualizado parcialmente (hubo datos inválidos).");

            return ok;

        } catch (Exception e) {
            System.out.println("Error editando producto.");
            return false;
        }
    }

    public boolean eliminarProducto(String codigo) {
        try {
            if (codigo == null || codigo.trim().isEmpty()) throw new Exception();
            codigo = codigo.trim().toUpperCase();

            for (int i = 0; i < productos.size(); i++) {
                if (productos.get(i).getCodigo().equals(codigo)) {
                    productos.remove(i);
                    guardarTodo(); // ✅
                    System.out.println("Producto eliminado correctamente.");
                    return true;
                }
            }

            System.out.println("Producto no encontrado.");
            return false;

        } catch (Exception e) {
            System.out.println("Error eliminando producto.");
            return false;
        }
    }

    public void listarAlertasStockMinimo() {
        try {
            if (productos.size() == 0) {
                System.out.println("No hay productos registrados.");
                return;
            }

            boolean hayAlertas = false;
            System.out.println("=== ALERTAS STOCK MÍNIMO ===");

            for (int i = 0; i < productos.size(); i++) {
                Producto p = productos.get(i);
                if (p.estaBajoMinimo()) {
                    hayAlertas = true;
                    System.out.println("- " + p.getCodigo() + " | " + p.getModelo()
                            + " | Stock: " + p.getStock()
                            + " | Tipo: " + p.getTipo());
                }
            }

            if (!hayAlertas) {
                System.out.println("No hay productos en nivel crítico.");
            }

        } catch (Exception e) {
            System.out.println("Error al generar alertas de stock mínimo.");
        }
    }

    // ------------------ CLIENTES ------------------
    public Cliente buscarCliente(String cedula) {
        try {
            if (cedula == null || cedula.trim().isEmpty()) throw new Exception();
            cedula = cedula.trim();

            for (int i = 0; i < clientes.size(); i++) {
                if (clientes.get(i).getCedula().equals(cedula)) {
                    return clientes.get(i);
                }
            }
            return null;

        } catch (Exception e) {
            System.out.println("Cédula/ID inválido.");
            return null;
        }
    }

    public boolean registrarCliente(Cliente c) {
        try {
            if (c == null) throw new NullPointerException();

            if (!c.esValido()) {
                System.out.println("Error: cliente con datos inválidos. No se registró.");
                return false;
            }

            if (buscarCliente(c.getCedula()) != null) {
                System.out.println("Error: ya existe un cliente con esa cédula/ID.");
                return false;
            }

            clientes.add(c);
            guardarTodo();
            System.out.println("Cliente registrado correctamente.");
            return true;

        } catch (NullPointerException npe) {
            System.out.println("Error: cliente no inicializado.");
            return false;
        } catch (Exception e) {
            System.out.println("Error registrando cliente.");
            return false;
        }
    }

    public void listarClientes() {
        try {
            if (clientes.size() == 0) {
                System.out.println("No hay clientes registrados.");
                return;
            }

            System.out.println("=== LISTA DE CLIENTES ===");
            for (int i = 0; i < clientes.size(); i++) {
                System.out.println((i + 1) + ") " + clientes.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error al listar clientes.");
        }
    }

    public void verDetalleCliente(String cedula) {
        try {
            Cliente c = buscarCliente(cedula);
            if (c == null) {
                System.out.println("Cliente no encontrado.");
                return;
            }

            System.out.println("=== DETALLE CLIENTE ===");
            System.out.println("Cédula/ID: " + c.getCedula());
            System.out.println("Nombre: " + c.getNombre());
            System.out.println("Dirección: " + c.getDireccion());
            System.out.println("Teléfono: " + c.getTelefono());
            System.out.println("Correo: " + c.getCorreo());

        } catch (Exception e) {
            System.out.println("Error mostrando detalle del cliente.");
        }
    }

    public boolean editarCliente(String cedula, String nuevoNombre, String nuevaDireccion, String nuevoTelefono, String nuevoCorreo) {
        try {
            Cliente c = buscarCliente(cedula);
            if (c == null) {
                System.out.println("Cliente no encontrado.");
                return false;
            }

            boolean ok = true;

            if (nuevoNombre != null && !nuevoNombre.trim().isEmpty()) ok = c.setNombre(nuevoNombre) && ok;
            if (nuevaDireccion != null && !nuevaDireccion.trim().isEmpty()) ok = c.setDireccion(nuevaDireccion) && ok;
            if (nuevoTelefono != null && !nuevoTelefono.trim().isEmpty()) ok = c.setTelefono(nuevoTelefono) && ok;
            if (nuevoCorreo != null && !nuevoCorreo.trim().isEmpty()) ok = c.setCorreo(nuevoCorreo) && ok;

            guardarTodo(); // ✅

            if (ok) System.out.println("Cliente actualizado correctamente.");
            else System.out.println("Cliente actualizado parcialmente (hubo datos inválidos).");

            return ok;

        } catch (Exception e) {
            System.out.println("Error editando cliente.");
            return false;
        }
    }

    public boolean eliminarCliente(String cedula) {
        try {
            if (cedula == null || cedula.trim().isEmpty()) throw new Exception();
            cedula = cedula.trim();

            for (int i = 0; i < clientes.size(); i++) {
                if (clientes.get(i).getCedula().equals(cedula)) {
                    clientes.remove(i);
                    guardarTodo(); // ✅
                    System.out.println("Cliente eliminado correctamente.");
                    return true;
                }
            }

            System.out.println("Cliente no encontrado.");
            return false;

        } catch (Exception e) {
            System.out.println("Error eliminando cliente.");
            return false;
        }
    }

    // ------------------ PEDIDOS ------------------
    public Pedido buscarPedido(int id) {
        try {
            if (id <= 0) throw new Exception();
            for (int i = 0; i < pedidos.size(); i++) {
                if (pedidos.get(i).getId() == id) return pedidos.get(i);
            }
            return null;
        } catch (Exception e) {
            System.out.println("ID de pedido inválido.");
            return null;
        }
    }

    public int crearPedido(String cedulaCliente, String fechaCreacion) {
        try {
            if (cedulaCliente == null || cedulaCliente.trim().isEmpty()) throw new Exception();

            Cliente c = buscarCliente(cedulaCliente);
            if (c == null) {
                System.out.println("No se puede crear pedido: cliente no encontrado.");
                return -1;
            }

            int id = contadorPedidos;
            contadorPedidos++;

            Pedido p = new Pedido(id, c.getCedula(), c.getNombre(), fechaCreacion);
            pedidos.add(p);

            guardarTodo(); // ✅ (guarda pedidos + contador)
            System.out.println("Pedido creado correctamente. ID: " + id);
            return id;

        } catch (Exception e) {
            System.out.println("Error creando pedido.");
            return -1;
        }
    }

    public boolean agregarProductoAPedido(int idPedido, String codigoProducto, int cantidad) {
        try {
            Pedido ped = buscarPedido(idPedido);
            if (ped == null) {
                System.out.println("Pedido no encontrado.");
                return false;
            }

            if (ped.getEstado() != EstadoPedido.PENDIENTE) {
                System.out.println("Solo se pueden agregar productos si el pedido está en PENDIENTE.");
                return false;
            }

            if (cantidad <= 0) {
                System.out.println("Cantidad inválida.");
                return false;
            }

            Producto prod = buscarProducto(codigoProducto);
            if (prod == null) {
                System.out.println("Producto no encontrado.");
                return false;
            }

            DetallePedido det = new DetallePedido(prod.getCodigo(), prod.getModelo(), prod.getTalla(), prod.getPrecio(), cantidad);
            boolean ok = ped.agregarDetalle(det);

            if (ok) {
                System.out.println("Producto agregado al pedido.");
                guardarTodo(); // ✅
            }
            return ok;

        } catch (Exception e) {
            System.out.println("Error agregando producto al pedido.");
            return false;
        }
    }

    public void verDetallePedido(int idPedido) {
        try {
            Pedido ped = buscarPedido(idPedido);
            if (ped == null) {
                System.out.println("Pedido no encontrado.");
                return;
            }

            System.out.println("=== DETALLE DE PEDIDO ===");
            System.out.println(ped.toString());
            System.out.println("--- Items ---");

            if (ped.getDetalles().size() == 0) {
                System.out.println("El pedido no tiene productos.");
                return;
            }

            for (int i = 0; i < ped.getDetalles().size(); i++) {
                System.out.println((i + 1) + ") " + ped.getDetalles().get(i));
            }

        } catch (Exception e) {
            System.out.println("Error mostrando detalle del pedido.");
        }
    }

    public void listarPedidos() {
        try {
            if (pedidos.size() == 0) {
                System.out.println("No hay pedidos registrados.");
                return;
            }

            System.out.println("=== LISTA DE PEDIDOS ===");
            for (int i = 0; i < pedidos.size(); i++) {
                System.out.println((i + 1) + ") " + pedidos.get(i));
            }

        } catch (Exception e) {
            System.out.println("Error listando pedidos.");
        }
    }

    public boolean confirmarPedido(int idPedido) {
        try {
            Pedido ped = buscarPedido(idPedido);
            if (ped == null) {
                System.out.println("Pedido no encontrado.");
                return false;
            }

            if (ped.getEstado() != EstadoPedido.PENDIENTE) {
                System.out.println("Solo se puede confirmar un pedido en estado PENDIENTE.");
                return false;
            }

            if (ped.getDetalles().size() == 0) {
                System.out.println("No se puede confirmar: el pedido no tiene productos.");
                return false;
            }

            for (int i = 0; i < ped.getDetalles().size(); i++) {
                DetallePedido d = ped.getDetalles().get(i);
                Producto prod = buscarProducto(d.getCodigoProducto());

                if (prod == null) {
                    System.out.println("No se puede confirmar: un producto ya no existe (" + d.getCodigoProducto() + ").");
                    return false;
                }

                if (d.getCantidad() > prod.getStock()) {
                    System.out.println("No se puede confirmar: stock insuficiente para " + d.getCodigoProducto() +
                            " (Stock: " + prod.getStock() + ", Pedido: " + d.getCantidad() + ")");
                    return false;
                }
            }

            for (int i = 0; i < ped.getDetalles().size(); i++) {
                DetallePedido d = ped.getDetalles().get(i);
                Producto prod = buscarProducto(d.getCodigoProducto());
                prod.descontarStock(d.getCantidad());
            }

            ped.setEstado(EstadoPedido.CONFIRMADO);

            guardarTodo(); // ✅ (stock + estado + todo)
            System.out.println("Pedido confirmado correctamente. Stock actualizado.");
            return true;

        } catch (Exception e) {
            System.out.println("Error confirmando pedido.");
            return false;
        }
    }

    public boolean cambiarEstadoPedido(int idPedido, EstadoPedido nuevoEstado) {
        try {
            Pedido ped = buscarPedido(idPedido);
            if (ped == null) {
                System.out.println("Pedido no encontrado.");
                return false;
            }

            if (nuevoEstado == null) {
                System.out.println("Estado inválido.");
                return false;
            }

            EstadoPedido actual = ped.getEstado();

            if (actual == EstadoPedido.PENDIENTE) {
                System.out.println("No se puede cambiar estado desde PENDIENTE manualmente. Use Confirmar Pedido.");
                return false;
            }

            if (actual == EstadoPedido.CONFIRMADO && nuevoEstado != EstadoPedido.EN_PREPARACION) {
                System.out.println("Transición inválida. De CONFIRMADO solo pasa a EN_PREPARACION.");
                return false;
            }

            if (actual == EstadoPedido.EN_PREPARACION && nuevoEstado != EstadoPedido.ENVIADO) {
                System.out.println("Transición inválida. De EN_PREPARACION solo pasa a ENVIADO.");
                return false;
            }

            if (actual == EstadoPedido.ENVIADO) {
                System.out.println("El pedido ya está ENVIADO. No se puede cambiar.");
                return false;
            }

            ped.setEstado(nuevoEstado);
            guardarTodo(); // ✅
            System.out.println("Estado actualizado correctamente.");
            return true;

        } catch (Exception e) {
            System.out.println("Error cambiando estado del pedido.");
            return false;
        }
    }

    // ------------------ REPORTES / BODEGA ------------------
    public void reporteStockCritico() {
        try {
            if (productos.size() == 0) {
                System.out.println("No hay productos registrados.");
                return;
            }

            System.out.println("=== REPORTE: STOCK CRÍTICO ===");
            boolean hay = false;

            for (int i = 0; i < productos.size(); i++) {
                Producto p = productos.get(i);
                if (p.estaBajoMinimo()) {
                    hay = true;
                    System.out.println("- " + p.getCodigo() + " | " + p.getModelo()
                            + " | Talla: " + p.getTalla()
                            + " | Stock: " + p.getStock()
                            + " | Tipo: " + p.getTipo());
                }
            }

            if (!hay) {
                System.out.println("No hay productos en nivel crítico.");
            }

        } catch (Exception e) {
            System.out.println("Error generando reporte de stock crítico.");
        }
    }

    public void reportePedidosPorEstado(EstadoPedido estado) {
        try {
            if (estado == null) {
                System.out.println("Estado inválido.");
                return;
            }

            if (pedidos.size() == 0) {
                System.out.println("No hay pedidos registrados.");
                return;
            }

            System.out.println("=== REPORTE: PEDIDOS EN ESTADO " + estado + " ===");
            boolean hay = false;

            for (int i = 0; i < pedidos.size(); i++) {
                Pedido p = pedidos.get(i);
                if (p.getEstado() == estado) {
                    hay = true;
                    System.out.println(p);
                }
            }

            if (!hay) {
                System.out.println("No existen pedidos en ese estado.");
            }

        } catch (Exception e) {
            System.out.println("Error generando reporte de pedidos por estado.");
        }
    }

    public void reporteVentasResumen() {
        try {
            if (pedidos.size() == 0) {
                System.out.println("No hay pedidos registrados.");
                return;
            }

            double totalVendido = 0;
            String[] codigos = new String[200];
            int[] cantidades = new int[200];
            int n = 0;

            for (int i = 0; i < pedidos.size(); i++) {
                Pedido p = pedidos.get(i);

                if (p.getEstado() == EstadoPedido.PENDIENTE) continue;

                totalVendido += p.getTotal();

                for (int j = 0; j < p.getDetalles().size(); j++) {
                    DetallePedido d = p.getDetalles().get(j);

                    int idx = -1;
                    for (int k = 0; k < n; k++) {
                        if (codigos[k].equals(d.getCodigoProducto())) {
                            idx = k;
                            break;
                        }
                    }

                    if (idx == -1) {
                        codigos[n] = d.getCodigoProducto();
                        cantidades[n] = d.getCantidad();
                        n++;
                    } else {
                        cantidades[idx] += d.getCantidad();
                    }
                }
            }

            System.out.println("=== REPORTE: RESUMEN DE VENTAS ===");
            System.out.println("Total vendido (sin pedidos pendientes): $" + totalVendido);

            if (n == 0) {
                System.out.println("No hay productos vendidos aún.");
                return;
            }

            int max = cantidades[0];
            int pos = 0;
            for (int i = 1; i < n; i++) {
                if (cantidades[i] > max) {
                    max = cantidades[i];
                    pos = i;
                }
            }

            System.out.println("Producto más vendido: " + codigos[pos] + " | Cantidad: " + max);

            System.out.println("--- Detalle por producto vendido ---");
            for (int i = 0; i < n; i++) {
                System.out.println("- " + codigos[i] + " | Cantidad vendida: " + cantidades[i]);
            }

        } catch (Exception e) {
            System.out.println("Error generando reporte de ventas.");
        }
    }

    public void listarPedidosConfirmados() {
        try {
            if (pedidos.size() == 0) {
                System.out.println("No hay pedidos registrados.");
                return;
            }

            System.out.println("=== PEDIDOS CONFIRMADOS (PARA BODEGA) ===");
            boolean hay = false;

            for (int i = 0; i < pedidos.size(); i++) {
                Pedido p = pedidos.get(i);
                if (p.getEstado() == EstadoPedido.CONFIRMADO) {
                    hay = true;
                    System.out.println(p);
                }
            }

            if (!hay) {
                System.out.println("No hay pedidos confirmados.");
            }

        } catch (Exception e) {
            System.out.println("Error listando pedidos confirmados.");
        }
    }

    public boolean prepararPedido(int idPedido) {
        try {
            Pedido p = buscarPedido(idPedido);
            if (p == null) {
                System.out.println("Pedido no encontrado.");
                return false;
            }

            if (p.getEstado() != EstadoPedido.CONFIRMADO) {
                System.out.println("Solo se puede preparar un pedido en estado CONFIRMADO.");
                return false;
            }

            p.setEstado(EstadoPedido.EN_PREPARACION);
            guardarTodo(); // ✅
            System.out.println("Pedido pasó a EN_PREPARACION.");
            return true;

        } catch (Exception e) {
            System.out.println("Error al preparar pedido.");
            return false;
        }
    }

    public ArrayList<Producto> getProductos() {
        try { return new ArrayList<>(productos); }
        catch (Exception e) { return new ArrayList<>(); }
    }

    public ArrayList<Cliente> getClientes() {
        try { return new ArrayList<>(clientes); }
        catch (Exception e) { return new ArrayList<>(); }
    }

    public ArrayList<Pedido> getPedidos() {
        try { return new ArrayList<>(pedidos); }
        catch (Exception e) { return new ArrayList<>(); }
    }

    public ArrayList<MovimientoInventario> getMovimientos() {
        try { return new ArrayList<>(movimientos); }
        catch (Exception e) { return new ArrayList<>(); }
    }
}