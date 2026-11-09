
package TP4SQL;

import Ferreteria.DatoInexistente;
import java.util.ArrayList;
import java.sql.SQLException;
import Ferreteria.Proveedor;


public interface ProveedorINT {
    Proveedor insertar(String nombre, String contacto, String sitioWeb) throws SQLException;

    ArrayList<Proveedor> listar() throws SQLException;

    ArrayList<Proveedor> buscarPorNombre(String texto) throws SQLException;

    Proveedor buscarPorId(int id) throws SQLException, DatoInexistente;

    void actualizar(Proveedor proveedor) throws SQLException;

    void eliminar(int id) throws SQLException;
}
