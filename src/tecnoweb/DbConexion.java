/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tecnoweb;

/**
 *
 * @author daniy
 */
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class DbConexion {
    private Statement st = null;
    private Connection connection = null;
    private String url = "";
    
    /**
     * @param host <code>String</code> host name or ip. Nombre del host o ip.
     * @param port <code>String</code> listening database port. Puerto en el que escucha la base de datos.
     * @param database <code>String</code> database name for the connection. Nombre de la base de datos para la conexión.
     * @param user <code>String</code> user name. Nombre de usuario.
     * @param password  <code>String</code> user password. Password del usuario.
     */
    public DbConexion(String host, String port, String database,String user, String password){
        try {
            try {
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException ex) {
                System.out.println("Error al registrar el driver de PostgreSQL: " + ex);
            }
            this.connection = null;
            this.url = "jdbc:postgresql://" + host + ":" + port + "/" + database;
            this.connection = DriverManager.getConnection(url,user, password);           
            boolean valid = this.connection.isValid(50000);
            System.out.println(valid ? "TEST OK" : "TEST FAIL");
        } catch (java.sql.SQLException sqle) { 
            System.out.println("Error al conectar con la base de datos de PostgreSQL (" + url + "): " + sqle);
        }
    }
    public void CloseConexion(){
        try {
            connection.close();
        } catch (SQLException e) {
        }
    }
    @SuppressWarnings("empty-statement")
    public List<String> Query(String query){
    System.out.println(query);
    List<String> personas = new ArrayList<>();
        try {
            this.st = this.connection.createStatement();
            try (ResultSet rs = this.st.executeQuery(query)) {
                while    ( rs.next() ) {
                    String  nombre= rs.getString("per_nom");
                    personas.add(nombre.trim());
                    System.out.println( "Persona: " + nombre );
                }  
                rs.close();
            }
           ;
        this.st.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
        return personas;
    }
    
    
    
    public static void main(String[] args) {
        DbConexion db = new DbConexion("www.tecnoweb.org.bo", "5432", "db_agenda","agenda", "agendaagenda");
        List<String> personas = new ArrayList<>();
        personas=db.Query("SELECT * FROM persona WHERE per_nom ILIKE '%e%'");
        System.out.println(personas.toString());
    }
}
