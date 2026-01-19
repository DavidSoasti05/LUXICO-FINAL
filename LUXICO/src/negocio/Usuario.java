package negocio;

import java.io.Serializable;

public abstract class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;
    protected String usuario;
    protected String clave;
    protected String nombre;

    public Usuario(String usuario, String clave, String nombre) {
        try {
            if (usuario == null || usuario.trim().isEmpty()) throw new Exception();
            if (clave == null || clave.trim().length() < 4) throw new Exception();
            if (nombre == null || nombre.trim().isEmpty()) throw new Exception();

            this.usuario = usuario.trim();
            this.clave = clave.trim();
            this.nombre = nombre.trim();

        } catch (Exception e) {
            System.out.println("Error creando usuario: datos inválidos.");
            this.usuario = "invalido";
            this.clave = "0000";
            this.nombre = "SinNombre";
        }
    }

    public abstract RolUsuario getRol();

    public String getUsuario() { return usuario; }
    public String getClave() { return clave; }
    public String getNombre() { return nombre; }
}