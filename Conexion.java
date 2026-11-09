
package TP4SQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private static final String URL = "jdbc:mysql://localhost:3306/TP4seminarioSQL";
    private static final String USUARIO = "root";
    private static final String CLAVE = "1403"; 

    // Abre y devuelve una conexión nueva, si falla, lanza SQLException
    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CLAVE);
    }
}
