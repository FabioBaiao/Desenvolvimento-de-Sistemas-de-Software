/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package business;

import data.DespesaIndividualDDAO;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Fábio Baião
 */
public class Despesa {
    private String descricao;
    private double valorTotal;
    private int id;
    private double valorAPagar;
    private String tipo;
    private String dataLimite;
    
    //private Map<String, DespesaIndividual> despesasIndividuais;
    private DespesaIndividualDDAO despesasIndividuais;
    
    
    public Despesa (String descricao, double valorTotal, int idDespesa, String tipo, String dataLimite){
        this.id = idDespesa;
        this.descricao = descricao;
        this.valorTotal = valorTotal;
        this.valorAPagar = 0;
        this.tipo = tipo;
        this.dataLimite = dataLimite;
        
        this.despesasIndividuais = new DespesaIndividualDDAO (this.id);
        //this.despesasIndividuais = new HashMap();
    }
    
    public Despesa(int id, String descricao, double valorTotal, double valorAPagar, String dataLimite, String tipo) {
        this.id = id;
        this.descricao = descricao;
        this.valorTotal = valorTotal;
        this.valorAPagar = valorAPagar;
        this.dataLimite = dataLimite;
        this.tipo = tipo;
        this.despesasIndividuais = new DespesaIndividualDDAO(this.id);
    }
    
    public void adicionarDespesasIndividuais(String emailCriador, Set<Morador> moradores){
        int n = moradores.size() + 1;
        double valor = valorTotal/n;
        for (Morador m : moradores){
            this.valorAPagar += valor;
            DespesaIndividual d = new DespesaIndividual(emailCriador, this.descricao, valor, this.id, m.getEmail(), tipo, dataLimite);
            m.adicionarDespesaIndividual(d);
            despesasIndividuais.put(m.getEmail(), d);
        }
    }
    
    public void adicionarDespesasIndividuais(String emailCriador, Set<Morador> moradores, List<Double> percentagens){
        Iterator<Double> it = percentagens.iterator();
        for (Morador m : moradores){
            double valor = valorTotal * it.next() * 0.01;
            this.valorAPagar += valor;
            DespesaIndividual d = new DespesaIndividual (emailCriador, this.descricao, valor, this.id, m.getEmail(), tipo, dataLimite);
            m.adicionarDespesaIndividual(d);
            this.despesasIndividuais.put(m.getEmail(), d);
        }
    }
    
    public int getId(){
        return this.id;
    }
    
    public void editarDescricao(String descricao){
        for (DespesaIndividual d : despesasIndividuais.values()){
            d.editarDescricao(descricao);
            this.despesasIndividuais.put(d.getEmailCriador(), d);
        }
        this.descricao = descricao;
    }
    
    public double getValorPago(){
        double sum = 0;
        for (DespesaIndividual d : despesasIndividuais.values()){
            sum += d.getValorPago();
        }
        return sum;
    }

    public void editarValor(double valor) throws DespesaInvalidaException{
        if (getValorPago() != 0){
            throw new DespesaInvalidaException ("Valor da despesa nao pode ser alterado!");
        }
        double razao = valor/this.valorTotal;
        for (DespesaIndividual d : despesasIndividuais.values()){
            d.editarValor(razao);
            this.despesasIndividuais.put(d.getEmailCriador(), d);
        }
        this.valorTotal = valor;
        this.valorAPagar *= valor/this.valorTotal;
    }

    public Set<String> eliminar() throws DespesaInvalidaException{
        double valorPago = getValorPago();
        if (valorPago > 0 && valorPago < this.valorAPagar){
            throw new DespesaInvalidaException ("Nao pode eliminar esta despesa!");
        }
        return despesasIndividuais.keySet();
    }

    public double getValorAPagar() {
        return this.valorAPagar;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public List<String> consultarPagamentos() {
        List<String> list = new ArrayList<>();
        for (DespesaIndividual di : despesasIndividuais.values()){
            List<String> l = di.consultarPagamentos();
            for (String s : l){
                StringBuilder sb = new StringBuilder();
                sb.append(s);
                sb.append(" - Recebido");
                list.add(sb.toString());
            }
        }
        return list;
    }

    public double getValorTotal() {
        return this.valorTotal;
    }

    public void editarData(String dataLimite) {
        for (DespesaIndividual d : despesasIndividuais.values()){
            d.editarData(dataLimite);
            this.despesasIndividuais.put(d.getEmailCriador(), d);
        }
        this.dataLimite = dataLimite;
    }

    public String getDataLimite() {
        return this.dataLimite;
    }

    public String getTipo() {
        return this.tipo;
    }
}
