# Cryptocurrency Trading Simulator with Clean UI

A JavaFX application that provides a clean, modern UI for simulating cryptocurrency trading. This project is designed for educational purposes to help users learn about cryptocurrency trading without financial risk.

## Features

- Modern, clean UI with responsive design
- Real-time cryptocurrency price simulation
- Buy and sell cryptocurrencies with virtual money
- Track portfolio performance and transaction history
- Detailed market information and cryptocurrency data

## Screenshots

_[Screenshots will be added after the first successful build]_

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 11 or higher
- Maven

### Running the Application

1. Clone this repository
2. Navigate to the project directory
3. Build the project:
   ```
   mvn clean package
   ```
4. Run the application:
   ```
   mvn javafx:run
   ```
   
   or
   
   ```
   java -jar target/java-crypto-ui-1.0-SNAPSHOT.jar
   ```

## Project Structure

- `src/main/java/com/cryptoui/model` - Data models with JavaFX properties for binding
- `src/main/java/com/cryptoui/controller` - Controllers for handling UI logic
- `src/main/java/com/cryptoui` - Main application
- `src/main/resources/fxml` - JavaFX FXML layout files
- `src/main/resources/css` - Stylesheet for the application

## Technologies Used

- JavaFX - UI framework
- FXML - UI layout
- CSS - Styling
- Maven - Build tool

## Future Enhancements

- Charts and graphs for cryptocurrency historical data
- Price alerts and notifications
- Integration with real cryptocurrency APIs
- Portfolio performance analytics
- Multiple user accounts
- Dark mode theme

## License

This project is licensed for educational purposes only. It is not intended for real trading or financial purposes. 