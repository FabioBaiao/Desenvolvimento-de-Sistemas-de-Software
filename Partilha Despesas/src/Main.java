
import business.Facade;
import presentation.LoginWindow;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Fábio Baião
 */
public class Main {
    public static void main(String[] args){
        Facade f = new Facade();
        
        LoginWindow lw = new LoginWindow(f);
        
        lw.setVisible(true);
    }
}
