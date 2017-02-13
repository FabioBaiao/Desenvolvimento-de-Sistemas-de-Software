/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package business;

import data.DespesaDAO;
import data.MoradorDAO;
import data.PagamentoDAO;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Fábio Baião
 */
public class Facade {
    private MoradorDAO moradores;
    //private Map<String, Morador> moradores; // String é o email
    private Morador autenticado;
    
    private int idDespesa;
    private int idPagamento;
    
    public Facade (){
        moradores = new MoradorDAO();
        //moradores = new HashMap<>();
        autenticado = null;
        
        this.idDespesa = DespesaDAO.getID(); //get de sql
        this.idPagamento = PagamentoDAO.getID(); //get de sql
    }
    
    public void registarMorador(String nome, String email, String password) throws EmailInvalidoException{
        if (moradores.containsKey(email)){
            throw new EmailInvalidoException ("Email ja existe!");
        }
        Morador morador = new Morador (nome, email, password);
        moradores.put(email, morador);
    }
    
    public void autenticarMorador(String email, String password) throws EmailInvalidoException, PasswordErradaException{
        this.autenticado = moradores.get(email);
        if (autenticado == null){
            throw new EmailInvalidoException ("Email nao existe!");
        }
        String pw = autenticado.getPassword();
        if (!pw.equals(password)){
            this.autenticado = null;
            throw new PasswordErradaException ("Password errada!");
        }
    }
    
    public void adicionarDespesa(String descricao, double valorTotal, List<String> emails, String tipo, String dataLimite) 
            throws EmailInvalidoException, ValorInvalidoException{
        if (valorTotal <= 0){
            throw new ValorInvalidoException ("Valor invalido!");
        }
        Set<Morador> set = new HashSet<>();
        for (String s : emails){
            if (s.equals(this.autenticado.getEmail())){
                throw new EmailInvalidoException ("Nao pode introduzir o proprio email!");
            }
            Morador m = moradores.get(s);
            if (m == null){
                throw new EmailInvalidoException ("Email: " + s + ", nao existe!");
            }
            set.add(m);
        }
        this.idDespesa++;
        this.autenticado.adicionarDespesa(this.idDespesa, descricao, valorTotal, set, tipo, dataLimite);
    }
    
    public void adicionarDespesa(String descricao, double valorTotal, List<String> emails, List<Double> percentagens, String tipo, String dataLimite) 
            throws ValorInvalidoException, PercentagemInvalidaException, EmailInvalidoException, DespesaInvalidaException{
        if (valorTotal <= 0){
            throw new ValorInvalidoException ("Valor invalido!");
        }
        double sum = 0;
        double n = 0;
        for (Double f : percentagens){
            sum += f;
            n++;
            if (f < 0 || f > 100 || sum > 100){
                throw new PercentagemInvalidaException ("Percentagens invalidas!");
            }
        }
        Set<Morador> set = new HashSet<>();
        for (String s : emails){
            n--;
            if (s.equals(this.autenticado.getEmail())){
                throw new EmailInvalidoException ("Nao pode introduzir o proprio email!");
            }
            Morador m = moradores.get(s);
            if (m == null){
                throw new EmailInvalidoException ("Email: " + s + ", nao existe!");
            }
            set.add(m);
        }
        if (n != 0){
            throw new DespesaInvalidaException ("Numero de emails diferente do numero de percentagens!");
        }
        this.idDespesa++;
        this.autenticado.adicionarDespesa(this.idDespesa, descricao, valorTotal, set, percentagens, tipo, dataLimite);
    }
    
    public void editarData(int id, String dataLimite) throws DespesaInvalidaException{
        this.autenticado.editarData(id, dataLimite);
    }
    
    public void editarDescricao(int id, String descricao) throws DespesaInvalidaException{
        this.autenticado.editarDescricao(id, descricao);
    }
    
    public void editarValor(int id, double valor) throws ValorInvalidoException, DespesaInvalidaException{
        if (valor <= 0){
            throw new ValorInvalidoException("Valor invalido!");
        }
        this.autenticado.editarValor(id, valor);
    }
    
    public void eliminarDespesa (int id) throws DespesaInvalidaException{
        Set<String> col = this.autenticado.eliminarDespesasIndividuais(id);
        for (String s : col){
            Morador m = moradores.get(s);
            m.eliminarDespesaIndividual(id);
        }
        this.autenticado.eliminarDespesa(id);
    }
    
    public void efetuarPagamento (int idDespesa, double valor, String tipo, String data) throws ValorInvalidoException, DespesaInvalidaException{
        if (valor <= 0){
            throw new ValorInvalidoException ("Valor invalido!");
        }
        this.autenticado.efetuarPagamento(idDespesa, this.idPagamento+1, valor, tipo, data);
        this.idPagamento++;
    }
    
    public void eliminarPagamento (int idDespesa, int idPagamento) throws DespesaInvalidaException, PagamentoInvalidoException{
        this.autenticado.eliminarPagamento(idDespesa, idPagamento);
    }
    
    public List<String> consultarDespesas(){
        return this.autenticado.consultarDespesas();
    }
    
    public List<String> consultarDespesasIndividuais(){
        return this.autenticado.consultarDespesasIndividuais();
    }
    
    public List<String> consultarPagamentos(){
        return this.autenticado.consultarPagamentos();
    }
}
