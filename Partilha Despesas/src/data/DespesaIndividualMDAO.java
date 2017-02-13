/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import business.DespesaIndividual;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Fábio Baião
 */
public class DespesaIndividualMDAO {
    private String email;
    private Connection conn;
    
    public DespesaIndividualMDAO(String email) {
        this.email = email;
    }
    
    public DespesaIndividual put(int id, DespesaIndividual despesaIndividual){
        try{
            conn = Connect.connect();
            String sql = "INSERT INTO despesaindividual (Despesa_idDespesa, Morador_Email, Descricao, Valor, Criador, DataLimite, Tipo)\n" +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)\n" +
                    "ON DUPLICATE KEY UPDATE Descricao=VALUES(Descricao), Valor=VALUES(Valor), DataLimite=VALUES(DataLimite)";
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setInt(1, id);
            stm.setString(2, this.email);
            stm.setString(3, despesaIndividual.getDescricao());
            stm.setDouble(4, despesaIndividual.getValor());
            stm.setString(5, despesaIndividual.getEmailCriador());
            stm.setString(6, despesaIndividual.getDataLimite());
            stm.setString(7, despesaIndividual.getTipo());
            stm.executeUpdate();
            
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        } finally {
            Connect.close(conn);
        }
        return null;
    }
    
    public DespesaIndividual remove(int id){
        DespesaIndividual d = this.get(id);
        try{
            conn = Connect.connect();
            String sql = "DELETE\n" +
                    "FROM despesaindividual\n" +
                    "WHERE Despesa_idDespesa=? AND Morador_Email=?";
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
    
    public DespesaIndividual get(int id){
        DespesaIndividual d = null;
        try{
            conn = Connect.connect();
            String sql = "SELECT *\n" +
                    "FROM despesaindividual\n" +
                    "WHERE Despesa_idDespesa=? AND Morador_Email=?";
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setInt(1, id);
            stm.setString(2, this.email);
            
            ResultSet rs = stm.executeQuery();
            if (rs.next()){
                d = new DespesaIndividual (rs.getString("Criador"), rs.getString("Descricao"), rs.getDouble("Valor"), id, this.email, rs.getString("DataLimite"), rs.getString("Tipo"));
            }
        } catch(SQLException | ClassNotFoundException ex){
            ex.printStackTrace();
        } finally {
            Connect.close(conn);
        }
        return d;
    }
    
    public Set<DespesaIndividual> values(){
        Set<DespesaIndividual> set = new HashSet<>();
        try{
            conn = Connect.connect();
            String sql = "SELECT *\n" +
                    "FROM despesaindividual\n" +
                    "WHERE Morador_Email=?";
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setString(1, this.email);
            
            ResultSet rs = stm.executeQuery();
            while(rs.next()){
                set.add(new DespesaIndividual(rs.getString("Criador"), rs.getString("Descricao"), rs.getDouble("Valor"), 
                        rs.getInt("Despesa_idDespesa"), this.email, rs.getString("DataLimite"), rs.getString("Tipo")));
            }
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        } finally {
            Connect.close(conn);
        }
        return set;
    }
    
}
