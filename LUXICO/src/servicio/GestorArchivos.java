package servicio;

import java.io.*;

public class GestorArchivos {

    public boolean guardarObjeto(String ruta, Object obj) {
        try {
            FileOutputStream fos = new FileOutputStream(ruta);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(obj);
            oos.close();
            fos.close();
            return true;

        } catch (FileNotFoundException fnfe) {
            System.out.println("No se pudo crear el archivo: " + ruta);
            return false;
        } catch (NotSerializableException nse) {
            System.out.println("Error: clase NO serializable. Falta implements Serializable en alguna clase.");
            return false;
        } catch (IOException ioe) {
            System.out.println("Error de entrada/salida al guardar: " + ruta);
            return false;
        } catch (Exception e) {
            System.out.println("Error general al guardar archivo.");
            return false;
        }
    }

    public Object cargarObjeto(String ruta) {
        try {
            File f = new File(ruta);
            if (!f.exists()) return null;

            FileInputStream fis = new FileInputStream(ruta);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Object obj = ois.readObject();
            ois.close();
            fis.close();
            return obj;

        } catch (EOFException eof) {
            System.out.println("Archivo vacío: " + ruta);
            return null;
        } catch (InvalidClassException ice) {
            System.out.println("El .dat no coincide con las clases actuales. Borra los .dat y ejecuta de nuevo.");
            return null;
        } catch (IOException ioe) {
            System.out.println("Error de entrada/salida al cargar: " + ruta);
            return null;
        } catch (ClassNotFoundException cnfe) {
            System.out.println("No se encontró una clase al leer el archivo.");
            return null;
        } catch (Exception e) {
            System.out.println("Error general al cargar archivo.");
            return null;
        }
    }

    public boolean eliminarArchivo(String ruta) {
        try {
            File f = new File(ruta);
            if (!f.exists()) return true;
            return f.delete();
        } catch (Exception e) {
            System.out.println("No se pudo eliminar: " + ruta);
            return false;
        }
    }
}