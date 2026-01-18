package vista;

import modelo.*;
import negocio.SistemaLuxico;

public class Menu {
    private SistemaLuxico sistema;
    private Input in;

    public Menu(SistemaLuxico sistema) {
        this.sistema = sistema;
        this.in = new Input();
    }

    public void iniciar() {
        try {
            System.out.println("=== LUXICO ===");
            String user = in.leerTexto("Usuario: ");
            String pass = in.leerTexto("Clave: ");

            Usuario u = sistema.login(user, pass);
            if (u == null) return;

            if (u.getRol() == RolUsuario.ADMIN) menuAdmin();
            else if (u.getRol() == RolUsuario.BODEGA) menuBodega();
            else menuVentas();

        } catch (Exception e) {
            System.out.println("Error general en el sistema.");
        }
    }

    private void registrarMovimiento() {
        try {
            String cod = in.leerTexto("Código del producto: ").trim().toUpperCase();

            // ✅ Validar ANTES de pedir más datos
            if (sistema.buscarProducto(cod) == null) {
                System.out.println("No se encontró un producto con el código: " + cod);
                return;
            }

            int tipo = in.leerEntero("Tipo (1=Entrada, 2=Salida, 3=Devolución): ");
            int cantidad = in.leerEntero("Cantidad: ");

            // Validaciones rápidas
            if (cantidad <= 0) {
                System.out.println("Cantidad inválida.");
                return;
            }

            String fecha = in.leerTexto("Fecha (dd/mm/aaaa): ");
            String motivo = in.leerTexto("Motivo (opcional): ");

            TipoMovimiento t;
            if (tipo == 1) t = TipoMovimiento.ENTRADA;
            else if (tipo == 2) t = TipoMovimiento.SALIDA;
            else if (tipo == 3) t = TipoMovimiento.DEVOLUCION;
            else {
                System.out.println("Tipo inválido.");
                return;
            }

            sistema.registrarMovimiento(cod, t, cantidad, fecha, motivo);

        } catch (Exception e) {
            System.out.println("Error registrando movimiento.");
        }
    }

    private void registrarProducto() {
        try {
            String codigo = in.leerTexto("Código: ");
            String modelo = in.leerTexto("Modelo: ");
            String talla = in.leerTexto("Talla: ");
            double precio = in.leerDouble("Precio: ");
            int stock = in.leerEntero("Stock inicial: ");
            int stockMin = in.leerEntero("Stock mínimo: ");

            int tipo = in.leerEntero("Tipo (1=Regular, 2=Edición Limitada): ");

            if (precio <= 0 || stock < 0 || stockMin < 0) {
                System.out.println("Datos inválidos. No se registró producto.");
                return;
            }

            if (tipo == 2) {
                int serie = in.leerEntero("Número de serie: ");
                Producto p = new ProductoEdicionLimitada(codigo, modelo, talla, precio, stock, stockMin, serie);
                sistema.agregarProducto(p);
            } else {
                Producto p = new ProductoRegular(codigo, modelo, talla, precio, stock, stockMin);
                sistema.agregarProducto(p);
            }

        } catch (Exception e) {
            System.out.println("Error registrando producto.");
        }
    }

    private void verDetalle() {
        try {
            String cod = in.leerTexto("Código del producto: ").trim().toUpperCase();

            if (sistema.buscarProducto(cod) == null) {
                System.out.println("No se encontró un producto con el código: " + cod);
                return;
            }

            sistema.verDetalleProducto(cod);

        } catch (Exception e) {
            System.out.println("Error viendo detalle.");
        }
    }

    private void editarProducto() {
        try {
            String cod = in.leerTexto("Código del producto a editar: ").trim().toUpperCase();

            //  Validación ANTES de pedir los nuevos datos
            if (sistema.buscarProducto(cod) == null) {
                System.out.println("No se encontró un producto con el código: " + cod);
                return;
            }

            System.out.println("Deja vacío lo que NO quieras cambiar.");
            String nuevoModelo = in.leerTexto("Nuevo modelo: ");
            String nuevaTalla  = in.leerTexto("Nueva talla: ");

            double nuevoPrecio = in.leerDouble("Nuevo precio (0 para no cambiar): ");
            int nuevoStockMin  = in.leerEntero("Nuevo stock mínimo (-1 para no cambiar): ");

            // Normalizar entradas
            if (nuevoPrecio < 0) nuevoPrecio = 0;
            if (nuevoStockMin < -1) nuevoStockMin = -1;

            sistema.editarProducto(cod, nuevoModelo, nuevaTalla, nuevoPrecio, nuevoStockMin);

        } catch (Exception e) {
            System.out.println("Error editando producto.");
        }
    }

