
package TP4SQL;

import Ferreteria.DatoInexistente;
import Ferreteria.Producto;
import Ferreteria.Proveedor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;


public class ModeloProducto implements ProductoINT{
    // Consulta base con JOIN: une cada producto con su proveedor en una sola consulta,
    // así se puede armar el Producto con su Proveedor completo. Cada método le agrega su WHERE / ORDER BY.
    private static final String SELECT_BASE =
            "SELECT p.id, p.nombre, p.precio, p.stock, p.stock_min, "
          + "pr.id AS proveedor_id, pr.nombre AS proveedor_nombre, pr.contacto, pr.sitio_web "
          + "FROM producto p INNER JOIN proveedor pr ON p.proveedor_id = pr.id ";

    private final Connection conexion;

    public ModeloProducto(Connection conexion) {
        this.conexion = conexion;
    }

    @Override
    public Producto insertar(String nombre, double precio, int stock, int stockMin, Proveedor proveedor)
            throws SQLException {
        Producto validado = new Producto(0, nombre, precio, stock, stockMin, proveedor);

        String sql = "INSERT INTO producto (nombre, precio, stock, stock_min, proveedor_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement sentencia = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            sentencia.setString(1, validado.getNombre());
            sentencia.setDouble(2, validado.getPrecio());
            sentencia.setInt(3, validado.getStock());
            sentencia.setInt(4, validado.getStockMin());
            sentencia.setInt(5, proveedor.getID());
            sentencia.executeUpdate();

            try (ResultSet claves = sentencia.getGeneratedKeys()) {
                claves.next();
                return new Producto(claves.getInt(1), validado.getNombre(), validado.getPrecio(),
                        validado.getStock(), validado.getStockMin(), proveedor);
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            if (e.getErrorCode() == 1062) {
                throw new IllegalArgumentException("El producto ya existe");
            }
            throw new IllegalArgumentException("El proveedor indicado no existe");
        }
    }

    @Override
    public ArrayList<Producto> listar() throws SQLException {
        return consultar(SELECT_BASE + "ORDER BY p.id", null);
    }

    @Override
    public ArrayList<Producto> listarPorPrecio() throws SQLException {
        // Acá no hace falta un algoritmo de ordenamiento propio: lo hace MySQL con ORDER BY.
        return consultar(SELECT_BASE + "ORDER BY p.precio, p.id", null);
    }

    @Override
    public ArrayList<Producto> buscarPorNombre(String texto) throws SQLException {
        return consultar(SELECT_BASE + "WHERE p.nombre LIKE ? ORDER BY p.id", "%" + texto + "%");
    }

    @Override
    public Producto buscarPorId(int id) throws SQLException, DatoInexistente {
        ArrayList<Producto> resultado = consultar(SELECT_BASE + "WHERE p.id = ?", id);
        if (resultado.isEmpty()) {
            throw new DatoInexistente("No existe el producto con ID " + id);
        }
        return resultado.get(0);
    }

    @Override
    public ArrayList<Producto> listarPorProveedor(int idProveedor) throws SQLException {
        return consultar(SELECT_BASE + "WHERE pr.id = ? ORDER BY p.id", idProveedor);
    }

    @Override
    public void actualizarPrecio(int id, double nuevoPrecio) throws SQLException {
        if (nuevoPrecio <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a cero");
        }
        String sql = "UPDATE producto SET precio = ? WHERE id = ?";
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setDouble(1, nuevoPrecio);
            sentencia.setInt(2, id);
            sentencia.executeUpdate();
        }
    }

    @Override
    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM producto WHERE id = ?";
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, id);
            sentencia.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new IllegalArgumentException("No se puede dar de baja: el producto tiene operaciones asociadas");
        }
    }


    private ArrayList<Producto> consultar(String sql, Object parametro) throws SQLException {
        ArrayList<Producto> lista = new ArrayList<>();
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            if (parametro != null) {
                sentencia.setObject(1, parametro);
            }
            try (ResultSet resultado = sentencia.executeQuery()) {
                while (resultado.next()) {
                    lista.add(armarProducto(resultado));
                }
            }
        }
        return lista;
    }

    // Convierte la fila actual en un Producto, armando también su Proveedor con las columnas del JOIN.
    private Producto armarProducto(ResultSet resultado) throws SQLException {
        Proveedor proveedor = new Proveedor(
                resultado.getInt("proveedor_id"),
                resultado.getString("proveedor_nombre"),
                resultado.getString("contacto"),
                resultado.getString("sitio_web"));
        return new Producto(
                resultado.getInt("id"),
                resultado.getString("nombre"),
                resultado.getDouble("precio"),
                resultado.getInt("stock"),
                resultado.getInt("stock_min"),
                proveedor);
    }
}
