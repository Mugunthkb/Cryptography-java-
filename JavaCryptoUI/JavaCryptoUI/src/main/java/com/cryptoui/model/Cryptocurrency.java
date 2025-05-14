package com.cryptoui.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Random;

/**
 * Model class for a cryptocurrency with JavaFX properties for data binding
 */
public class Cryptocurrency {
    private final StringProperty symbol;
    private final StringProperty name;
    private final StringProperty description;
    private final DoubleProperty price;
    private final DoubleProperty change24h;
    private final DoubleProperty marketCap;
    
    private static final Random random = new Random();
    
    public Cryptocurrency(String symbol, String name, String description, 
                         double price, double change24h, double marketCap) {
        this.symbol = new SimpleStringProperty(symbol);
        this.name = new SimpleStringProperty(name);
        this.description = new SimpleStringProperty(description);
        this.price = new SimpleDoubleProperty(price);
        this.change24h = new SimpleDoubleProperty(change24h);
        this.marketCap = new SimpleDoubleProperty(marketCap);
    }
    
    /**
     * Updates the cryptocurrency's price with a random fluctuation
     */
    public void updatePrice() {
        // Generate a random price change between -3% and +3%
        double changePercent = (random.nextDouble() * 6 - 3) / 100;
        double newPrice = getPrice() * (1 + changePercent);
        
        // Round to 2 decimal places
        newPrice = Math.round(newPrice * 100.0) / 100.0;
        
        // Update price
        setPrice(newPrice);
        
        // Update 24h change value slightly
        double newChange = getChange24h() + (random.nextDouble() * 0.4 - 0.2);
        setChange24h(Math.round(newChange * 100.0) / 100.0);
    }
    
    // JavaFX property getters
    public StringProperty symbolProperty() {
        return symbol;
    }
    
    public StringProperty nameProperty() {
        return name;
    }
    
    public StringProperty descriptionProperty() {
        return description;
    }
    
    public DoubleProperty priceProperty() {
        return price;
    }
    
    public DoubleProperty change24hProperty() {
        return change24h;
    }
    
    public DoubleProperty marketCapProperty() {
        return marketCap;
    }
    
    // Standard getters and setters
    public String getSymbol() {
        return symbol.get();
    }
    
    public void setSymbol(String symbol) {
        this.symbol.set(symbol);
    }
    
    public String getName() {
        return name.get();
    }
    
    public void setName(String name) {
        this.name.set(name);
    }
    
    public String getDescription() {
        return description.get();
    }
    
    public void setDescription(String description) {
        this.description.set(description);
    }
    
    public double getPrice() {
        return price.get();
    }
    
    public void setPrice(double price) {
        this.price.set(price);
    }
    
    public double getChange24h() {
        return change24h.get();
    }
    
    public void setChange24h(double change24h) {
        this.change24h.set(change24h);
    }
    
    public double getMarketCap() {
        return marketCap.get();
    }
    
    public void setMarketCap(double marketCap) {
        this.marketCap.set(marketCap);
    }
} 