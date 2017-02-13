/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package business;

import data.DespesaDAO;
import data.DespesaIndividualMDAO;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Fábio Baião
 */
public class Morador {
    private String nome;
    private String email;
    private String password;
    
    private DespesaDAO despesas;
    private DespesaIndividualMDAO despesasIndividuais;
    //private Map<Integer, Despesa> despesas;
    //private Map<Integer, DespesaIndividual> despesasIndividuais;
    
    
    public Morador (String nome, String email, String password){
        this.nome = nome;
        this.email = email;
        this.password = password;
        this.despesas = new DespesaDAO (email);
        this.despesasIndividuais = new DespesaIndividualMDAO(email);
        //this.despesas = new HashMap<>();
        //this.despesasIndividuais = new HashMap<>();
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }
    
    public void adicionarDespesaIndividual (DespesaIndividual despesaIndividual){
        this.despesasIndividuais.put(despesaIndividual.getIdDespesa(), despesaIndividual);
    }
    
    public void adicionarDespesa (int idDespesa, String descricao, double valorTotal, Set<Morador> moradores, String tipo, String dataLimite){
        Despesa despesa = new Despesa (descricao, valorTotal, idDespesa, tipo, dataLimite);
        this.despesas.put(idDespesa, despesa);
        despesa.adicionarDespesasIndividuais(this.email, moradores);
        this.despesas.put(idDespesa, despesa);
    }
    
    public void adicionarDespesa (int idDespesa, String descricao, double valorTotal, Set<Morador> moradores, List<Double> percentagens, String tipo, String dataLimite){
        Despesa despesa = new Despesa (descricao, valorTotal, idDespesa, tipo, dataLimite);
        this.despesas.put(idDespesa, despesa);
        despesa.adicionarDespesasIndividuais(this.email, moradores, percentagens);
        this.despesas.put(idDespesa, despesa);
    }
    
    public void editarDescricao (int id, String descricao) throws DespesaInvalidaException{
        Despesa d = despesas.get(id);
        if (d == null){
            throw new DespesaInvalidaException ("Despesa invalida!");
        }
        d.editarDescricao(descricao);
        this.despesas.put(id, d);
    }

    public void editarValor(int id, double valor) throws ValorInvalidoException, DespesaInvalidaException{
        Despesa d = despesas.get(id);
        if (d == null){
            throw new DespesaInvalidaException ("Despesa invalida!");
        }
        d.editarValor(valor);
        this.despesas.put(id, d);
    }

    public Set<String> eliminarDespesasIndividuais(int id) throws DespesaInvalidaException{
        Despesa d = despesas.get(id);
        if (d == null){
            throw new DespesaInvalidaException ("Despesa invalida!");
        }
        Set<String> set =  d.eliminar();
        return set;
    }
    
    public void eliminarDespesa(int id){
        despesas.remove(id);
    }

    public void eliminarDespesaIndividual(int idDespesa) {
        DespesaIndividual di = despesasIndividuais.get(idDespesa);
        di.eliminarPagamentos();
        despesasIndividuais.remove(idDespesa);
    }

    public void efetuarPagamento(int idDespesa, int idPagamento, double valor, String tipo, String data) throws DespesaInvalidaException, ValorInvalidoException{
        DespesaIndividual di = despesasIndividuais.get(idDespesa);
        if (di == null){
            throw new DespesaInvalidaException ("Despesa invalida!");
        }
        di.adicionarPagamento(valor, idPagamento, this.email, tipo, data);
    }

    public void eliminarPagamento(int idDespesa, int idPagamento) throws DespesaInvalidaException, PagamentoInvalidoException{
        DespesaIndividual di = despesasIndividuais.get(idDespesa);
        if (di == null){
            throw new DespesaInvalidaException ("So pode eliminar despesas que efetuou!");
        }
        di.eliminarPagamento(this.email, idPagamento);
    }

    public List<String> consultarDespesas() {
        List<String> list = new ArrayList<>();
        for (Despesa d : despesas.values()){
            StringBuilder sb = new StringBuilder();
            String s;
            s = Integer.toString(d.getId());
            sb.append(s);
            sb.append(" - ");
            sb.append(d.getDescricao());
            sb.append(" - ");
            sb.append(d.getDataLimite());
            sb.append(" - ");
            sb.append(d.getTipo());
            sb.append(" - ");
            s = Double.toString(d.getValorAPagar());
            sb.append(s);
            sb.append(" - ");
            s = Double.toString(d.getValorPago());
            sb.append(s);
            list.add(sb.toString());
        }
        return list;
    }

    public List<String> consultarDespesasIndividuais() {
        List<String> list = new ArrayList<>();
        for (DespesaIndividual di : despesasIndividuais.values()){
            if (di.getValor() == di.getValorPago()){
                continue;
            }
            StringBuilder sb = new StringBuilder();
            String s;
            s = Integer.toString(di.getIdDespesa());
            sb.append(s);
            sb.append(" - ");
            sb.append(di.getDescricao());
            sb.append(" - ");
            sb.append(di.getEmailCriador());
            sb.append(" - ");
            sb.append(di.getDataLimite());
            sb.append(" - ");
            sb.append(di.getTipo());
            sb.append(" - ");
            s = Double.toString(di.getValor());
            sb.append(s);
            sb.append(" - ");
            s = Double.toString(di.getValorPago());
            sb.append(s);
            list.add(sb.toString());
        }
        return list;
    }

    public List<String> consultarPagamentos() {
        List<String> list = new ArrayList<>();
        for (Despesa d : despesas.values()){
            List<String> l = d.consultarPagamentos();
            list.addAll(l);
        }
        for (DespesaIndividual di : despesasIndividuais.values()){
            List<String> l = di.consultarPagamentos();
            for (String s : l){
                StringBuilder sb = new StringBuilder();
                sb.append(s);
                sb.append(" - Efetuado");
                list.add(sb.toString());
            }
        }
        return list;
    }

    public String getNome() {
        return this.nome;
    }

    void editarData(int id, String dataLimite) throws DespesaInvalidaException{
        Despesa d = despesas.get(id);
        if (d == null){
            throw new DespesaInvalidaException ("Despesa invalida!");
        }
        d.editarData(dataLimite);
        this.despesas.put(id, d);
    }
}
