
package TP4SQL;

import Ferreteria.Teclado;
import Ferreteria.Formato;
import Ferreteria.Producto;
import Ferreteria.Proveedor;
import java.util.ArrayList;

public class VistaProductos {
    // ARREGLO con las opciones del menú
    private static final String[] OPCIONES = {
        "Registrar producto",
        "Listar productos (ordenados por precio)",
        "Buscar producto por nombre",
        "Buscar producto por ID",
        "Modificar precio",
        "Productos de un proveedor",
        "Dar de baja producto"
    };

    public int mostrarMenu() {
        System.out.println("--- MODULO PRODUCTOS (MySQL) ---");
        for (int i = 0; i < OPCIONES.length; i++) {
            System.out.println((i + 1) + ". " + OPCIONES[i]);
        }
        System.out.println("0. Volver al menu principal");
        return Teclado.LeerNum("Opcion: ");
    }

    public String pedirTexto(String mensaje) {
        return Teclado.LeerTexto(mensaje);
    }

    public int pedirEntero(String mensaje) {
        return Teclado.LeerNum(mensaje);
    }

    public double pedirDecimal(String mensaje) {
        return Teclado.LeerDecimal(mensaje);
    }

    public boolean confirmar(String mensaje) {
        return Teclado.LeerTexto(mensaje + " (escriba confirmo): ").equalsIgnoreCase("confirmo");
    }

    // Muestra los productos como una tabla, los que están bajo el mínimo llevan una alerta
    public void mostrarProductos(ArrayList<Producto> productos) {
        if (productos.isEmpty()) {
            System.out.println("No hay productos para mostrar.");
            return;
        }
        System.out.printf("%-4s %-24s %14s %6s %4s  %s%n", "ID", "Producto", "Precio", "Stock", "Min", "Proveedor");
        for (Producto p : productos) {
            String alerta = p.estaBajoMin() ? "   ** BAJO MINIMO **" : "";
            System.out.printf("%-4d %-24s %14s %6d %4d  %s%s%n", p.getID(), p.getNombre(),
                    Formato.dinero(p.getPrecio()), p.getStock(), p.getStockMin(),
                    p.getProveedor().getNombre(), alerta);
        }
    }

    public void mostrarProveedores(ArrayList<Proveedor> proveedores) {
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
