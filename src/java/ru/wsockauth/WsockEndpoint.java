/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.wsockauth;

import java.io.*; 
import java.util.*; 
import javax.websocket.server.ServerEndpoint;
import javax.websocket.Session;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.EncodeException;

/**
 *
 * @author alex
 */
@ServerEndpoint(
    value = "/wsockauth", 
    encoders = {WsockEncoder.class}, 
    decoders = {WsockDecoder.class}
)
public class WsockEndpoint {
    
    private static final Set<Session> sessions = Collections.synchronizedSet(new HashSet<Session>());
    private static final Map<String, UserPassport> mapSessiion = new HashMap<String, UserPassport>();
    private MessageProcessor messageProcessor = null;
    
    @OnMessage
    public void onMessage(Session session, Messaage message) throws IOException, EncodeException {    
        if (!messageProcessor.process(session, message, mapSessiion.get(session.getId()))) {
            session.close();
        }
    }
    
    @OnOpen
    public void onOpen(Session session) throws IOException, EncodeException {
        sessions.add(session);
        mapSessiion.put(session.getId(), new UserPassport());
        messageProcessor = new MessageProcessor();
        Messaage txtMessage = new Messaage("USER_MESSAGE", "xxx-xxx-xxx-xxx", "Server: Handshake");
        session.getBasicRemote().sendObject(txtMessage);
    }

    @OnClose
    public void onClose(Session session) throws IOException, EncodeException {
        Messaage txtMessage = new Messaage("TTT", "xxx-xxx-xxx-xxx", "Server: Stop Session, OK");
        session.getBasicRemote().sendObject(txtMessage);
        mapSessiion.remove(session.getId());
        sessions.remove(session);
    }

    @OnError
    public void onError(Throwable error) {
    }

}
