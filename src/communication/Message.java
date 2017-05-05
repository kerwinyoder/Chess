/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package communication;

import java.io.Serializable;

/**
 *
 * @author djfearon0
 */
public class Message implements Serializable {

    private boolean requestAccepted;
    private boolean requestSeen;
    private Object body;
    private String color;
    private String header;
    private String requestedIP;
    private String sendingIP;

    public Message(String h, Object b) {
        header = h;
        body = b;
        requestAccepted = false;
        requestSeen = false;
        requestedIP = null;
        sendingIP = null;
        color = "";
    }

    public void setHeader(String h) {
        header = h;
    }

    public void setBody(Object b) {
        body = b;
    }

    public String getHeader() {
        return header;
    }

    public Object getBody() {
        return body;
    }

    public void acceptRequest() {
        requestAccepted = true;
    }

    public boolean requestAccepted() {
        return requestAccepted;
    }

    public void setRequestedIP(String ip) {
        requestedIP = ip;
    }

    public String getRequestedIP() {
        return requestedIP;
    }

    public void setSendingIP(String ip) {
        sendingIP = ip;
    }

    public String getSendingIP() {
        return sendingIP;
    }

    public void requestSeen() {
        requestSeen = true;
    }

    public boolean getRequestSeen() {
        return requestSeen;
    }
    
    public void setColor(String c){
        color = c;
    }
    
    public String getColor(){
        return color;
    }

}