    private void eliminarProducto() {
        try {
            String cod = in.leerTexto("Código del producto a eliminar: ").trim().toUpperCase();

            // ✅ Validar ANTES de confirmar
            if (sistema.buscarProducto(cod) == null) {
                System.out.println("No se encontró un producto con el código: " + cod);
                return;
            }

            String conf = in.leerTexto("¿Seguro que deseas eliminarlo? (S/N): ");

            if (conf.equalsIgnoreCase("S")) {
                sistema.eliminarProducto(cod);
            } else {
                System.out.println("Eliminación cancelada.");
            }

        } catch (Exception e) {
            System.out.println("Error eliminando producto.");
        }
    }



    private void menuBodega() {
        int op;
        do {
            System.out.println("\n--- MENÚ BODEGA (INVENTARIO) ---");
            System.out.println("1. Registrar producto");
            System.out.println("2. Listar productos");
            System.out.println("3. Ver detalle de un producto");
            System.out.println("4. Editar producto");
            System.out.println("5. Eliminar producto");
            System.out.println("6. Registrar movimiento (entrada/salida/devolución)");
            System.out.println("7. Ver historial de movimientos");
            System.out.println("8. Ver alertas de stock mínimo");
            System.out.println("9. Ver pedidos confirmados");
            System.out.println("10. Marcar pedido como EN PREPARACIÓN");
            System.out.println("0. Volver");
            op = in.leerEntero("Opción: ");

            try {
                if (op == 1) registrarProducto();
                else if (op == 2) sistema.listarProductos();
                else if (op == 3) verDetalle();
                else if (op == 4) editarProducto();
                else if (op == 5) eliminarProducto();
                else if (op == 6) registrarMovimiento();
                else if (op == 7) sistema.listarMovimientos();
                else if (op == 8) sistema.listarAlertasStockMinimo();
                else if (op == 9) sistema.listarPedidosConfirmados();
                else if (op == 10) prepararPedidoBodega();
            } catch (Exception e) {
                System.out.println("Error en menú bodega.");
            }

        } while (op != 0);
    }

    private void menuAdmin() {
        int op;
        do {
            System.out.println("\n===== MENÚ ADMIN (LUXICO) =====");
            System.out.println("1. Ir a menú Bodega (Inventario)");
            System.out.println("2. Ir a menú Ventas (Clientes/Pedidos)");
            System.out.println("3. Listar productos");
            System.out.println("4. Listar clientes");
            System.out.println("5. Listar pedidos");
            System.out.println("6. Ver detalle de pedido");
            System.out.println("7. Confirmar pedido (descuenta stock)");
            System.out.println("8. Cambiar estado de pedido");
            System.out.println("9. REPORTES");
            System.out.println("10. Reiniciar sistema (borrar .dat)");
            System.out.println("0. Cerrar sesión");
            op = in.leerEntero("Opción: ");

            try {
                if (op == 1) {
                    menuBodega();
                } else if (op == 2) {
                    menuVentas();
                } else if (op == 3) {
                    sistema.listarProductos();
                } else if (op == 4) {
                    sistema.listarClientes();
                } else if (op == 5) {
                    sistema.listarPedidos();
                } else if (op == 6) {
                    verDetallePedidoAdmin();
                } else if (op == 7) {
                    confirmarPedidoAdmin();
                } else if (op == 8) {
                    cambiarEstadoPedidoAdmin();
                } else if (op == 9) {
                    menuReportesAdmin();
                } else if (op == 10) {
                    sistema.resetSistema();
                }
            } catch (Exception e) {
                System.out.println("Error en menú admin.");
            }

        } while (op != 0);
    }


