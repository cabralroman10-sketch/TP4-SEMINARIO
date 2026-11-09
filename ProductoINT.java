
package TP4SQL;

import Ferreteria.DatoInexistente;
import Ferreteria.Producto;
import Ferreteria.Proveedor;
import java.util.ArrayList;
import java.sql.SQLException;

public interface ProductoINT {
     Producto insertar(String nombre, double precio, int stock, int stockMin, Proveedor proveedor) throws SQLException;

    ArrayList<Producto> listar() throws SQLException;

    ArrayList<Producto> listarPorPrecio() throws SQLException;

    ArrayList<Producto> buscarPorNombre(String texto) throws SQLException;

    Producto buscarPorId(int id) throws SQLException, DatoInexistente;

    ArrayList<Producto> listarPorProveedor(int idProveedor) throws SQLException;

    void actualizarPrecio(int id, double nuevoPrecio) throws SQLException;

    void eliminar(int id) throws SQLException;
}
