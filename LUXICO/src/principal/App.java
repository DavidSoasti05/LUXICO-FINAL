package principal;

import utilitaria.SistemaLuxico;
import negocio.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class App extends JFrame {

    private SistemaLuxico sistema;
    private Usuario usuarioActual;

    private CardLayout card;
    private JPanel root;

    private LoginPanel loginPanel;
    private MainPanel mainPanel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new App().setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error iniciando App.");
            }
        });
    }

    public App() {
        try {
            sistema = new SistemaLuxico();

            setTitle("Luxico - Sistema (GUI)");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(1050, 600);
            setLocationRelativeTo(null);

            card = new CardLayout();
            root = new JPanel(card);

            loginPanel = new LoginPanel();
            mainPanel = new MainPanel();

            root.add(loginPanel, "LOGIN");
            root.add(mainPanel, "MAIN");

            add(root);
            card.show(root, "LOGIN");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error creando ventana.");
        }
    }

    private Producto buscarProductoPorCodigo(String codigo) {
        if (codigo == null) return null;
        String cod = codigo.trim().toUpperCase();
        if (cod.isEmpty()) return null;

        ArrayList<Producto> list = sistema.getProductos();
        for (Producto p : list) {
            if (p != null && p.getCodigo() != null && p.getCodigo().equals(cod)) return p;
        }
        return null;
    }

    private class LoginPanel extends JPanel {
        private JTextField txtUser;
        private JPasswordField txtPass;

        public LoginPanel() {
            try {
                setLayout(new GridBagLayout());
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(8, 8, 8, 8);
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.weightx = 1;

                final int LOGO_W = 560;
                final int LOGO_H = 500;

                final int MARCO_W = 560;
                final int MARCO_H = 260;

                JPanel logoPanel = new JPanel() {
                    Image img;

                    {
                        try {
                            java.net.URL url = getClass().getResource("/recursos/luxico_jordan.png");
                            if (url != null) img = new ImageIcon(url).getImage();
                            else System.out.println("No se encontro /luxico_jordan.png en recursos");
                        } catch (Exception e) {
                            System.out.println("No se pudo cargar el logo: " + e.getMessage());
                        }
                        setOpaque(false);
                        setPreferredSize(new Dimension(MARCO_W, MARCO_H));
                    }

                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        if (img != null) {
                            int x = (getWidth() - LOGO_W) / 2;
                            int y = (getHeight() - LOGO_H) / 2 + 60;
                            g.drawImage(img, x, y, LOGO_W, LOGO_H, this);
                        }
                    }
                };

                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.gridwidth = 2;
                gbc.anchor = GridBagConstraints.CENTER;
                gbc.fill = GridBagConstraints.NONE;
                gbc.insets = new Insets(50, 8, 0, 8);
                add(logoPanel, gbc);

                JLabel title = new JLabel("LUXICO - Login", SwingConstants.CENTER);
                title.setFont(new Font("Arial", Font.BOLD, 24));

                gbc.insets = new Insets(8, 8, 8, 8);
                gbc.gridx = 0;
                gbc.gridy = 1;
                gbc.gridwidth = 2;
                gbc.anchor = GridBagConstraints.CENTER;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                add(title, gbc);

                gbc.gridwidth = 1;
                gbc.gridx = 0;
                gbc.gridy = 2;
                gbc.anchor = GridBagConstraints.EAST;
                gbc.fill = GridBagConstraints.NONE;
                add(new JLabel("Usuario:"), gbc);

                txtUser = new JTextField(16);
                gbc.gridx = 1;
                gbc.gridy = 2;
                gbc.anchor = GridBagConstraints.WEST;
                gbc.fill = GridBagConstraints.NONE;
                add(txtUser, gbc);

                gbc.gridx = 0;
                gbc.gridy = 3;
                gbc.anchor = GridBagConstraints.EAST;
                gbc.fill = GridBagConstraints.NONE;
                add(new JLabel("Clave:"), gbc);

                txtPass = new JPasswordField(16);
                gbc.gridx = 1;
                gbc.gridy = 3;
                gbc.anchor = GridBagConstraints.WEST;
                gbc.fill = GridBagConstraints.NONE;
                add(txtPass, gbc);

                JButton btnLogin = new JButton("Ingresar");
                btnLogin.setPreferredSize(new Dimension(220, 34));

                gbc.gridx = 0;
                gbc.gridy = 4;
                gbc.gridwidth = 2;
                gbc.anchor = GridBagConstraints.CENTER;
                gbc.fill = GridBagConstraints.NONE;
                add(btnLogin, gbc);

                gbc.gridx = 0;
                gbc.gridy = 5;
                gbc.gridwidth = 2;
                gbc.weighty = 1;
                add(new JLabel(""), gbc);

                btnLogin.addActionListener(e -> {
                    try {
                        String u = txtUser.getText().trim();
                        String p = new String(txtPass.getPassword()).trim();
                        Usuario log = sistema.login(u, p);

                        if (log == null) {
                            JOptionPane.showMessageDialog(this, "Credenciales incorrectas.");
                            return;
                        }

                        usuarioActual = log;
                        mainPanel.configurarPorRol(usuarioActual);
                        mainPanel.refrescarTodo();
                        card.show(root, "MAIN");

                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Error en login.");
                    }
                });

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error creando login.");
            }
        }
    }

    // =========================================================
    // PANEL PRINCIPAL
    // =========================================================
    private class MainPanel extends JPanel {

        private JLabel lblRol;
        private JTabbedPane tabs;

        private ProductosPanel productosPanel;
        private MovimientosPanel movimientosPanel;
        private ClientesPanel clientesPanel;
        private PedidosPanel pedidosPanel;
        private ReportesPanel reportesPanel;

        public MainPanel() {
            try {
                setLayout(new BorderLayout());

                JPanel top = new JPanel(new BorderLayout());
                lblRol = new JLabel("Rol: -", SwingConstants.LEFT);
                lblRol.setFont(new Font("Arial", Font.BOLD, 16));
                top.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

                JButton btnLogout = new JButton("Cerrar sesión");
                btnLogout.addActionListener(e -> logout());

                top.add(lblRol, BorderLayout.WEST);
                top.add(btnLogout, BorderLayout.EAST);

                add(top, BorderLayout.NORTH);

                tabs = new JTabbedPane();

                productosPanel = new ProductosPanel();
                movimientosPanel = new MovimientosPanel();
                clientesPanel = new ClientesPanel();
                pedidosPanel = new PedidosPanel();
                reportesPanel = new ReportesPanel();

                tabs.addTab("Productos", productosPanel);
                tabs.addTab("Movimientos", movimientosPanel);
                tabs.addTab("Clientes", clientesPanel);
                tabs.addTab("Pedidos", pedidosPanel);
                tabs.addTab("Reportes", reportesPanel);

                add(tabs, BorderLayout.CENTER);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error creando panel principal.");
            }
        }

        public void configurarPorRol(Usuario u) {
            try {
                String rol;
                if (u instanceof Admin) rol = "ADMIN";
                else if (u instanceof Bodega) rol = "BODEGA";
                else if (u instanceof Ventas) rol = "VENTAS";
                else rol = "DESCONOCIDO";

                lblRol.setText("Usuario: " + u.getUsuario() + " | Rol: " + rol);

                tabs.removeAll();

                if (u instanceof Admin) {
                    tabs.addTab("Productos", productosPanel);
                    tabs.addTab("Movimientos", movimientosPanel);
                    tabs.addTab("Clientes", clientesPanel);
                    tabs.addTab("Pedidos", pedidosPanel);
                    tabs.addTab("Reportes", reportesPanel);
                } else if (u instanceof Bodega) {
                    tabs.addTab("Productos", productosPanel);
                    tabs.addTab("Movimientos", movimientosPanel);
                    tabs.addTab("Pedidos", pedidosPanel);
                    tabs.addTab("Reportes", reportesPanel);
                } else if (u instanceof Ventas) {
                    tabs.addTab("Clientes", clientesPanel);
                    tabs.addTab("Pedidos", pedidosPanel);
                    tabs.addTab("Reportes", reportesPanel);
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error configurando rol.");
            }
        }

        public void refrescarTodo() {
            try {
                productosPanel.refrescarTabla();
                movimientosPanel.refrescarTabla();
                clientesPanel.refrescarTabla();
                pedidosPanel.refrescarTabla();
            } catch (Exception e) {
            }
        }
    }

    private void logout() {
        try {
            usuarioActual = null;
            card.show(root, "LOGIN");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error cerrando sesión.");
        }
    }

    private class ProductosPanel extends JPanel {

        private DefaultTableModel model;
        private JTable table;

        public ProductosPanel() {
            try {
                setLayout(new BorderLayout());

                model = new DefaultTableModel(new Object[]{"Código", "Modelo", "Talla", "Precio", "Stock", "StockMin", "Tipo"}, 0) {
                    public boolean isCellEditable(int row, int col) { return false; }
                };
                table = new JTable(model);
                add(new JScrollPane(table), BorderLayout.CENTER);

                JPanel btns = new JPanel(new FlowLayout(FlowLayout.LEFT));

                JButton btnCrear = new JButton("Crear");
                JButton btnEditar = new JButton("Editar");
                JButton btnEliminar = new JButton("Eliminar");
                JButton btnRefrescar = new JButton("Refrescar");

                btns.add(btnCrear);
                btns.add(btnEditar);
                btns.add(btnEliminar);
                btns.add(btnRefrescar);

                add(btns, BorderLayout.NORTH);

                btnCrear.addActionListener(e -> crearProductoGUI());
                btnEditar.addActionListener(e -> editarProductoGUI());
                btnEliminar.addActionListener(e -> eliminarProductoGUI());
                btnRefrescar.addActionListener(e -> refrescarTabla());

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error creando panel Productos.");
            }
        }

        public void refrescarTabla() {
            try {
                model.setRowCount(0);
                ArrayList<Producto> list = sistema.getProductos();
                for (Producto p : list) {
                    model.addRow(new Object[]{
                            p.getCodigo(), p.getModelo(), p.getTalla(),
                            p.getPrecio(), p.getStock(), p.getStockMin(), p.getTipo()
                    });
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error refrescando productos.");
            }
        }

        private void crearProductoGUI() {
            try {
                if (!(usuarioActual instanceof Admin) && !(usuarioActual instanceof Bodega)) {
                    JOptionPane.showMessageDialog(this, "Solo Admin/Bodega puede crear productos.");
                    return;
                }

                String codigo = JOptionPane.showInputDialog(this, "Código:");
                if (codigo == null) return;

                String modelo = JOptionPane.showInputDialog(this, "Modelo:");
                if (modelo == null) return;

                String talla = JOptionPane.showInputDialog(this, "Talla:");
                if (talla == null) return;

                String precioS = JOptionPane.showInputDialog(this, "Precio:");
                if (precioS == null) return;

                String stockS = JOptionPane.showInputDialog(this, "Stock inicial:");
                if (stockS == null) return;

                String stockMinS = JOptionPane.showInputDialog(this, "Stock mínimo:");
                if (stockMinS == null) return;

                Object[] tipos = {"REGULAR", "EDICION_LIMITADA"};
                Object tipoSel = JOptionPane.showInputDialog(this, "Tipo:", "Tipo de producto",
                        JOptionPane.QUESTION_MESSAGE, null, tipos, tipos[0]);
                if (tipoSel == null) return;

                double precio = Double.parseDouble(precioS.trim());
                int stock = Integer.parseInt(stockS.trim());
                int stockMin = Integer.parseInt(stockMinS.trim());

                Producto p;
                if (tipoSel.toString().equals("REGULAR")) {
                    p = new ProductoRegular(codigo.trim().toUpperCase(), modelo.trim(), talla.trim(), precio, stock, stockMin);
                } else {
                    p = new ProductoEdicionLimitada(codigo.trim().toUpperCase(), modelo.trim(), talla.trim(), precio, stock, stockMin);
                }

                boolean ok = sistema.agregarProducto(p);
                JOptionPane.showMessageDialog(this, ok ? "Producto creado." : "No se pudo crear.");
                refrescarTabla();

            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "Precio/Stock inválido.");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error creando producto.");
            }
        }

        private void editarProductoGUI() {
            try {
                if (!(usuarioActual instanceof Admin) && !(usuarioActual instanceof Bodega)) {
                    JOptionPane.showMessageDialog(this, "Solo Admin/Bodega puede editar productos.");
                    return;
                }

                String codigo = JOptionPane.showInputDialog(this, "Código del producto a editar:");
                if (codigo == null) return;

                Producto p = buscarProductoPorCodigo(codigo);
                if (p == null) {
                    JOptionPane.showMessageDialog(this, "Producto no encontrado.");
                    return;
                }

                String nuevoModelo = JOptionPane.showInputDialog(this, "Nuevo modelo (vacío = no cambiar):", p.getModelo());
                if (nuevoModelo == null) return;

                String nuevaTalla = JOptionPane.showInputDialog(this, "Nueva talla (vacío = no cambiar):", p.getTalla());
                if (nuevaTalla == null) return;

                String nuevoPrecioS = JOptionPane.showInputDialog(this, "Nuevo precio (0 = no cambiar):", String.valueOf(p.getPrecio()));
                if (nuevoPrecioS == null) return;

                String nuevoStockMinS = JOptionPane.showInputDialog(this, "Nuevo stock mínimo (-1 = no cambiar):", String.valueOf(p.getStockMin()));
                if (nuevoStockMinS == null) return;

                // aplicar cambios al OBJETO
                if (!nuevoModelo.trim().isEmpty()) p.setModelo(nuevoModelo.trim());
                if (!nuevaTalla.trim().isEmpty()) p.setTalla(nuevaTalla.trim());

                double nuevoPrecio = Double.parseDouble(nuevoPrecioS.trim());
                if (nuevoPrecio > 0) p.setPrecio(nuevoPrecio);

                int nuevoStockMin = Integer.parseInt(nuevoStockMinS.trim());
                if (nuevoStockMin >= 0) p.setStockMin(nuevoStockMin);

                sistema.guardarTodo();

                JOptionPane.showMessageDialog(this, "Producto actualizado.");
                refrescarTabla();

            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "Precio/StockMin inválido.");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error editando producto.");
            }
        }

        private void eliminarProductoGUI() {
            try {
                if (!(usuarioActual instanceof Admin) && !(usuarioActual instanceof Bodega)) {
                    JOptionPane.showMessageDialog(this, "Solo Admin/Bodega puede eliminar productos.");
                    return;
                }

                String codigo = JOptionPane.showInputDialog(this, "Código del producto a eliminar:");
                if (codigo == null) return;

                Producto p = buscarProductoPorCodigo(codigo);
                if (p == null) {
                    JOptionPane.showMessageDialog(this, "Producto no encontrado.");
                    return;
                }

                int op = JOptionPane.showConfirmDialog(this,
                        "¿Seguro de eliminar " + p.getCodigo() + " - " + p.getModelo() + "?",
                        "Confirmar",
                        JOptionPane.YES_NO_OPTION);

                if (op != JOptionPane.YES_OPTION) return;

                boolean ok = sistema.eliminarProducto(p);

                JOptionPane.showMessageDialog(this, ok ? "Producto eliminado." : "No se pudo eliminar.");
                refrescarTabla();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error eliminando producto.");
            }
        }
    }

    private class MovimientosPanel extends JPanel {

        private DefaultTableModel model;
        private JTable table;

        public MovimientosPanel() {
            try {
                setLayout(new BorderLayout());

                model = new DefaultTableModel(new Object[]{"Producto", "Tipo", "Cantidad", "Fecha", "Motivo"}, 0) {
                    public boolean isCellEditable(int row, int col) { return false; }
                };
                table = new JTable(model);
                add(new JScrollPane(table), BorderLayout.CENTER);

                JPanel btns = new JPanel(new FlowLayout(FlowLayout.LEFT));
                JButton btnRegistrar = new JButton("Registrar movimiento");
                JButton btnRefrescar = new JButton("Refrescar");

                btns.add(btnRegistrar);
                btns.add(btnRefrescar);

                add(btns, BorderLayout.NORTH);

                btnRegistrar.addActionListener(e -> registrarMovimientoGUI());
                btnRefrescar.addActionListener(e -> refrescarTabla());

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error creando panel Movimientos.");
            }
        }

        public void refrescarTabla() {
            try {
                model.setRowCount(0);
                ArrayList<MovimientoInventario> list = sistema.getMovimientos();
                for (MovimientoInventario m : list) {
                    // OO puro: MovimientoInventario tiene Producto
                    Producto p = m.getProducto();
                    String cod = (p != null) ? p.getCodigo() : "N/A";

                    model.addRow(new Object[]{
                            cod, m.getTipo(), m.getCantidad(), m.getFecha(), m.getMotivo()
                    });
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error refrescando movimientos.");
            }
        }

        private void registrarMovimientoGUI() {
            try {
                if (!(usuarioActual instanceof Admin) && !(usuarioActual instanceof Bodega)) {
                    JOptionPane.showMessageDialog(this, "Solo Admin/Bodega puede registrar movimientos.");
                    return;
                }

                String codigo = JOptionPane.showInputDialog(this, "Código producto:");
                if (codigo == null) return;

                Producto prod = buscarProductoPorCodigo(codigo);
                if (prod == null) {
                    JOptionPane.showMessageDialog(this, "Producto no encontrado.");
                    return;
                }

                Object[] tipos = {"ENTRADA", "SALIDA", "DEVOLUCION"};
                Object tipoSel = JOptionPane.showInputDialog(this, "Tipo:", "Movimiento",
                        JOptionPane.QUESTION_MESSAGE, null, tipos, tipos[0]);
                if (tipoSel == null) return;

                String cantS = JOptionPane.showInputDialog(this, "Cantidad:");
                if (cantS == null) return;

                String motivo = JOptionPane.showInputDialog(this, "Motivo:");
                if (motivo == null) return;

                int cantidad = Integer.parseInt(cantS.trim());
                String fecha = LocalDate.now().toString();
                TipoMovimiento tipo = TipoMovimiento.valueOf(tipoSel.toString());

                // ✅ OO puro: se manda el OBJETO Producto
                boolean ok = sistema.registrarMovimiento(prod, tipo, cantidad, fecha, motivo);

                JOptionPane.showMessageDialog(this, ok ? "Movimiento registrado." : "No se pudo registrar.");
                refrescarTabla();

            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "Cantidad inválida.");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error registrando movimiento.");
            }
        }
    }

    private class ClientesPanel extends JPanel {

        private DefaultTableModel model;
        private JTable table;

        public ClientesPanel() {
            try {
                setLayout(new BorderLayout());

                model = new DefaultTableModel(new Object[]{"Cédula", "Nombre", "Dirección", "Teléfono", "Correo"}, 0) {
                    public boolean isCellEditable(int row, int col) { return false; }
                };
                table = new JTable(model);
                add(new JScrollPane(table), BorderLayout.CENTER);

                JPanel btns = new JPanel(new FlowLayout(FlowLayout.LEFT));
                JButton btnCrear = new JButton("Registrar");
                JButton btnEditar = new JButton("Editar");
                JButton btnEliminar = new JButton("Eliminar");
                JButton btnRefrescar = new JButton("Refrescar");

                btns.add(btnCrear);
                btns.add(btnEditar);
                btns.add(btnEliminar);
                btns.add(btnRefrescar);

                add(btns, BorderLayout.NORTH);

                btnCrear.addActionListener(e -> crearClienteGUI());
                btnEditar.addActionListener(e -> editarClienteGUI());
                btnEliminar.addActionListener(e -> eliminarClienteGUI());
                btnRefrescar.addActionListener(e -> refrescarTabla());

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error creando panel Clientes.");
            }
        }

        public void refrescarTabla() {
            try {
                model.setRowCount(0);
                ArrayList<Cliente> list = sistema.getClientes();
                for (Cliente c : list) {
                    model.addRow(new Object[]{
                            c.getCedula(), c.getNombre(), c.getDireccion(), c.getTelefono(), c.getCorreo()
                    });
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error refrescando clientes.");
            }
        }

        private void crearClienteGUI() {
            try {
                if (!(usuarioActual instanceof Admin) && !(usuarioActual instanceof Ventas)) {
                    JOptionPane.showMessageDialog(this, "Solo Admin/Ventas puede registrar clientes.");
                    return;
                }

                String cedula = JOptionPane.showInputDialog(this, "Cédula (10 dígitos):");
                if (cedula == null) return;

                String nombre = JOptionPane.showInputDialog(this, "Nombre:");
                if (nombre == null) return;

                String dir = JOptionPane.showInputDialog(this, "Dirección:");
                if (dir == null) return;

                String tel = JOptionPane.showInputDialog(this, "Teléfono (09xxxxxxxx):");
                if (tel == null) return;

                String correo = JOptionPane.showInputDialog(this, "Correo (@gmail.com, @outlook.com, @hotmail.com, @icloud.com):");
                if (correo == null) return;

                Cliente c = new Cliente(cedula.trim(), nombre.trim(), dir.trim(), tel.trim(), correo.trim());

                boolean ok = sistema.registrarCliente(c);
                JOptionPane.showMessageDialog(this, ok ? "Cliente registrado." : "No se pudo registrar.");
                refrescarTabla();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error registrando cliente.");
            }
        }

        private void editarClienteGUI() {
            try {
                if (!(usuarioActual instanceof Admin) && !(usuarioActual instanceof Ventas)) {
                    JOptionPane.showMessageDialog(this, "Solo Admin/Ventas puede editar clientes.");
                    return;
                }

                String cedula = JOptionPane.showInputDialog(this, "Cédula del cliente:");
                if (cedula == null) return;

                Cliente c = sistema.buscarCliente(cedula);
                if (c == null) {
                    JOptionPane.showMessageDialog(this, "Cliente no encontrado.");
                    return;
                }

                String nuevoNombre = JOptionPane.showInputDialog(this, "Nuevo nombre (vacío = no cambiar):", c.getNombre());
                if (nuevoNombre == null) return;

                String nuevaDir = JOptionPane.showInputDialog(this, "Nueva dirección (vacío = no cambiar):", c.getDireccion());
                if (nuevaDir == null) return;

                String nuevoTel = JOptionPane.showInputDialog(this, "Nuevo teléfono (vacío = no cambiar):", c.getTelefono());
                if (nuevoTel == null) return;

                String nuevoCorreo = JOptionPane.showInputDialog(this, "Nuevo correo (vacío = no cambiar):", c.getCorreo());
                if (nuevoCorreo == null) return;

                boolean ok = sistema.editarCliente(cedula, nuevoNombre, nuevaDir, nuevoTel, nuevoCorreo);
                JOptionPane.showMessageDialog(this, ok ? "Cliente actualizado." : "No se actualizó.");
                refrescarTabla();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error editando cliente.");
            }
        }

        private void eliminarClienteGUI() {
            try {
                if (!(usuarioActual instanceof Admin) && !(usuarioActual instanceof Ventas)) {
                    JOptionPane.showMessageDialog(this, "Solo Admin/Ventas puede eliminar clientes.");
                    return;
                }

                String cedula = JOptionPane.showInputDialog(this, "Cédula del cliente a eliminar:");
                if (cedula == null) return;

                int op = JOptionPane.showConfirmDialog(this, "¿Seguro de eliminar " + cedula + "?", "Confirmar",
                        JOptionPane.YES_NO_OPTION);
                if (op != JOptionPane.YES_OPTION) return;

                boolean ok = sistema.eliminarCliente(cedula);
                JOptionPane.showMessageDialog(this, ok ? "Cliente eliminado." : "No se pudo eliminar.");
                refrescarTabla();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error eliminando cliente.");
            }
        }
    }

    private class PedidosPanel extends JPanel {

        private DefaultTableModel model;
        private JTable table;

        public PedidosPanel() {
            try {
                setLayout(new BorderLayout());

                model = new DefaultTableModel(new Object[]{"ID", "Cliente", "Cedula", "Fecha", "Estado", "Total"}, 0) {
                    public boolean isCellEditable(int r, int c) { return false; }
                };
                table = new JTable(model);
                add(new JScrollPane(table), BorderLayout.CENTER);

                JPanel btns = new JPanel(new FlowLayout(FlowLayout.LEFT));

                JButton btnCrear = new JButton("Crear pedido");
                JButton btnAgregar = new JButton("Agregar producto");
                JButton btnDetalle = new JButton("Ver detalle");
                JButton btnConfirmar = new JButton("Confirmar pedido");
                JButton btnEstado = new JButton("Cambiar estado");
                JButton btnPreparar = new JButton("Marcar EN_PREPARACION (Bodega)");
                JButton btnRefrescar = new JButton("Refrescar");

                btns.add(btnCrear);
                btns.add(btnAgregar);
                btns.add(btnDetalle);
                btns.add(btnConfirmar);
                btns.add(btnEstado);
                btns.add(btnPreparar);
                btns.add(btnRefrescar);

                add(btns, BorderLayout.NORTH);

                btnCrear.addActionListener(e -> crearPedidoGUI());
                btnAgregar.addActionListener(e -> agregarProductoPedidoGUI());
                btnDetalle.addActionListener(e -> verDetallePedidoGUI());
                btnConfirmar.addActionListener(e -> confirmarPedidoGUI());
                btnEstado.addActionListener(e -> cambiarEstadoGUI());
                btnPreparar.addActionListener(e -> prepararPedidoGUI());
                btnRefrescar.addActionListener(e -> refrescarTabla());

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error creando panel Pedidos.");
            }
        }

        public void refrescarTabla() {
            try {
                model.setRowCount(0);
                ArrayList<Pedido> list = sistema.getPedidos();
                for (Pedido p : list) {

                    Cliente c = p.getCliente();
                    String nombre = (c != null) ? c.getNombre() : "N/A";
                    String cedula = (c != null) ? c.getCedula() : "N/A";

                    model.addRow(new Object[]{
                            p.getId(), nombre, cedula,
                            p.getFechaCreacion(), p.getEstado(), p.getTotal()
                    });
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error refrescando pedidos.");
            }
        }

        private void crearPedidoGUI() {
            try {
                if (!(usuarioActual instanceof Admin) && !(usuarioActual instanceof Ventas)) {
                    JOptionPane.showMessageDialog(this, "Solo Admin/Ventas puede crear pedidos.");
                    return;
                }

                String cedula = JOptionPane.showInputDialog(this, "Cédula del cliente:");
                if (cedula == null) return;

                Cliente c = sistema.buscarCliente(cedula);
                if (c == null) {
                    JOptionPane.showMessageDialog(this, "Cliente no encontrado.");
                    return;
                }

                String fecha = LocalDate.now().toString();
                int id = sistema.crearPedido(c, fecha);

                JOptionPane.showMessageDialog(this, id > 0 ? "Pedido creado. ID: " + id : "No se pudo crear.");
                refrescarTabla();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error creando pedido.");
            }
        }

        private void agregarProductoPedidoGUI() {
            try {
                if (!(usuarioActual instanceof Admin) && !(usuarioActual instanceof Ventas)) {
                    JOptionPane.showMessageDialog(this, "Solo Admin/Ventas puede agregar items.");
                    return;
                }

                String idS = JOptionPane.showInputDialog(this, "ID Pedido:");
                if (idS == null) return;

                String cod = JOptionPane.showInputDialog(this, "Código producto:");
                if (cod == null) return;

                String cantS = JOptionPane.showInputDialog(this, "Cantidad:");
                if (cantS == null) return;

                int id = Integer.parseInt(idS.trim());
                int cant = Integer.parseInt(cantS.trim());

                Producto prod = buscarProductoPorCodigo(cod);
                if (prod == null) {
                    JOptionPane.showMessageDialog(this, "Producto no encontrado.");
                    return;
                }
                boolean ok = sistema.agregarProductoAPedido(id, prod, cant);

                JOptionPane.showMessageDialog(this, ok ? "Item agregado." : "No se pudo agregar.");
                refrescarTabla();

            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "ID/Cantidad inválida.");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error agregando producto al pedido.");
            }
        }

        private void verDetallePedidoGUI() {
            try {
                String idS = JOptionPane.showInputDialog(this, "ID Pedido:");
                if (idS == null) return;

                int id = Integer.parseInt(idS.trim());
                Pedido p = sistema.buscarPedido(id);

                if (p == null) {
                    JOptionPane.showMessageDialog(this, "Pedido no encontrado.");
                    return;
                }

                Cliente c = p.getCliente();
                String nombre = (c != null) ? c.getNombre() : "N/A";
                String cedula = (c != null) ? c.getCedula() : "N/A";

                StringBuilder sb = new StringBuilder();
                sb.append("ID: ").append(p.getId()).append("\n");
                sb.append("Cliente: ").append(nombre).append(" (").append(cedula).append(")\n");
                sb.append("Fecha: ").append(p.getFechaCreacion()).append("\n");
                sb.append("Estado: ").append(p.getEstado()).append("\n");
                sb.append("Total: $").append(p.getTotal()).append("\n\n");
                sb.append("--- Items ---\n");

                if (p.getDetalles().size() == 0) sb.append("Sin productos.\n");
                else {
                    for (int i = 0; i < p.getDetalles().size(); i++) {
                        sb.append(i + 1).append(") ").append(p.getDetalles().get(i)).append("\n");
                    }
                }

                mostrarTexto("Detalle Pedido #" + id, sb.toString());

            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "ID inválido.");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error mostrando detalle.");
            }
        }

        private void confirmarPedidoGUI() {
            try {
                if (!(usuarioActual instanceof Admin) && !(usuarioActual instanceof Ventas)) {
                    JOptionPane.showMessageDialog(this, "Solo Admin/Ventas puede confirmar pedidos.");
                    return;
                }

                String idS = JOptionPane.showInputDialog(this, "ID Pedido a confirmar:");
                if (idS == null) return;

                int id = Integer.parseInt(idS.trim());
                boolean ok = sistema.confirmarPedido(id);

                JOptionPane.showMessageDialog(this, ok ? "Pedido confirmado." : "No se pudo confirmar.");
                refrescarTabla();

            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "ID inválido.");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error confirmando pedido.");
            }
        }

        private void cambiarEstadoGUI() {
            try {
                if (!(usuarioActual instanceof Admin) && !(usuarioActual instanceof Ventas)) {
                    JOptionPane.showMessageDialog(this, "Solo Admin/Ventas puede cambiar estado.");
                    return;
                }

                String idS = JOptionPane.showInputDialog(this, "ID Pedido:");
                if (idS == null) return;

                int id = Integer.parseInt(idS.trim());

                Object[] opciones = {"EN_PREPARACION", "ENVIADO"};
                Object sel = JOptionPane.showInputDialog(this, "Nuevo estado:", "Estado",
                        JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);
                if (sel == null) return;

                EstadoPedido est = EstadoPedido.valueOf(sel.toString());
                boolean ok = sistema.cambiarEstadoPedido(id, est);

                JOptionPane.showMessageDialog(this, ok ? "Estado actualizado." : "No se pudo cambiar estado.");
                refrescarTabla();

            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "ID inválido.");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error cambiando estado.");
            }
        }

        private void prepararPedidoGUI() {
            try {
                if (!(usuarioActual instanceof Admin) && !(usuarioActual instanceof Bodega)) {
                    JOptionPane.showMessageDialog(this, "Solo Admin/Bodega puede preparar pedidos.");
                    return;
                }

                String idS = JOptionPane.showInputDialog(this, "ID Pedido CONFIRMADO:");
                if (idS == null) return;

                int id = Integer.parseInt(idS.trim());
                boolean ok = sistema.prepararPedido(id);

                JOptionPane.showMessageDialog(this, ok ? "Pedido pasó a EN_PREPARACION." : "No se pudo preparar.");
                refrescarTabla();

            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "ID inválido.");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error preparando pedido.");
            }
        }
    }

    private class ReportesPanel extends JPanel {

        public ReportesPanel() {
            try {
                setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));

                JButton btnStock = new JButton("Stock crítico");
                JButton btnPedidosEstado = new JButton("Pedidos por estado");
                JButton btnVentas = new JButton("Resumen ventas");
                JButton btnReset = new JButton("RESET (borrar .dat)");

                add(btnStock);
                add(btnPedidosEstado);
                add(btnVentas);
                add(btnReset);

                btnStock.addActionListener(e -> reporteStockCriticoGUI());
                btnPedidosEstado.addActionListener(e -> reportePedidosPorEstadoGUI());
                btnVentas.addActionListener(e -> reporteVentasGUI());
                btnReset.addActionListener(e -> resetGUI());

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error creando reportes.");
            }
        }

        private void reporteStockCriticoGUI() {
            try {
                ArrayList<Producto> list = sistema.getProductos();
                StringBuilder sb = new StringBuilder();
                sb.append("=== REPORTE STOCK CRÍTICO ===\n");

                boolean hay = false;
                for (Producto p : list) {
                    if (p.estaBajoMinimo()) {
                        hay = true;
                        sb.append("- ").append(p.getCodigo()).append(" | ").append(p.getModelo())
                                .append(" | Stock: ").append(p.getStock())
                                .append(" | Min: ").append(p.getStockMin())
                                .append("\n");
                    }
                }
                if (!hay) sb.append("No hay productos críticos.\n");
                mostrarTexto("Stock Crítico", sb.toString());

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error reporte stock.");
            }
        }

        private void reportePedidosPorEstadoGUI() {
            try {
                Object[] estados = {"PENDIENTE", "CONFIRMADO", "EN_PREPARACION", "ENVIADO"};
                Object sel = JOptionPane.showInputDialog(this, "Estado:", "Reporte pedidos",
                        JOptionPane.QUESTION_MESSAGE, null, estados, estados[0]);
                if (sel == null) return;

                EstadoPedido est = EstadoPedido.valueOf(sel.toString());

                ArrayList<Pedido> list = sistema.getPedidos();
                StringBuilder sb = new StringBuilder();
                sb.append("=== PEDIDOS EN ESTADO ").append(est).append(" ===\n");

                boolean hay = false;
                for (Pedido p : list) {
                    if (p.getEstado() == est) {
                        hay = true;
                        sb.append(p).append("\n");
                    }
                }
                if (!hay) sb.append("No existen pedidos en ese estado.\n");

                mostrarTexto("Pedidos por Estado", sb.toString());

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error reporte pedidos por estado.");
            }
        }

        private void reporteVentasGUI() {
            try {
                ArrayList<Pedido> list = sistema.getPedidos();

                double total = 0;

                String[] codigos = new String[400];
                int[] cantidades = new int[400];
                int n = 0;

                for (Pedido p : list) {
                    if (p.getEstado() == EstadoPedido.PENDIENTE) continue;

                    total += p.getTotal();

                    for (int i = 0; i < p.getDetalles().size(); i++) {
                        DetallePedido d = p.getDetalles().get(i);

                        Producto prod = d.getProducto();
                        String cod = (prod != null) ? prod.getCodigo() : "N/A";

                        int idx = -1;
                        for (int k = 0; k < n; k++) {
                            if (codigos[k].equals(cod)) {
                                idx = k;
                                break;
                            }
                        }

                        if (idx == -1) {
                            codigos[n] = cod;
                            cantidades[n] = d.getCantidad();
                            n++;
                        } else {
                            cantidades[idx] += d.getCantidad();
                        }
                    }
                }

                StringBuilder sb = new StringBuilder();
                sb.append("=== RESUMEN DE VENTAS ===\n");
                sb.append("Total vendido (sin pendientes): $").append(total).append("\n\n");

                if (n == 0) {
                    sb.append("Aún no hay ventas.\n");
                    mostrarTexto("Ventas", sb.toString());
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

                sb.append("Producto más vendido: ").append(codigos[pos]).append(" (").append(max).append(" unidades)\n\n");
                sb.append("--- Detalle ---\n");
                for (int i = 0; i < n; i++) {
                    sb.append("- ").append(codigos[i]).append(": ").append(cantidades[i]).append("\n");
                }

                mostrarTexto("Ventas", sb.toString());

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error reporte ventas.");
            }
        }

        private void resetGUI() {
            try {
                if (!(usuarioActual instanceof Admin)) {
                    JOptionPane.showMessageDialog(this, "Solo ADMIN puede hacer RESET.");
                    return;
                }

                int op = JOptionPane.showConfirmDialog(this,
                        "¿Seguro? Se borrarán TODOS los .dat y se reinicia el sistema.",
                        "RESET",
                        JOptionPane.YES_NO_OPTION);

                if (op != JOptionPane.YES_OPTION) return;

                sistema.resetSistema();
                JOptionPane.showMessageDialog(this, "Sistema reiniciado.");

                mainPanel.refrescarTodo();
                logout();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error reseteando sistema.");
            }
        }
    }

    private void mostrarTexto(String titulo, String texto) {
        try {
            JTextArea area = new JTextArea(texto);
            area.setEditable(false);
            area.setFont(new Font("Consolas", Font.PLAIN, 12));
            JScrollPane sp = new JScrollPane(area);
            sp.setPreferredSize(new Dimension(900, 450));
            JOptionPane.showMessageDialog(this, sp, titulo, JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error mostrando texto.");
        }
    }
}