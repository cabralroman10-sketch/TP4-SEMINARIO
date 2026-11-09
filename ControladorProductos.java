
package TP4SQL;

import java.sql.SQLException;
import Ferreteria.DatoInexistente;
import Ferreteria.Producto;
import Ferreteria.Proveedor;
import java.util.ArrayList;

public class ControladorProductos {
    private final ProductoINT modelo;
    private final ProveedorINT modeloProveedor;
    private final VistaProductos vista;

    public ControladorProductos(ProductoINT modelo, ProveedorINT modeloProveedor, VistaProductos vista) {
        this.modelo = modelo;
        this.modeloProveedor = modeloProveedor;
        this.vista = vista;
    }

    public void ejecutar() {
        int opcion;
        do {
            opcion = vista.mostrarMenu();
            try {
                switch (opcion) {
                    case 1: 
                        registrar(); 
                        break;
                    case 2: 
                        vista.mostrarProductos(modelo.listarPorPrecio()); 
                        break;
                    case 3: 
                        buscarPorNombre(); 
                        break;
                    case 4: 
                        buscarPorId(); 
                        break;
                    case 5: 
                        modificarPrecio(); 
                        break;
                    case 6: 
                        productosDeProveedor(); 
                        break;
                    case 7: 
                        darDeBaja(); 
                        break;
                    case 0: 
                        break;
                    default: vista.mostrarMensaje("Opcion invalida.");
                }
            } 
            catch (SQLException e) {
                vista.mostrarError("Fallo la base de datos: " + e.getMessage());
            } 
            catch (DatoInexistente | IllegalArgumentException e) {
                vista.mostrarError(e.getMessage());
            }
        } while (opcion != 0);
    }

    private void registrar() throws SQLException, DatoInexistente {
        String nombre = vista.pedirTexto("Nombre: ");
        double precio = vista.pedirDecimal("Precio de venta: ");
        int stock = vista.pedirEntero("Stock inicial: ");
        int stockMin = vista.pedirEntero("Stock minimo: ");
        
        vista.mostrarProveedores(modeloProveedor.listar());
        Proveedor proveedor = modeloProveedor.buscarPorId(vista.pedirEntero("ID del proveedor: "));
        Producto nuevo = modelo.insertar(nombre, precio, stock, stockMin, proveedor);
        vista.mostrarMensaje("Producto registrado: " + nuevo);
    }

    private void buscarPorNombre() throws SQLException {
        ArrayList<Producto> encontrados = modelo.buscarPorNombre(vista.pedirTexto("Nombre a buscar: "));
        if (encontrados.isEmpty()) {
            vista.mostrarMensaje("No existen resultados.");
        } else {
            vista.mostrarProductos(encontrados);
        }
    }

    private void buscarPorId() throws SQLException, DatoInexistente {
        Producto p = modelo.buscarPorId(vista.pedirEntero("ID del producto: "));
        vista.mostrarMensaje(p.toString());
    }

    private void modificarPrecio() throws SQLException, DatoInexistente {
        vista.mostrarProductos(modelo.listar());
        Producto p = modelo.buscarPorId(vista.pedirEntero("ID del producto a modificar: "));
        vista.mostrarMensaje("Precio actual: " + p.getPrecio());
        p.setPrecio(vista.pedirDecimal("Nuevo precio: "));      // el objeto valida que sea mayor a cero
        modelo.actualizarPrecio(p.getID(), p.getPrecio());      // UPDATE en la base de datos
        vista.mostrarMensaje("Producto actualizado: " + p);
    }

    private void productosDeProveedor() throws SQLException, DatoInexistente {
        vista.mostrarProveedores(modeloProveedor.listar());
        Proveedor proveedor = modeloProveedor.buscarPorId(vista.pedirEntero("ID del proveedor: "));
        ArrayList<Producto> productos = modelo.listarPorProveedor(proveedor.getID());
        if (productos.isEmpty()) {
            vista.mostrarMensaje("El proveedor no tiene productos asociados.");
        } else {
            vista.mostrarProductos(productos);
        }
    }

    private void darDeBaja() throws SQLException, DatoInexistente {
        vista.mostrarProductos(modelo.listar());
        Producto p = modelo.buscarPorId(vista.pedirEntero("ID del producto a dar de baja: "));
        if (vista.confirmar("Confirmar baja de " + p.getNombre())) {
            modelo.eliminar(p.getID());                         // DELETE en la base de datos
            vista.mostrarMensaje("Producto dado de baja.");
        } else {
            vista.mostrarMensaje("Operacion cancelada.");
        }
    }
}
