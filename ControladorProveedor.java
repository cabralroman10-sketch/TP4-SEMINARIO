
package TP4SQL;

import Ferreteria.DatoInexistente;
import Ferreteria.Proveedor;
import java.util.ArrayList;
import java.sql.SQLException;


public class ControladorProveedor {
    private final ProveedorINT modelo;
    private final VistaProveedor vista;

    public ControladorProveedor(ProveedorINT modelo, VistaProveedor vista) {
        this.modelo = modelo;
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
                        listar(); 
                        break;
                    case 3: 
                        buscar(); 
                        break;
                    case 4: 
                        modificar(); 
                        break;
                    case 5: 
                        darDeBaja(); 
                        break;
                    case 0: 
                        break;
                    default: vista.mostrarMensaje("Opcion invalida.");
                }
            } catch (SQLException e) {
                
                // Cualquier problema con la base de datos llega acá
                vista.mostrarError("Fallo la base de datos: " + e.getMessage());
            } 
            catch (DatoInexistente | IllegalArgumentException e) {
                vista.mostrarError(e.getMessage());
            }
        } while (opcion != 0);
    }

    private void registrar() throws SQLException {
        String[] datos = vista.Agregar();       
        Proveedor nuevo = modelo.insertar(datos[0], datos[1], datos[2]);
        vista.mostrarMensaje("Proveedor registrado: " + nuevo.descripcion());
    }

    private void listar() throws SQLException {
        vista.mostrarProveedores(modelo.listar());
    }

    private void buscar() throws SQLException {
        ArrayList<Proveedor> encontrados = modelo.buscarPorNombre(vista.pedirTexto("Nombre a buscar: "));
        if (encontrados.isEmpty()) {
            vista.mostrarMensaje("No existen resultados.");
        } else {
            vista.mostrarProveedores(encontrados);
        }
    }

    private void modificar() throws SQLException, DatoInexistente {
        listar();
        Proveedor p = modelo.buscarPorId(vista.pedirId("ID del proveedor a modificar: "));
        String[] nuevos = vista.Modificar();  
        if (!nuevos[0].isEmpty()) {
            p.setContacto(nuevos[0]);
        }
        if (!nuevos[1].isEmpty()) {
            p.setSitioWeb(nuevos[1]);
        }
        modelo.actualizar(p);                               // UPDATE en la base de datos
        vista.mostrarMensaje("Proveedor actualizado: " + p.descripcion());
    }

    private void darDeBaja() throws SQLException, DatoInexistente {
        listar();
        Proveedor p = modelo.buscarPorId(vista.pedirId("ID del proveedor a dar de baja: "));
        if (vista.confirmar("Confirmar baja de " + p.getNombre())) {
            modelo.eliminar(p.getID());                     // DELETE en la base de datos
            vista.mostrarMensaje("Proveedor dado de baja");
        } else {
            vista.mostrarMensaje("Operacion cancelada");
        }
    }
}
