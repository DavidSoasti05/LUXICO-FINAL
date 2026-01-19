package negocio;

import java.io.Serializable;

public class Cliente implements Serializable {
    private static final long serialVersionUID = 1L;
    private String cedula;
    private String nombre;
    private String direccion;
    private String telefono;
    private String correo;

    public Cliente(String cedula, String nombre, String direccion, String telefono, String correo) {
        try {
            if (!setCedula(cedula)) throw new Exception();
            if (nombre == null || nombre.trim().isEmpty()) throw new Exception();
            if (direccion == null || direccion.trim().isEmpty()) throw new Exception();
            if (!setTelefono(telefono)) throw new Exception();
            if (!setCorreo(correo)) throw new Exception();

            this.nombre = nombre.trim();
            this.direccion = direccion.trim();

        } catch (Exception e) {
            System.out.println("Error creando cliente: datos inválidos. NO se registró.");
            this.cedula = null;
            this.nombre = null;
            this.direccion = null;
            this.telefono = null;
            this.correo = null;
        }
    }

    public String getCedula() { return cedula; }
    public String getNombre() { return nombre; }
    public String getDireccion() { return direccion; }
    public String getTelefono() { return telefono; }
    public String getCorreo() { return correo; }

    public boolean setCedula(String cedula) {
        try {
            if (cedula == null) throw new Exception();
            cedula = cedula.replaceAll("[^0-9]", "");

            if (!esCedulaEcuatoriana(cedula)) throw new Exception();
            this.cedula = cedula;
            return true;

        } catch (Exception e) {
            System.out.println("Error: cédula ecuatoriana inválida (debe ser de 10 dígitos y válida).");
            return false;
        }
    }

    public boolean setNombre(String nombre) {
        try {
            if (nombre == null || nombre.trim().isEmpty()) throw new Exception();
            this.nombre = nombre.trim();
            return true;
        } catch (Exception e) {
            System.out.println("Error: nombre inválido.");
            return false;
        }
    }

    public boolean setDireccion(String direccion) {
        try {
            if (direccion == null || direccion.trim().isEmpty()) throw new Exception();
            this.direccion = direccion.trim();
            return true;
        } catch (Exception e) {
            System.out.println("Error: dirección inválida.");
            return false;
        }
    }

    public boolean setTelefono(String telefono) {
        try {
            if (telefono == null) throw new Exception();
            telefono = telefono.trim();

            if (telefono.length() != 10) throw new Exception();

            for (int i = 0; i < telefono.length(); i++) {
                if (!Character.isDigit(telefono.charAt(i))) throw new Exception();
            }

            if (!telefono.startsWith("09")) throw new Exception();

            this.telefono = telefono;
            return true;

        } catch (Exception e) {
            System.out.println("Error: teléfono inválido (Ecuador: inicia con 09 y tiene 10 dígitos).");
            return false;
        }
    }

    public boolean setCorreo(String correo) {
        try {
            if (!validarCorreoPermitido(correo)) throw new Exception();
            this.correo = correo.trim().toLowerCase();
            return true;
        } catch (Exception e) {
            System.out.println("Error: correo inválido (solo @gmail.com, @outlook.com, @hotmail.com, @icloud.com).");
            return false;
        }
    }

    // ---------------- VALIDACIONES (TRY/CATCH) ----------------
    // Validación de cédula ecuatoriana:
    // - 10 dígitos numéricos
    // - provincia 01..24
    // - tercer dígito 0..5 (persona natural)
    // - dígito verificador con módulo 10
    public static boolean esCedulaEcuatoriana(String cedula) {
        try {
            if (cedula == null) return false;

            cedula = cedula.replaceAll("[^0-9]", "");
            if (cedula.length() != 10) return false;

            // solo dígitos
            for (int i = 0; i < 10; i++) {
                if (cedula.charAt(i) < '0' || cedula.charAt(i) > '9') return false;
            }

            int provincia = Integer.parseInt(cedula.substring(0, 2));
            if (provincia < 1 || provincia > 24) return false;

            int tercer = Integer.parseInt(cedula.substring(2, 3));
            if (tercer >= 6) return false; // cédula natural

            int[] coef = {2,1,2,1,2,1,2,1,2};
            int suma = 0;

            for (int i = 0; i < 9; i++) {
                int dig = cedula.charAt(i) - '0';
                int prod = dig * coef[i];
                if (prod >= 10) prod -= 9;
                suma += prod;
            }

            int mod = suma % 10;
            int verificador = (mod == 0) ? 0 : (10 - mod);

            int ultimo = cedula.charAt(9) - '0';
            return verificador == ultimo;

        } catch (Exception e) {
            return false;
        }
    }

    public static boolean validarCorreoPermitido(String correo) {
        try {
            if (correo == null) throw new Exception();
            correo = correo.trim().toLowerCase();
            int at = correo.indexOf('@');
            if (at <= 0) throw new Exception();

            // dominios permitidos
            boolean ok = correo.endsWith("@gmail.com") ||
                    correo.endsWith("@outlook.com") ||
                    correo.endsWith("@hotmail.com") ||
                    correo.endsWith("@icloud.com") ||
                    correo.endsWith("@yahoo.com");

            if (!ok) throw new Exception();

            if (correo.contains(" ")) throw new Exception();

            return true;

        } catch (Exception e) {
            return false;
        }
    }

    public boolean esValido() {
        try {
            return cedula != null && nombre != null && direccion != null && telefono != null && correo != null;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String toString() {
        try {
            return cedula + " | " + nombre + " | " + telefono + " | " + correo;
        } catch (Exception e) {
            return "Cliente inválido";
        }
    }
}