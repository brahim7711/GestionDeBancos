/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gestordebanca;

import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;

/**
 *
 * @author Usuario
 */
public class Conexion {
    Connection  conexion= null;
    String usuario = "root";
    String contraseña ="Economy1_";
    String url = "jdbc:mysql://localhost:3306/banco?useSSL=false&serverTimezone=UTC";
    
    public Connection estableceConnection(){
          if (conexion== null) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection(url,usuario,contraseña);
            JOptionPane.showMessageDialog(null,"Conectado","Conexion Establecida",JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,"No se conectó: "+e.toString(),"Error",JOptionPane.ERROR_MESSAGE);
        } }
        return conexion;
    }
    
    
}
