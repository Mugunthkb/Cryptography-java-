package com.cryptoui.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Service class to manage cryptocurrency market data with JavaFX Observable collections
 */
public class MarketService {
    private final ObservableMap<String, Cryptocurrency> marketData;
    private Timer updateTimer;
    private static final int UPDATE_INTERVAL = 3000; // 3 seconds
    
    public MarketService() {
        marketData = FXCollections.observableHashMap();
        initializeMarketData();
    }
    
    /**
     * Initialize market data with some popular cryptocurrencies
     */
    private void initializeMarketData() {
        // Bitcoin
        marketData.put("BTC", new Cryptocurrency(
            "BTC",
            "Bitcoin",
            "The original cryptocurrency, created by Satoshi Nakamoto in 2009",
            42150.75,
            1.2,
            786000000000.0
        ));
        
        // Ethereum
        marketData.put("ETH", new Cryptocurrency(
            "ETH", 
            "Ethereum",
            "Smart contract platform enabling decentralized applications",
            2250.50,
            -0.8,
            268000000000.0
        ));
        
        // Cardano
        marketData.put("ADA", new Cryptocurrency(
            "ADA",
            "Cardano",
            "Proof-of-stake blockchain platform with a focus on sustainability",
            0.42,
            0.3,
            14800000000.0
        ));
        
        // Solana
        marketData.put("SOL", new Cryptocurrency(
            "SOL",
            "Solana",
            "High-performance blockchain supporting smart contracts and DeFi",
            110.85,
            2.1,
            48300000000.0
        ));
        
        // Dogecoin
        marketData.put("DOGE", new Cryptocurrency(
            "DOGE",
            "Dogecoin",
            "Originally created as a joke, now one of the most popular memecoins",
            0.089,
            -1.5,
            12500000000.0
        ));
    }
    
    /**
     * Start periodic price updates
     */
    public void startMarketUpdates() {
        if (updateTimer != null) {
            stopMarketUpdates();
        }
        
        updateTimer = new Timer(true);
        updateTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updatePrices();
            }
        }, UPDATE_INTERVAL, UPDATE_INTERVAL);
    }
    
    /**
     * Stop periodic price updates
     */
    public void stopMarketUpdates() {
        if (updateTimer != null) {
            updateTimer.cancel();
            updateTimer = null;
        }
    }
    
    /**
     * Update prices for all cryptocurrencies
     */
    private void updatePrices() {
        for (Cryptocurrency crypto : marketData.values()) {
            crypto.updatePrice();
        }
    }
    
    /**
     * Get the current market data
     * @return ObservableMap of crypto symbols to their data
     */
    public ObservableMap<String, Cryptocurrency> getMarketData() {
        return marketData;
    }
    
    /**
     * Get cryptocurrency by symbol
     * @param symbol Cryptocurrency symbol
     * @return Cryptocurrency object or null if not found
     */
    public Cryptocurrency getCryptocurrency(String symbol) {
        return marketData.get(symbol);
    }
} 