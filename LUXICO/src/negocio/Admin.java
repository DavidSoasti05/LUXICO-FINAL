package negocio;

import java.io.Serializable;

public class Admin extends Usuario implements Serializable {
    private static final long serialVersionUID = 1L;
    public Admin(String usuario, String clave, String nombre) {
        super(usuario, clave, nombre);
    }

    @Override
    public RolUsuario getRol() {
        return RolUsuario.ADMIN;
    }
}