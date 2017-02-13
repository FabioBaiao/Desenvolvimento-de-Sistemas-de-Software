/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package business;

import data.PagamentoDAO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Fábio Baião
 */
public class DespesaIndividual {
    private String emailCriador; //
    private String descricao;
    private double valor;
    private int idDespesa;
    private String tipo;
    private String dataLimite;
    
    private PagamentoDAO pagamentos;
    //private Map<Integer, Pagamento> pagamentos;
    
    public DespesaIndividual(String emailCriador, String descricao, double valor, int idDespesa, String devedor, String tipo, String dataLimite) {
        this.emailCriador = emailCriador;
        this.descricao = descricao;
        this.valor = valor;
        this.idDespesa = idDespesa;
        this.tipo = tipo;
        this.dataLimite = dataLimite;
        
        this.pagamentos = new PagamentoDAO(idDespesa, devedor);
        //this.pagamentos = new HashMap<>();
    }

    public void editarValor(double razao) {
        this.valor *= razao;
    }
    
    public void editarDescricao(String descricao){
        this.descricao = descricao;
    }

    public int getIdDespesa() {
        return this.idDespesa;
    }
    
    public double getValorPago(){
        double sum = 0;
        for (Pagamento p : pagamentos.values()){
            sum += p.getValor();
        }
        return sum;
    }

    public void adicionarPagamento(double valor, int idPagamento, String email, String tipo, String data) throws ValorInvalidoException{
        if (valor + getValorPago() > this.valor){
            throw new ValorInvalidoException ("Valor invalido!");
        }
        Pagamento pagamento = new Pagamento(idPagamento, valor, tipo, data);
        pagamentos.put(idPagamento, pagamento);
    }

    public void eliminarPagamento(String email, int idPagamento) throws PagamentoInvalidoException{
        Pagamento pagamento = pagamentos.remove(idPagamento);
        if (pagamento == null){
            throw new PagamentoInvalidoException ("Pagamento invalido!");
        }
    }

    public double getValor() {
        return this.valor;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public String getEmailCriador() {
        return this.emailCriador;
    }

    public List<String> consultarPagamentos() {
        List<String> list = new ArrayList<>();
        for (Pagamento p : pagamentos.values()){
            StringBuilder sb = new StringBuilder();
            String s;
            s = Integer.toString(idDespesa);
            sb.append(s);
            sb.append(" - ");
            s = Integer.toString(p.getId());
            sb.append(s);
            sb.append(" - ");
            sb.append(descricao);
            sb.append(" - ");
            sb.append(p.getData());
            sb.append(" - ");
            sb.append(p.getTipo());
            sb.append(" - ");
            s = Double.toString(p.getValor());
            sb.append(s);
            list.add(sb.toString());
        }
        return list;
    }

    public void editarData(String dataLimite) {
        this.dataLimite = dataLimite;
    }

    public String getDataLimite() {
        return this.dataLimite;
    }

    public String getTipo() {
        return this.tipo;
    }

    public void eliminarPagamentos() {
        this.pagamentos.clear();
    }
}
