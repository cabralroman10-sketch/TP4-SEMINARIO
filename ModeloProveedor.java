
package TP4SQL;

import Ferreteria.DatoInexistente;
import Ferreteria.Proveedor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;

public class ModeloProveedor implements ProveedorINT {
     private final Connection conexion;

    public ModeloProveedor(Connection conexion) {
        this.conexion = conexion;
    }

    @Override
    public Proveedor insertar(String nombre, String contacto, String sitioWeb) throws SQLException {
        // Se crea primero el objeto para que valide los datos (el nombre es obligatorio)
        Proveedor validado = new Proveedor(0, nombre, contacto, sitioWeb);

     
        String sql = "INSERT INTO proveedor (nombre, contacto, sitio_web) VALUES (?, ?, ?)";
        try (PreparedStatement sentencia = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            sentencia.setString(1, validado.getNombre());
            sentencia.setString(2, validado.getContacto());
            sentencia.setString(3, validado.getsitioWeb());
            sentencia.executeUpdate();

            try (ResultSet claves = sentencia.getGeneratedKeys()) {
                claves.next();
                return new Proveedor(claves.getInt(1), validado.getNombre(),
                        validado.getContacto(), validado.getsitioWeb());
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            // La base rechazó el INSERT: el nombre es UNIQUE y ya existe.
            throw new IllegalArgumentException("El proveedor ya existe");
        }
    }

    @Override
    public ArrayList<Proveedor> listar() throws SQLException {
        String sql = "SELECT id, nombre, contacto, sitio_web FROM proveedor ORDER BY id";
        ArrayList<Proveedor> lista = new ArrayList<>();
        try (PreparedStatement sentencia = conexion.prepareStatement(sql);
             ResultSet resultado = sentencia.executeQuery()) {
            while (resultado.next()) {              // next() avanza fila por fila hasta que no quedan más
                lista.add(armarProveedor(resultado));
            }
        }
        return lista;
    }

    @Override
    public ArrayList<Proveedor> buscarPorNombre(String texto) throws SQLException {
        String sql = "SELECT id, nombre, contacto, sitio_web FROM proveedor WHERE nombre LIKE ? ORDER BY id";
        ArrayList<Proveedor> lista = new ArrayList<>();
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setString(1, "%" + texto + "%");      
            try (ResultSet resultado = sentencia.executeQuery()) {
                while (resultado.next()) {
                    lista.add(armarProveedor(resultado));
                }
            }
        }
        return lista;
    }

    @Override
    public Proveedor buscarPorId(int id) throws SQLException, DatoInexistente {
        String sql = "SELECT id, nombre, contacto, sitio_web FROM proveedor WHERE id = ?";
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, id);
            try (ResultSet resultado = sentencia.executeQuery()) {
                if (resultado.next()) {
                    return armarProveedor(resultado);
                }
            }
        }
        throw new DatoInexistente("No existe el proveedor con ID " + id);
    }

    @Override
    public void actualizar(Proveedor proveedor) throws SQLException {
        String sql = "UPDATE proveedor SET nombre = ?, contacto = ?, sitio_web = ? WHERE id = ?";
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setString(1, proveedor.getNombre());
            sentencia.setString(2, proveedor.getContacto());
            sentencia.setString(3, proveedor.getsitioWeb());
            sentencia.setInt(4, proveedor.getID());
            sentencia.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new IllegalArgumentException("Ya existe otro proveedor con ese nombre");
        }
    }

    @Override
    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM proveedor WHERE id = ?";
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, id);
            sentencia.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e) {
            // La clave foránea de producto.proveedor_id impide borrar un proveedor que tiene productos
            throw new IllegalArgumentException("No se puede dar de baja: el proveedor tiene productos asociados");
        }
    }

    // Convierte la fila actual del ResultSet en un objeto Proveedor
    private Proveedor armarProveedor(ResultSet resultado) throws SQLException {
        return new Proveedor(
                resultado.getInt("id"),
                resultado.getString("nombre"),
                resultado.getString("contacto"),
                resultado.getString("sitio_web"));
    }
}
