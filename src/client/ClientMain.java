/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.IOException;

/**
 *
 * @author frost
 */
public class ClientMain {

    //Run a new, independent client
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Client c = new Client();
        c.run();
    }
}