    private void menuVentas() {
        int op;
        do {
            System.out.println("\n--- MENÚ VENTAS ---");
            System.out.println("1. Registrar cliente");
            System.out.println("2. Listar clientes");
            System.out.println("3. Ver detalle de cliente");
            System.out.println("4. Editar cliente");
            System.out.println("5. Eliminar cliente");
            System.out.println("6. Crear pedido");
            System.out.println("7. Agregar producto a pedido");
            System.out.println("8. Ver detalle de pedido");
            System.out.println("9. Confirmar pedido (descuenta stock)");
            System.out.println("10. Cambiar estado del pedido");
            System.out.println("11. Listar pedidos");
            System.out.println("12. Reporte stock crítico");
            System.out.println("13. Reporte pedidos por estado");
            System.out.println("14. Reporte resumen ventass");

            System.out.println("0. Volver");
            op = in.leerEntero("Opción: ");

            try {
                if (op == 1) registrarCliente();
                else if (op == 2) sistema.listarClientes();
                else if (op == 3) verDetalleCliente();
                else if (op == 4) editarCliente();
                else if (op == 5) eliminarCliente();
                else if (op == 6) crearPedido();
                else if (op == 7) agregarProductoAPedido();
                else if (op == 8) verDetallePedido();
                else if (op == 9) confirmarPedido();
                else if (op == 10) cambiarEstadoPedido();
                else if (op == 11) sistema.listarPedidos();
                else if (op == 12) sistema.reporteStockCritico();
                else if (op == 13) reportePedidosPorEstadoMenu();
                else if (op == 14) sistema.reporteVentasResumen();
            } catch (Exception e) {
                System.out.println("Error en menú ventas.");
            }

        } while (op != 0);
    }

    private void reportePedidosPorEstadoMenu() {
        try {
            System.out.println("1=PENDIENTE, 2=CONFIRMADO, 3=EN_PREPARACION, 4=ENVIADO");
            int op = in.leerEntero("Estado: ");

            EstadoPedido est;
            if (op == 1) est = EstadoPedido.PENDIENTE;
            else if (op == 2) est = EstadoPedido.CONFIRMADO;
            else if (op == 3) est = EstadoPedido.EN_PREPARACION;
            else if (op == 4) est = EstadoPedido.ENVIADO;
            else {
                System.out.println("Opción inválida.");
                return;
            }

            sistema.reportePedidosPorEstado(est);

        } catch (Exception e) {
            System.out.println("Error en reporte por estado.");
        }
    }

    private void verDetallePedidoAdmin() {
        try {
            int id = in.leerEntero("ID del pedido: ");

            if (sistema.buscarPedido(id) == null) {
                System.out.println("Pedido no encontrado.");
                return;
            }

            sistema.verDetallePedido(id);

        } catch (Exception e) {
            System.out.println("Error viendo detalle del pedido (Admin).");
        }
    }

    private void confirmarPedidoAdmin() {
        try {
            int id = in.leerEntero("ID del pedido a confirmar: ");

            if (sistema.buscarPedido(id) == null) {
                System.out.println("Pedido no encontrado.");
                return;
            }

            sistema.confirmarPedido(id);

        } catch (Exception e) {
            System.out.println("Error confirmando pedido (Admin).");
        }
    }

    private void cambiarEstadoPedidoAdmin() {
        try {
            int id = in.leerEntero("ID del pedido: ");

            if (sistema.buscarPedido(id) == null) {
                System.out.println("Pedido no encontrado.");
                return;
            }

            System.out.println("Estados disponibles:");
            System.out.println("1 = EN_PREPARACION");
            System.out.println("2 = ENVIADO");
            int op = in.leerEntero("Nuevo estado: ");

            EstadoPedido est;
            if (op == 1) est = EstadoPedido.EN_PREPARACION;
            else if (op == 2) est = EstadoPedido.ENVIADO;
            else {
                System.out.println("Opción inválida.");
                return;
            }

            sistema.cambiarEstadoPedido(id, est);

        } catch (Exception e) {
            System.out.println("Error cambiando estado (Admin).");
        }
    }

    private void menuReportesAdmin() {
        int op;
        do {
            System.out.println("\n===== REPORTES (ADMIN) =====");
            System.out.println("1. Reporte stock crítico");
            System.out.println("2. Reporte pedidos por estado");
            System.out.println("3. Reporte resumen de ventas");
            System.out.println("0. Volver");
            op = in.leerEntero("Opción: ");

            try {
                if (op == 1) {
                    sistema.reporteStockCritico();
                } else if (op == 2) {
                    reportePedidosPorEstadoAdmin();
                } else if (op == 3) {
                    sistema.reporteVentasResumen();
                }
            } catch (Exception e) {
                System.out.println("Error en menú de reportes.");
            }

        } while (op != 0);
    }

