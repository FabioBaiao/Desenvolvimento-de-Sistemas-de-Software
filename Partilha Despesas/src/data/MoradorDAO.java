/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import business.Morador;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Fábio Baião
 */
public class MoradorDAO {
    private Connection conn;
    
    
    public boolean containsKey(String email){
        boolean r = false;
        try {
            conn = Connect.connect();
            String sql = "SELECT *\n" +
                    "FROM morador\n" +
                    "WHERE Email=?";
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setString(1, email);
            ResultSet rs = stm.executeQuery();
            r = rs.next();
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        } finally {
            Connect.close(conn);
        }
        return r;
    }
    
    public Morador get(String email){
        Morador m = null;
        try{
            conn = Connect.connect();
            String sql = "SELECT *\n" +
                    "FROM morador\n" +
                    "WHERE Email=?";
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setString(1, email);
            
            ResultSet rs = stm.executeQuery();
            if (rs.next()){
                m = new Morador (rs.getString("Nome"), email, rs.getString("Password"));
            }
        } catch(SQLException | ClassNotFoundException ex){
            ex.printStackTrace();
        } finally {
            Connect.close(conn);
        }
        return m;
    }
    
    public Morador put(String email, Morador morador){
        try{
            conn = Connect.connect();
            String sql = "INSERT INTO morador (Nome, Email, Password)\n" +
                    "VALUES (?, ?, ?)";
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setString(1, morador.getNome());
            stm.setString(2, email);
            stm.setString(3, morador.getPassword());
            stm.executeUpdate();
            
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        } finally {
            Connect.close(conn);
        }
        return null;
    }
}
