
package TP4SQL;

import java.util.ArrayList;
import Ferreteria.Teclado;
import Ferreteria.Proveedor;

public class VistaProveedor {
     // ARREGLO con el texto de las opciones: el menú se arma recorriéndolo con un for
    private static final String[] OPCIONES = {
        "Registrar proveedor",
        "Listar proveedores",
        "Buscar proveedor por nombre",
        "Modificar proveedor",
        "Dar de baja proveedor"
    };

    public int mostrarMenu() {
        System.out.println("--- MODULO PROVEEDOR (MySQL) ---");
        for (int i = 0; i < OPCIONES.length; i++) {
            System.out.println((i + 1) + ". " + OPCIONES[i]);
        }
        System.out.println("0. Volver al menu principal");
        return Teclado.LeerNum("Opcion: ");
    }

    // Devuelve un ARREGLO con los tres datos del formulario
    public String[] Agregar() {
        String[] datos = new String[3];
        datos[0] = Teclado.LeerTexto("Nombre: ");
        datos[1] = Teclado.LeerTexto("Telefono: ");
        datos[2] = Teclado.LeerTexto("Sitio web: ");
        return datos;
    }

    public String[] Modificar() {
        String[] datos = new String[2];
        datos[0] = Teclado.LeerTexto("Nuevo telefono (Enter para no cambiar): ");
        datos[1] = Teclado.LeerTexto("Nuevo sitio web (Enter para no cambiar): ");
        return datos;
    }

    public String pedirTexto(String mensaje) {
        return Teclado.LeerTexto(mensaje);
    }

    public int pedirId(String mensaje) {
        return Teclado.LeerNum(mensaje);
    }

    public boolean confirmar(String mensaje) {
        return Teclado.LeerTexto(mensaje + "(escriba confirmo): ").equalsIgnoreCase("confirmo");
    }

    public void mostrarProveedores(ArrayList<Proveedor> proveedores) {
        if (proveedores.isEmpty()) {
            System.out.println("No hay proveedores para mostrar.");
        }
        for (Proveedor p : proveedores) {
            System.out.println(p.descripcion());
        }
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    public void mostrarError(String mensaje) {
        System.out.println("Error: " + mensaje);
    }
}