    private void reportePedidosPorEstadoAdmin() {
        try {
            System.out.println("1=PENDIENTE, 2=CONFIRMADO, 3=EN_PREPARACION, 4=ENVIADO");
            int op = in.leerEntero("Estado: ");

            EstadoPedido est;
            if (op == 1) est = EstadoPedido.PENDIENTE;
            else if (op == 2) est = EstadoPedido.CONFIRMADO;
            else if (op == 3) est = EstadoPedido.EN_PREPARACION;
            else if (op == 4) est = EstadoPedido.ENVIADO;
            else {
                System.out.println("Opción inválida.");
                return;
            }

            sistema.reportePedidosPorEstado(est);

        } catch (Exception e) {
            System.out.println("Error en reporte por estado (Admin).");
        }
    }

    private void prepararPedidoBodega() {
        try {
            int id = in.leerEntero("ID del pedido a preparar: ");
            if (sistema.buscarPedido(id) == null) {
                System.out.println("Pedido no encontrado.");
                return;
            }
            sistema.prepararPedido(id);
        } catch (Exception e) {
            System.out.println("Error preparando pedido.");
        }
    }

    private void registrarCliente() {
        boolean registrado = false;

        while (!registrado) {
            try {
                System.out.println("\n=== REGISTRAR CLIENTE ===");

                String cedula = in.leerTexto("Cédula/ID (10 dígitos EC): ").trim();

                // ✅ Validar cédula apenas se ingresa
                if (!Cliente.esCedulaEcuatoriana(cedula)) {
                    System.out.println("Cédula inválida. Debe ser ecuatoriana y válida. Intenta de nuevo.\n");
                    continue; // vuelve a empezar desde cédula (y todo de nuevo)
                }

                // ✅ Validar que no exista ya
                if (sistema.buscarCliente(cedula) != null) {
                    System.out.println("Ya existe un cliente con esa cédula. Intenta con otra.\n");
                    continue;
                }

                String nombre = in.leerTexto("Nombre: ").trim();
                if (nombre.isEmpty()) {
                    System.out.println("Nombre inválido. Intenta de nuevo.\n");
                    continue;
                }

                String direccion = in.leerTexto("Dirección: ").trim();
                if (direccion.isEmpty()) {
                    System.out.println("Dirección inválida. Intenta de nuevo.\n");
                    continue;
                }

                String telefono = in.leerTexto("Teléfono (09 + 8 dígitos): ").trim();
                // ✅ Validar teléfono aquí (antes de crear Cliente)
                if (telefono.length() != 10 || !telefono.startsWith("09")) {
                    System.out.println("Teléfono inválido. Debe iniciar con 09 y tener 10 dígitos.\n");
                    continue;
                }
                for (int i = 0; i < telefono.length(); i++) {
                    if (!Character.isDigit(telefono.charAt(i))) {
                        System.out.println("Teléfono inválido. Solo números.\n");
                        telefono = ""; // fuerza continue
                        break;
                    }
                }
                if (telefono.isEmpty()) continue;

                String correo = in.leerTexto("Correo: ").trim();
                // ✅ Validar correo permitido aquí
                if (!Cliente.validarCorreoPermitido(correo)) {
                    System.out.println("Correo inválido. Solo: @gmail.com, @outlook.com, @hotmail.com, @icloud.com\n");
                    continue;
                }

                // Crear cliente
                Cliente c = new Cliente(cedula, nombre, direccion, telefono, correo);

                // ✅ Si por alguna razón quedó inválido, reinicia
                if (!c.esValido()) {
                    System.out.println("No se pudo crear el cliente. Intenta de nuevo.\n");
                    continue;
                }

                // Registrar en sistema
                if (sistema.registrarCliente(c)) {
                    registrado = true;
                    System.out.println("Cliente registrado correctamente.");
                } else {
                    System.out.println("No se pudo registrar el cliente. Intenta de nuevo.\n");
                }

            } catch (Exception e) {
                System.out.println("Error registrando cliente. Intenta de nuevo.\n");
            }
        }
    }

    private void verDetalleCliente() {
        try {
            String cedula = in.leerTexto("Cédula/ID del cliente: ").trim();

            if (sistema.buscarCliente(cedula) == null) {
                System.out.println("No se encontró un cliente con ese ID: " + cedula);
                return;
            }

            sistema.verDetalleCliente(cedula);

        } catch (Exception e) {
            System.out.println("Error viendo detalle del cliente.");
        }
    }

