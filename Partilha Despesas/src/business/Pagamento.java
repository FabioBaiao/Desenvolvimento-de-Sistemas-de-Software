/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package business;

/**
 *
 * @author Fábio Baião
 */
public class Pagamento {
    private double valor;
    private int id;
    private String tipo;
    private String data;

    public Pagamento(int id, double valor, String tipo, String data) {
        this.id = id;
        this.valor = valor;
        this.tipo = tipo;
        this.data = data;
    }

    public double getValor() {
        return valor;
    }

    public int getId() {
        return this.id;
    }

    public String getData() {
        return this.data;
    }

    public String getTipo() {
        return this.tipo;
    }
}
