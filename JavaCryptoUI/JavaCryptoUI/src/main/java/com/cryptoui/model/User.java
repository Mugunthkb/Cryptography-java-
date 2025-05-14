package com.cryptoui.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

import java.util.HashMap;
import java.util.Map;

/**
 * Model class for a user with JavaFX properties for data binding
 */
public class User {
    private final StringProperty username;
    private final DoubleProperty balance;
    private final ObservableMap<String, Double> portfolio;
    
    private static final double DEFAULT_BALANCE = 10000.0;
    
    public User(String username) {
        this.username = new SimpleStringProperty(username);
        this.balance = new SimpleDoubleProperty(DEFAULT_BALANCE);
        this.portfolio = FXCollections.observableHashMap();
    }
    
    /**
     * Buy cryptocurrency
     * @param symbol Cryptocurrency symbol
     * @param amount Amount to buy
     * @param price Current price per unit
     * @return true if purchase was successful, false otherwise
     */
    public boolean buyCrypto(String symbol, double amount, double price) {
        double totalCost = amount * price;
        
        // Check if user has enough balance
        if (getBalance() < totalCost) {
            return false;
        }
        
        // Update balance
        setBalance(getBalance() - totalCost);
        
        // Update portfolio
        if (portfolio.containsKey(symbol)) {
            portfolio.put(symbol, portfolio.get(symbol) + amount);
        } else {
            portfolio.put(symbol, amount);
        }
        
        return true;
    }
    
    /**
     * Sell cryptocurrency
     * @param symbol Cryptocurrency symbol
     * @param amount Amount to sell
     * @param price Current price per unit
     * @return true if sale was successful, false otherwise
     */
    public boolean sellCrypto(String symbol, double amount, double price) {
        // Check if user has the cryptocurrency and enough amount
        if (!portfolio.containsKey(symbol) || portfolio.get(symbol) < amount) {
            return false;
        }
        
        // Update balance
        double saleValue = amount * price;
        setBalance(getBalance() + saleValue);
        
        // Update portfolio
        double newAmount = portfolio.get(symbol) - amount;
        if (newAmount <= 0) {
            portfolio.remove(symbol);
        } else {
            portfolio.put(symbol, newAmount);
        }
        
        return true;
    }
    
    /**
     * Get the amount of a specific cryptocurrency owned
     * @param symbol Cryptocurrency symbol
     * @return Amount owned or 0 if none
     */
    public double getCryptoAmount(String symbol) {
        return portfolio.getOrDefault(symbol, 0.0);
    }
    
    /**
     * Reset user's account (for simulation purposes)
     */
    public void resetAccount() {
        setBalance(DEFAULT_BALANCE);
        portfolio.clear();
    }
    
    // JavaFX property getters
    public StringProperty usernameProperty() {
        return username;
    }
    
    public DoubleProperty balanceProperty() {
        return balance;
    }
    
    public ObservableMap<String, Double> getPortfolio() {
        return portfolio;
    }
    
    // Standard getters and setters
    public String getUsername() {
        return username.get();
    }
    
    public void setUsername(String username) {
        this.username.set(username);
    }
    
    public double getBalance() {
        return balance.get();
    }
    
    public void setBalance(double balance) {
        this.balance.set(balance);
    }
    
    /**
     * Calculate total portfolio value
     * @param cryptoMap Map of cryptocurrency symbols to their data
     * @return Total value of the portfolio
     */
    public double getPortfolioValue(Map<String, Cryptocurrency> cryptoMap) {
        double totalValue = 0.0;
        
        for (Map.Entry<String, Double> entry : portfolio.entrySet()) {
            String symbol = entry.getKey();
            double amount = entry.getValue();
            
            if (cryptoMap.containsKey(symbol)) {
                double price = cryptoMap.get(symbol).getPrice();
                totalValue += amount * price;
            }
        }
        
        return totalValue;
    }
} 