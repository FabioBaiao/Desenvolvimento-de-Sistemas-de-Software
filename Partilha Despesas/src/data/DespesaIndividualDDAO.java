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
public class DespesaIndividualDDAO {
    private int id;
    private Connection conn;

    public DespesaIndividualDDAO(int id) {
        this.id = id;
    }
    
    public DespesaIndividual put(String email, DespesaIndividual despesaIndividual){
        try{
            conn = Connect.connect();
            String sql = "INSERT INTO despesaindividual (Despesa_idDespesa, Morador_Email, Descricao, Valor, Criador, DataLimite, Tipo)\n" +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)\n" +
                    "ON DUPLICATE KEY UPDATE Descricao=VALUES(Descricao), Valor=VALUES(Valor), DataLimite=VALUES(DataLimite)";
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setInt(1, this.id);
            stm.setString(2, email);
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
    
    public Set<DespesaIndividual> values(){
        Set<DespesaIndividual> set = new HashSet<>();
        try{
            conn = Connect.connect();
            String sql = "SELECT *\n" +
                    "FROM despesaindividual\n" +
                    "WHERE Despesa_idDespesa=?";
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setInt(1, this.id);
            
            ResultSet rs = stm.executeQuery();
            while(rs.next()){
                set.add(new DespesaIndividual(rs.getString("Criador"), rs.getString("Descricao"), rs.getDouble("Valor"), 
                        this.id, rs.getString("Morador_Email"), rs.getString("DataLimite"), rs.getString("Tipo")));
            }
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        } finally {
            Connect.close(conn);
        }
        return set;
    }
    
    public Set<String> keySet(){
        Set<String> set = new HashSet<>();
        try{
            conn = Connect.connect();
            String sql = "SELECT Morador_Email\n" +
                    "FROM despesaindividual\n" +
                    "WHERE Despesa_idDespesa=?";
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setInt(1, this.id);
            
            ResultSet rs = stm.executeQuery();
            while(rs.next()){
                set.add(rs.getString("Morador_Email"));
            }
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        } finally {
            Connect.close(conn);
        }
        return set;
    }
    
}
