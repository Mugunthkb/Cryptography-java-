package com.cryptoui.controller;

import com.cryptoui.model.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/**
 * Main controller for the Crypto UI application
 */
public class CryptoController {
    
    // Model classes
    private MarketService marketService;
    private User currentUser;
    private ObservableList<Transaction> transactionHistory;
    
    // FXML UI controls will be injected by JavaFX
    @FXML private Label usernameLabel;
    @FXML private Label balanceLabel;
    @FXML private Label portfolioValueLabel;
    @FXML private Label totalWorthLabel;
    
    @FXML private TableView<Cryptocurrency> marketTableView;
    @FXML private TableColumn<Cryptocurrency, String> symbolColumn;
    @FXML private TableColumn<Cryptocurrency, String> nameColumn;
    @FXML private TableColumn<Cryptocurrency, Double> priceColumn;
    @FXML private TableColumn<Cryptocurrency, Double> changeColumn;
    
    @FXML private TableView<Transaction> transactionTableView;
    @FXML private TableColumn<Transaction, String> transactionTimeColumn;
    @FXML private TableColumn<Transaction, String> transactionTypeColumn;
    @FXML private TableColumn<Transaction, String> transactionSymbolColumn;
    @FXML private TableColumn<Transaction, Double> transactionAmountColumn;
    @FXML private TableColumn<Transaction, Double> transactionPriceColumn;
    @FXML private TableColumn<Transaction, Double> transactionTotalColumn;
    
    @FXML private ComboBox<String> cryptoComboBox;
    @FXML private TextField amountTextField;
    @FXML private Label currentPriceLabel;
    @FXML private Label totalCostLabel;
    @FXML private Label cryptoDescriptionLabel;
    @FXML private Label cryptoHoldingsLabel;
    
    @FXML private Button buyButton;
    @FXML private Button sellButton;
    @FXML private Button resetAccountButton;
    
