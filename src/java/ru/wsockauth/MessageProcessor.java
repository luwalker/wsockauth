/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.wsockauth;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.EncodeException;
import javax.websocket.Session;

/**
 *
 * @author alex
 */
public class MessageProcessor {
    
    private static final int ROLE_ADMIN = 1;
    private static final int ROLE_USER = 2;
    private static final String CUSTOMER_NOT_FOUND = "Customer not found";
    private static final String CODE_CUSTOMER_NOT_FOUND = "customer.notFound";
    
    
    private DbConnection dbConnection = null;

    public MessageProcessor() {
        dbConnection = new DbConnection();
    }

    public boolean process(Session session, Messaage message, UserPassport passport) throws IOException, EncodeException {
        
        switch (message.getType()) {
            case "USER_MESSAGE":
                sendText(session, "Server: process text message: \"" + message.getData("text") + "\"" );
                break;
                
            case "REGISTRATION_CUSTOMER":
                if (passport.getRole() == ROLE_ADMIN) {
                    String sql = "SELECT * FROM users WHERE login='" + message.getData("email") + "'";
                    try {
                        ResultSet sqlRs = dbConnection.executeQuery(sql);
                        if ( sqlRs.next() ) {
                            sendText(session, "Server: User \"" + message.getData("email") + "\" exist already" );
                            return true;
                        }
                        sqlRs.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(MessageProcessor.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    customerRegistration(message.getData("email"), message.getData("password"), ROLE_USER);
                    sendText(session, "Server: New user registered: \"" + message.getData("email") + "\"" );
                }
                else {
                    sendText(session, "Server: You are not Admin, registration denied for \"" + message.getData("email") + "\"" );
                }
                break;
                        
            case "LOGIN_CUSTOMER":
                String authemail = message.getData("email");
                String authpassw = message.getData("password");
                
                //make authentication
                try { 
                    // count records in USERS
                    String sql = "SELECT COUNT(*) AS count FROM users";
                    ResultSet sqlRs1 = dbConnection.executeQuery(sql); 
                    
                    if (sqlRs1.next()) {
                        int count = sqlRs1.getInt("count");
                        if (count == 0) {
                            // set first user as Admin
                            passport.setRole(ROLE_ADMIN);
                            customerRegistration(authemail, authpassw, ROLE_ADMIN);
                            System.out.println("User \"" + authemail + "\" set as Administrator");
                            sendText(session, "Server: First user \"" + message.getData("email") + "\" registered as Admin" );
                        }
                        else {
                            // login processing

                            Messaage authMessage = new Messaage();
                                    
                            // get user's password and check received one
                            sql = "SELECT * FROM users WHERE login='" + authemail + "'";
                            ResultSet sqlRs2 = dbConnection.executeQuery(sql); 
                            if ( sqlRs2.next() && sqlRs2.getString("password").equals(authpassw) ) {
                                
                                sendText(session, "Server: User login: \"" + message.getData("email") + "\"" );

                                // generate new Token
                                int userId = sqlRs2.getInt("id");
                                
                                passport.setUserId(userId);
                                passport.setRole(sqlRs2.getInt("role"));
                                String ApiToken = UUID.randomUUID().toString();
                                passport.setApiToken(ApiToken);
                                
                                Date date = new Date();
                                Timestamp currentDate = new Timestamp(date.getTime());
                                Timestamp tokenExpirDate = new Timestamp(date.getTime() + 60 * 60000) ;
                                passport.setApiTokenExpirationDate(tokenExpirDate);

                                // get previous Token and save to database (TokenHistory table)
                                saveTokenHistory(userId);
                                
                                // save Token to database (Tokens table)
                                saveNewTocken(userId, currentDate, ApiToken, tokenExpirDate);

                                // Send CUSTOMER_API_TOKEN message
                                authMessage.setType("CUSTOMER_API_TOKEN");
                                authMessage.setSequenceId(message.getSequenceId());
                                authMessage.addData("api_token", ApiToken);
                                SimpleDateFormat dformat = new SimpleDateFormat("yyyyy-mm-dd'T'hh:mm:ss'Z'");
                                String ApiTokenExpirationDate = dformat.format(date);        
                                authMessage.addData("api_token_expiration_date", ApiTokenExpirationDate);
                                send(session, authMessage);
                            }
                            else {
                                // Auth ERROR
                                //sendText(session, "Server: User \"" + message.getData("email") + "\" denied" );
                                authMessage.setType("CUSTOMER_ERROR");
                                authMessage.setSequenceId(message.getSequenceId());
                                authMessage.addData("error_description", CUSTOMER_NOT_FOUND);
                                authMessage.addData("error_code", CODE_CUSTOMER_NOT_FOUND);
                                send(session, authMessage);

                                return false;
                            }
                            sqlRs2.close();
                        }
                    }
                    sqlRs1.close();
                    
                } catch (SQLException ex) {
                    System.out.println("Connect failed !");
                }

                break;
            default:
                this.send(session, message);
        }
        return true;
    }
    
    private void sendText(Session session, String txtStr) throws IOException, EncodeException  {
        Messaage txtMessage = new Messaage("USER_MESSAGE", UUID.randomUUID().toString(), txtStr);
        send(session, txtMessage);
    }
    
    private void send(Session session, Messaage message) throws IOException, EncodeException  {
        session.getBasicRemote().sendObject(message);
    }

    private void customerRegistration(String email, String passw, int role) {
        try {
            customerRegistration(email, passw, role, null);
        } catch (SQLException ex) {
            Logger.getLogger(MessageProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void customerRegistration(String email, String passw, int role, Statement countStatement) throws SQLException {
        String sql = "INSERT INTO WAUSERBOT.USERS (login, password, role) VALUES ('"+email+"', '"+passw+"', "+ String.valueOf(role) +")";
        dbConnection.executeUpdate(sql);
    }
    
    private void saveNewTocken(int userId, Timestamp currentDate, String token, Timestamp tockenExpireDate) {
        String sql = "INSERT INTO WAUSERBOT.TOKENS (userid, date, token, tokenexpire) VALUES ("+String.valueOf(userId)+", '"+currentDate.toString()+"', '"+ token +"', '"+tockenExpireDate.toString()+"')";
        dbConnection.executeUpdate(sql);
    }

    private void saveTokenHistory(int userId) {
        String sql = "SELECT * FROM WAUSERBOT.TOKENS WHERE userid=" + userId;
        try {
            ResultSet sqlRs = dbConnection.executeQuery(sql);
            if ( sqlRs.next() ) {
                Date date = new Date();
                Timestamp curretTimestamp = new Timestamp(date.getTime());
                sql = "INSERT INTO WAUSERBOT.TOKENHISTORY (userid, date, token, tokenexpire, dateresign) VALUES ("+sqlRs.getString("userid")+", '"+sqlRs.getString("date")+"', '"+ sqlRs.getString("token") +"', '"+sqlRs.getString("tokenexpire")+"', '"+curretTimestamp.toString()+"')";
                dbConnection.executeUpdate(sql);

                sql = "DELETE FROM WAUSERBOT.TOKENS WHERE userid=" + userId;
                dbConnection.executeUpdate(sql);
            }
            sqlRs.close();
        } catch (SQLException ex) {
            Logger.getLogger(MessageProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
