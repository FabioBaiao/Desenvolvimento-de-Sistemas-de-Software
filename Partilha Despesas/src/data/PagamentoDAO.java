/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import business.Pagamento;
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
public class PagamentoDAO {
    private int idDespesa;
    private String devedor;
    private Connection conn;

    public PagamentoDAO(int idDespesa, String devedor) {
        this.idDespesa = idDespesa;
        this.devedor = devedor;
    }
    
    public Set<Pagamento> values(){
        Set<Pagamento> set = new HashSet<>();
        try{
            conn = Connect.connect();
            String sql = "SELECT *\n" +
                    "FROM pagamento\n" +
                    "WHERE DespesaIndividual_Morador_Email=? AND DespesaIndividual_Despesa_idDespesa=?";
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setString(1, this.devedor);
            stm.setInt(2, this.idDespesa);
            
            ResultSet rs = stm.executeQuery();
            while(rs.next()){
                set.add(new Pagamento(rs.getInt("idPagamento"), rs.getDouble("Valor"), rs.getString("Tipo"), rs.getString("Data")));
            }
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        } finally {
            Connect.close(conn);
        }
        return set; 
    } 
    
    public Pagamento put(int idPagamento, Pagamento pagamento){
        try{
            conn = Connect.connect();
            String sql = "INSERT INTO pagamento (idPagamento, DespesaIndividual_Morador_Email, DespesaIndividual_Despesa_idDespesa, Valor, Tipo, Data)\n" +
                    "VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setInt(1, idPagamento);
            stm.setString(2, this.devedor);
            stm.setInt(3, this.idDespesa);
            stm.setDouble(4, pagamento.getValor());
            stm.setString(5, pagamento.getTipo());
            stm.setString(6, pagamento.getData());
            stm.executeUpdate();
            
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        } finally {
            Connect.close(conn);
        }
        return null;
    }
    
    public Pagamento remove(int idPagamento){
        Pagamento p = this.get(idPagamento);
        try{
            conn = Connect.connect();
            String sql = "DELETE\n" +
                    "FROM pagamento\n" +
                    "WHERE DespesaIndividual_Despesa_idDespesa=? AND idPagamento=?";
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setInt(1, this.idDespesa);
            stm.setInt(2, idPagamento);
            stm.executeUpdate();
            
        } catch(SQLException | ClassNotFoundException ex){
            ex.printStackTrace();
        } finally {
            Connect.close(conn);
        }
        return p;
    }

    private Pagamento get(int idPagamento) {
        Pagamento p = null;
        try{
            conn = Connect.connect();
            String sql = "SELECT *\n" +
                    "FROM pagamento\n" +
                    "WHERE DespesaIndividual_Despesa_idDespesa=? AND DespesaIndividual_Morador_Email=?";
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setInt(1, this.idDespesa);
            stm.setString(2, this.devedor);
            
            ResultSet rs = stm.executeQuery();
            if (rs.next()){
                p = new Pagamento (idPagamento, rs.getDouble("Valor"), rs.getString("Tipo"), rs.getString("Data"));
            }
        } catch(SQLException | ClassNotFoundException ex){
            ex.printStackTrace();
        } finally {
            Connect.close(conn);
        }
        return p;
    }
    
    public static int getID() {
        int id = 0;
        Connection conn = null;
        try{
            conn = Connect.connect();
            String sql = "SELECT idPagamento\n" +
                    "FROM pagamento\n" +
                    "ORDER BY idPagamento DESC\n" + 
                    "LIMIT 1";
            PreparedStatement stm = conn.prepareStatement(sql);
            
            ResultSet rs = stm.executeQuery();
            if (rs.next()){
                id = rs.getInt("idPagamento");
            }
            
        } catch(SQLException | ClassNotFoundException ex){
            ex.printStackTrace();
        } finally {
            Connect.close(conn);
        }
        return id;
    }

    public void clear() {
        try {
            conn = Connect.connect();
            String sql = "DELETE FROM pagamento\n" + 
                    "WHERE DespesaIndividual_Morador_Email=? AND DespesaIndividual_Despesa_idDespesa=?";
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setString(1, this.devedor);
            stm.setInt(2, this.idDespesa);
            stm.executeUpdate();
            
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace(); 
        } finally {
            Connect.close(conn);
        }
    }
}
