/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import client.Client;
import java.io.IOException;

/**
 *
 * @author frost
 */
public class ServerMain {
    public static void main(String[] args) throws IOException{
        Server s = new Server();
        s.run();
    }
}
