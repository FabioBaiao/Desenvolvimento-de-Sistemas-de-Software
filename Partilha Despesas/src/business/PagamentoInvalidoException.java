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
public class PagamentoInvalidoException extends Exception {

    
    public PagamentoInvalidoException(String msg) {
        super(msg);
    }
}
