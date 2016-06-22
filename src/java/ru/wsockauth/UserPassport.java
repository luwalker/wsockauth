/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.wsockauth;

import java.sql.Timestamp;

/**
 *
 * @author alex
 */
public class UserPassport {
    
    private int userId;
    private int role;
    private String apiToken;
    private Timestamp apiTokenExpirationDate;

    public UserPassport() {
    }
    
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }

    public Timestamp getApiTokenExpirationDate() {
        return apiTokenExpirationDate;
    }

    public void setApiTokenExpirationDate(Timestamp apiTokenExpirationDate) {
        this.apiTokenExpirationDate = apiTokenExpirationDate;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }
    
}