    /**
     * Initialize the controller
     */
    @FXML
    public void initialize() {
        // Initialize model
        marketService = new MarketService();
        currentUser = new User("Demo User");
        transactionHistory = FXCollections.observableArrayList();
        
        // Set up UI components
        setupMarketTable();
        setupTransactionTable();
        setupCryptoSelection();
        setupUserInfo();
        setupButtonHandlers();
        
        // Start market updates
        marketService.startMarketUpdates();
        
        // Set up a periodic UI update
        Thread updateThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000); // Update UI every second
                    Platform.runLater(this::updateUI);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
        updateThread.setDaemon(true);
        updateThread.start();
    }
    
    /**
     * Set up the market data table
     */
    private void setupMarketTable() {
        symbolColumn.setCellValueFactory(new PropertyValueFactory<>("symbol"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        changeColumn.setCellValueFactory(new PropertyValueFactory<>("change24h"));
        
        // Format the price column to show currency
        priceColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty || price == null) {
                    setText(null);
                } else {
                    setText(String.format("$%.2f", price));
                }
            }
        });
        
        // Format the change column to show percentage and color-code it
        changeColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Double change, boolean empty) {
                super.updateItem(change, empty);
                if (empty || change == null) {
                    setText(null);
                    setTextFill(Color.BLACK);
                } else {
                    setText(String.format("%.2f%%", change));
                    setTextFill(change >= 0 ? Color.GREEN : Color.RED);
                }
            }
        });
        
        // Bind the table to the market data
        marketTableView.setItems(FXCollections.observableArrayList(
            marketService.getMarketData().values()));
    }
    
    /**
     * Set up the transaction history table
     */
    private void setupTransactionTable() {
        transactionTimeColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getFormattedTimestamp()));
        
        transactionTypeColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getTypeAsString()));
        
        transactionSymbolColumn.setCellValueFactory(
            new PropertyValueFactory<>("cryptoSymbol"));
        
        transactionAmountColumn.setCellValueFactory(
            new PropertyValueFactory<>("amount"));
        
        transactionPriceColumn.setCellValueFactory(
            new PropertyValueFactory<>("price"));
        
        // Custom column for total value
        transactionTotalColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleDoubleProperty(
                cellData.getValue().getTotalValue()).asObject());
        
        // Format price and total columns to show currency
        transactionPriceColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty || price == null) {
                    setText(null);
                } else {
                    setText(String.format("$%.2f", price));
                }
            }
        });
        
        transactionTotalColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Double total, boolean empty) {
                super.updateItem(total, empty);
                if (empty || total == null) {
                    setText(null);
                } else {
                    setText(String.format("$%.2f", total));
                }
            }
        });
        
        // Bind the table to transaction history
        transactionTableView.setItems(transactionHistory);
    }
    
    /**
     * Set up the cryptocurrency selection dropdown
     */
    private void setupCryptoSelection() {
        // Populate combo box with available cryptocurrencies
        cryptoComboBox.setItems(FXCollections.observableArrayList(
            marketService.getMarketData().keySet()));
        
        // Set default selection
        if (!cryptoComboBox.getItems().isEmpty()) {
            cryptoComboBox.setValue(cryptoComboBox.getItems().get(0));
            updateSelectedCrypto();
        }
        
        // Add listener for selection changes
        cryptoComboBox.setOnAction(e -> updateSelectedCrypto());
        
        // Add listener for amount changes
        amountTextField.textProperty().addListener((obs, oldVal, newVal) -> {
            try {
                double amount = Double.parseDouble(newVal);
                updateTotalCost(amount);
            } catch (NumberFormatException ex) {
                totalCostLabel.setText("$0.00");
            }
        });
    }
    
    /**
     * Set up user information display
     */
    private void setupUserInfo() {
        usernameLabel.setText(currentUser.getUsername());
        updateUserInfo();
    }
    
    /**
     * Set up button handlers
     */
    private void setupButtonHandlers() {
        buyButton.setOnAction(e -> executeBuy());
        sellButton.setOnAction(e -> executeSell());
        resetAccountButton.setOnAction(e -> resetAccount());
    }
    
    /**
     * Update the UI elements with current data
     */
    private void updateUI() {
        updateUserInfo();
        marketTableView.refresh();
        updateSelectedCrypto();
    }
    
    /**
     * Update the user information display
     */
    private void updateUserInfo() {
        balanceLabel.setText(String.format("$%.2f", currentUser.getBalance()));
        
        double portfolioValue = currentUser.getPortfolioValue(marketService.getMarketData());
        portfolioValueLabel.setText(String.format("$%.2f", portfolioValue));
        
        double totalWorth = currentUser.getBalance() + portfolioValue;
        totalWorthLabel.setText(String.format("$%.2f", totalWorth));
    }
    
    /**
     * Update the selected cryptocurrency information
     */
    private void updateSelectedCrypto() {
        String symbol = cryptoComboBox.getValue();
        if (symbol == null) return;
        
        Cryptocurrency crypto = marketService.getCryptocurrency(symbol);
        if (crypto == null) return;
        
        currentPriceLabel.setText(String.format("$%.2f", crypto.getPrice()));
        cryptoDescriptionLabel.setText(crypto.getDescription());
        
        double ownedAmount = currentUser.getCryptoAmount(symbol);
        cryptoHoldingsLabel.setText(String.format("%.6f %s", ownedAmount, symbol));
        
        // Update total cost based on current amount
        try {
            double amount = Double.parseDouble(amountTextField.getText());
            updateTotalCost(amount);
        } catch (NumberFormatException e) {
            // Ignore invalid input
        }
    }
    
    /**
     * Update the total cost display
     */
    private void updateTotalCost(double amount) {
        String symbol = cryptoComboBox.getValue();
        if (symbol == null) return;
        
        Cryptocurrency crypto = marketService.getCryptocurrency(symbol);
        if (crypto == null) return;
        
        double totalCost = amount * crypto.getPrice();
        totalCostLabel.setText(String.format("$%.2f", totalCost));
    }
    
    /**
     * Execute a buy transaction
     */
    private void executeBuy() {
        String symbol = cryptoComboBox.getValue();
        if (symbol == null) return;
        
        Cryptocurrency crypto = marketService.getCryptocurrency(symbol);
        if (crypto == null) return;
        
        try {
            double amount = Double.parseDouble(amountTextField.getText());
            if (amount <= 0) {
                showAlert(Alert.AlertType.ERROR, "Invalid Amount", 
                          "Please enter a positive amount to buy.");
                return;
            }
            
            double price = crypto.getPrice();
            boolean success = currentUser.buyCrypto(symbol, amount, price);
            
            if (success) {
                Transaction transaction = new Transaction(
                    currentUser.getUsername(),
                    symbol,
                    amount,
                    price,
                    Transaction.TransactionType.BUY
                );
                transactionHistory.add(0, transaction);
                
                updateUserInfo();
                clearTransactionInput();
                showAlert(Alert.AlertType.INFORMATION, "Transaction Successful", 
                          String.format("Successfully purchased %.6f %s", amount, symbol));
            } else {
                showAlert(Alert.AlertType.ERROR, "Transaction Failed", 
                          "Insufficient funds for this purchase.");
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", 
                      "Please enter a valid number for amount.");
        }
    }
    
    /**
     * Execute a sell transaction
     */
    private void executeSell() {
        String symbol = cryptoComboBox.getValue();
        if (symbol == null) return;
        
        Cryptocurrency crypto = marketService.getCryptocurrency(symbol);
        if (crypto == null) return;
        
        try {
            double amount = Double.parseDouble(amountTextField.getText());
            if (amount <= 0) {
                showAlert(Alert.AlertType.ERROR, "Invalid Amount", 
                          "Please enter a positive amount to sell.");
                return;
            }
            
            double price = crypto.getPrice();
            boolean success = currentUser.sellCrypto(symbol, amount, price);
            
            if (success) {
                Transaction transaction = new Transaction(
                    currentUser.getUsername(),
                    symbol,
                    amount,
                    price,
                    Transaction.TransactionType.SELL
                );
                transactionHistory.add(0, transaction);
                
                updateUserInfo();
                clearTransactionInput();
                showAlert(Alert.AlertType.INFORMATION, "Transaction Successful", 
                          String.format("Successfully sold %.6f %s", amount, symbol));
            } else {
                showAlert(Alert.AlertType.ERROR, "Transaction Failed", 
                          "Insufficient cryptocurrency holdings for this sale.");
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", 
                      "Please enter a valid number for amount.");
        }
    }
    
    /**
     * Reset the user's account
     */
    private void resetAccount() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Reset Account");
        alert.setHeaderText("Reset Your Account");
        alert.setContentText("Are you sure you want to reset your account? " +
                            "This will clear your portfolio and set your balance back to the default.");
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                currentUser.resetAccount();
                transactionHistory.clear();
                updateUserInfo();
                clearTransactionInput();
            }
        });
    }
    
    /**
     * Clear the transaction input fields
     */
    private void clearTransactionInput() {
        amountTextField.clear();
        updateSelectedCrypto();
    }
    
    /**
     * Show an alert dialog
     */
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Clean up resources when the application closes
     */
    public void shutdown() {
        marketService.stopMarketUpdates();
    }
} 