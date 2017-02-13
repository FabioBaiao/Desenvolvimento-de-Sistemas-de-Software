/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import business.Despesa;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Fábio Baião
 */
public class DespesaDAO {

    private String email;
    private Connection conn;

    public DespesaDAO(String email) {
        this.email = email;
    }
    
    public Despesa put(int id, Despesa despesa){
        try {
            conn = Connect.connect();
            String sql = "INSERT INTO despesa (idDespesa, Morador_Email, Descricao, ValorTotal, ValorAPagar, DataLimite, Tipo)\n" + 
                    "VALUES (?, ?, ?, ?, ?, ?, ?)\n" +
                    "ON DUPLICATE KEY UPDATE Descricao=VALUES(Descricao), ValorTotal=VALUES(ValorTotal), ValorAPagar=VALUES(ValorAPagar), DataLimite=VALUES(DataLimite)"; 
            PreparedStatement stm = conn.prepareStatement(sql);//, Statement.RETURN_GENERATED_KEYS);
            stm.setInt(1, id);
            stm.setString(2, this.email);
            stm.setString(3, despesa.getDescricao());
            stm.setDouble(4, despesa.getValorTotal());
            stm.setDouble(5, despesa.getValorAPagar());
            stm.setString(6, despesa.getDataLimite());
            stm.setString(7, despesa.getTipo());
            stm.executeUpdate();
            
            //
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        } finally {
            Connect.close(conn);
        }
        return null;
    }
    
    public Despesa get(int id){
        Despesa d = null;
        try{
            conn = Connect.connect();
            String sql = "SELECT *\n" +
                    "FROM despesa\n" +
                    "WHERE idDespesa=? AND Morador_Email=?";
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setInt(1, id);
            stm.setString(2, this.email);
            
            ResultSet rs = stm.executeQuery();
            if (rs.next()){
                d = new Despesa (id, rs.getString("Descricao"), rs.getDouble("ValorTotal"), rs.getDouble("ValorAPagar"), rs.getString("DataLimite"), rs.getString("Tipo"));
            }
        } catch(SQLException | ClassNotFoundException ex){
            ex.printStackTrace();
        } finally {
            Connect.close(conn);
        }
        return d;
    }
    
    public Despesa remove(int id){
        Despesa d = this.get(id);
        try{
            conn = Connect.connect();
            String sql = "DELETE\n" +
                    "FROM despesa\n" +
                    "WHERE idDespesa=? AND Morador_Email=?";
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setInt(1, id);
            stm.setString(2, this.email);
            stm.executeUpdate();
            
        } catch(SQLException | ClassNotFoundException ex){
            ex.printStackTrace();
        } finally {
            Connect.close(conn);
        }
        return d;
    }
    
    public Set<Despesa> values(){
        Set<Despesa> set = new HashSet<>();
        try{
            conn = Connect.connect();
            String sql = "SELECT *\n" +
                    "FROM despesa\n" +
                    "WHERE Morador_Email=?";
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setString(1, this.email);
            
            ResultSet rs = stm.executeQuery();
            while (rs.next()){
                set.add(new Despesa(rs.getInt("idDespesa"), rs.getString("Descricao"), rs.getDouble("ValorTotal"), 
                        rs.getDouble("ValorAPagar"), rs.getString("DataLimite"), rs.getString("Tipo")));
            }
        } catch(SQLException | ClassNotFoundException ex){
            ex.printStackTrace();
        } finally {
            Connect.close(conn);
        }
        return set;
    }
    
    public static int getID() {
        int id = 0;
        Connection conn = null;
        try{
            conn = Connect.connect();
            String sql = "SELECT idDespesa\n" +
                    "FROM despesa\n" +
                    "ORDER BY idDespesa DESC\n" + 
                    "LIMIT 1";
            PreparedStatement stm = conn.prepareStatement(sql);
            
            ResultSet rs = stm.executeQuery();
            if (rs.next()){
                id = rs.getInt("idDespesa");
            }
            
        } catch(SQLException | ClassNotFoundException ex){
            ex.printStackTrace();
        } finally {
            Connect.close(conn);
        }
        return id;
    }
}
