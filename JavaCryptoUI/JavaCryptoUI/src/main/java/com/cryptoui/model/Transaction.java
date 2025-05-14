package com.cryptoui.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Model class for a transaction with JavaFX properties for data binding
 */
public class Transaction {
    private final StringProperty username;
    private final StringProperty cryptoSymbol;
    private final DoubleProperty amount;
    private final DoubleProperty price;
    private final ObjectProperty<LocalDateTime> timestamp;
    private final ObjectProperty<TransactionType> type;
    
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    public enum TransactionType {
        BUY, SELL
    }
    
    public Transaction(String username, String cryptoSymbol, double amount, double price, TransactionType type) {
        this.username = new SimpleStringProperty(username);
        this.cryptoSymbol = new SimpleStringProperty(cryptoSymbol);
        this.amount = new SimpleDoubleProperty(amount);
        this.price = new SimpleDoubleProperty(price);
        this.timestamp = new SimpleObjectProperty<>(LocalDateTime.now());
        this.type = new SimpleObjectProperty<>(type);
    }
    
    /**
     * Calculate the total value of this transaction
     * @return Total value of the transaction
     */
    public double getTotalValue() {
        return getAmount() * getPrice();
    }
    
    // JavaFX property getters
    public StringProperty usernameProperty() {
        return username;
    }
    
    public StringProperty cryptoSymbolProperty() {
        return cryptoSymbol;
    }
    
    public DoubleProperty amountProperty() {
        return amount;
    }
    
    public DoubleProperty priceProperty() {
        return price;
    }
    
    public ObjectProperty<LocalDateTime> timestampProperty() {
        return timestamp;
    }
    
    public ObjectProperty<TransactionType> typeProperty() {
        return type;
    }
    
    // Standard getters
    public String getUsername() {
        return username.get();
    }
    
    public String getCryptoSymbol() {
        return cryptoSymbol.get();
    }
    
    public double getAmount() {
        return amount.get();
    }
    
    public double getPrice() {
        return price.get();
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp.get();
    }
    
    public TransactionType getType() {
        return type.get();
    }
    
    /**
     * Format the transaction details as a string
     * @return Formatted transaction string
     */
    @Override
    public String toString() {
        String typeStr = getType() == TransactionType.BUY ? "Bought" : "Sold";
        return String.format("[%s] %s %.6f %s at $%.2f (Total: $%.2f)", 
                             getTimestamp().format(formatter),
                             typeStr,
                             getAmount(),
                             getCryptoSymbol(),
                             getPrice(),
                             getTotalValue());
    }
    
    /**
     * Get formatted timestamp
     * @return Formatted date/time string
     */
    public String getFormattedTimestamp() {
        return getTimestamp().format(formatter);
    }
    
    /**
     * Get transaction type as string
     * @return "Buy" or "Sell"
     */
    public String getTypeAsString() {
        return getType() == TransactionType.BUY ? "Buy" : "Sell";
    }
} 