    private void editarCliente() {
        try {
            String cedula = in.leerTexto("Cédula/ID del cliente a editar: ").trim();

            if (sistema.buscarCliente(cedula) == null) {
                System.out.println("No se encontró un cliente con ese ID: " + cedula);
                return;
            }

            System.out.println("Deja vacío lo que NO quieras cambiar.");
            String nuevoNombre = in.leerTexto("Nuevo nombre: ");
            String nuevaDireccion = in.leerTexto("Nueva dirección: ");
            String nuevoTelefono = in.leerTexto("Nuevo teléfono: ");
            String nuevoCorreo = in.leerTexto("Nuevo correo: ");

            sistema.editarCliente(cedula, nuevoNombre, nuevaDireccion, nuevoTelefono, nuevoCorreo);

        } catch (Exception e) {
            System.out.println("Error editando cliente.");
        }
    }

    private void eliminarCliente() {
        try {
            String cedula = in.leerTexto("Cédula/ID del cliente a eliminar: ").trim();

            if (sistema.buscarCliente(cedula) == null) {
                System.out.println("No se encontró un cliente con ese ID: " + cedula);
                return;
            }

            String conf = in.leerTexto("¿Seguro que deseas eliminarlo? (S/N): ");
            if (conf.equalsIgnoreCase("S")) {
                sistema.eliminarCliente(cedula);
            } else {
                System.out.println("Eliminación cancelada.");
            }

        } catch (Exception e) {
            System.out.println("Error eliminando cliente.");
        }
    }

    private void crearPedido() {
        try {
            String cedula = in.leerTexto("Cédula/ID del cliente: ").trim();

            if (sistema.buscarCliente(cedula) == null) {
                System.out.println("No se encontró un cliente con ese ID: " + cedula);
                return;
            }

            String fecha = in.leerTexto("Fecha (dd/mm/aaaa): ").trim();
            int id = sistema.crearPedido(cedula, fecha);

            if (id != -1) {
                System.out.println("✅ Pedido creado. Ahora agrega productos con la opción 7.");
            }

        } catch (Exception e) {
            System.out.println("Error creando pedido.");
        }
    }

    private void agregarProductoAPedido() {
        try {
            int id = in.leerEntero("ID del pedido: ");
            if (sistema.buscarPedido(id) == null) {
                System.out.println("Pedido no encontrado.");
                return;
            }

            String cod = in.leerTexto("Código del producto: ").trim().toUpperCase();
            if (sistema.buscarProducto(cod) == null) {
                System.out.println("Producto no encontrado con el código: " + cod);
                return;
            }

            int cantidad = in.leerEntero("Cantidad: ");
            if (cantidad <= 0) {
                System.out.println("Cantidad inválida.");
                return;
            }

            sistema.agregarProductoAPedido(id, cod, cantidad);

        } catch (Exception e) {
            System.out.println("Error agregando producto al pedido.");
        }
    }

    private void verDetallePedido() {
        try {
            int id = in.leerEntero("ID del pedido: ");
            if (sistema.buscarPedido(id) == null) {
                System.out.println("Pedido no encontrado.");
                return;
            }
            sistema.verDetallePedido(id);

        } catch (Exception e) {
            System.out.println("Error viendo detalle del pedido.");
        }
    }

    private void confirmarPedido() {
        try {
            int id = in.leerEntero("ID del pedido a confirmar: ");
            if (sistema.buscarPedido(id) == null) {
                System.out.println("Pedido no encontrado.");
                return;
            }
            sistema.confirmarPedido(id);

        } catch (Exception e) {
            System.out.println("Error confirmando pedido.");
        }
    }

    private void cambiarEstadoPedido() {
        try {
            int id = in.leerEntero("ID del pedido: ");
            if (sistema.buscarPedido(id) == null) {
                System.out.println("Pedido no encontrado.");
                return;
            }

            System.out.println("1=EN_PREPARACION, 2=ENVIADO");
            int op = in.leerEntero("Nuevo estado: ");

            EstadoPedido est;
            if (op == 1) est = EstadoPedido.EN_PREPARACION;
            else if (op == 2) est = EstadoPedido.ENVIADO;
            else {
                System.out.println("Opción inválida.");
                return;
            }

            sistema.cambiarEstadoPedido(id, est);

        } catch (Exception e) {
            System.out.println("Error cambiando estado.");
        }
    }
}