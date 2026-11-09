
package TP4SQL;

import java.sql.Connection;
import java.sql.SQLException;

public class PruebaConexion {
    
    public static void main(String[] args) {
 
        try (Connection conexion = Conexion.conectar()) {
            System.out.println("Conexion exitosa a la base: " + conexion.getCatalog());
        } catch (SQLException e) {
            System.out.println("No se pudo conectar: " + e.getMessage());
        }
    }
}
