import java.sql.*;
import java.util.Scanner;

import javax.management.RuntimeErrorException;

/* 
 * 
 import org.jline.reader.*;
 import org.jline.terminal.Terminal;
 import org.jline.terminal.TerminalBuilder;
 */

/**
 * ReservationSystem
 */
import java.sql.*;
import java.util.Scanner;

import java.sql.*;
import java.util.Scanner;

public class RailwayTicketReservationSystem {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/Railway";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Welcome@123";

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            createTables(connection);

            Scanner scanner = new Scanner(System.in);

            while (true) {
                
                System.out.println("1. Book Ticket");
                System.out.println("2. View Tickets");
                System.out.println("3. Cancel Ticket");
                System.out.println("4. Exit");

                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        bookTicket(connection, scanner);
                        break;
                    case 2:
                        viewTickets(connection);
                        break;
                    case 3:
                        cancelTicket(connection, scanner);
                        break;
                    case 4:
                        System.out.println("Exiting the program. Thank you!");
                        connection.close();
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void createTables(Connection connection) throws SQLException {
        // (Same as before)
        Statement statement = connection.createStatement();

        String createTableQuery = "CREATE TABLE IF NOT EXISTS tickets (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "passenger_name VARCHAR(255) NOT NULL," +
                "source_station VARCHAR(255) NOT NULL," +
                "destination_station VARCHAR(255) NOT NULL," +
                "travel_date DATE NOT NULL," +
                "booking_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")";
        statement.executeUpdate(createTableQuery);
    }

    private static void bookTicket(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("Enter passenger name: ");
        String passengerName = scanner.next();

        System.out.print("Enter source station: ");
        String sourceStation = scanner.next();

        System.out.print("Enter destination station: ");
        String destinationStation = scanner.next();

        System.out.print("Enter travel date (YYYY-MM-DD): ");
        String travelDate = scanner.next();

        String insertQuery = "INSERT INTO tickets (passenger_name, source_station, destination_station, travel_date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, passengerName);
            preparedStatement.setString(2, sourceStation);
            preparedStatement.setString(3, destinationStation);
            preparedStatement.setDate(4, Date.valueOf(travelDate));

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Ticket booked successfully!");
            } else {
                System.out.println("Ticket booking failed. Please try again.");
            }
        }
    }

    private static void viewTickets(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM tickets");

        System.out.println("ID\tPassenger Name\tSource\tDestination\tTravel Date\tBooking Date");
        System.out.println("--------------------------------------------------------------");

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String passengerName = resultSet.getString("passenger_name");
            String sourceStation = resultSet.getString("source_station");
            String destinationStation = resultSet.getString("destination_station");
            Date travelDate = resultSet.getDate("travel_date");
            Timestamp bookingDate = resultSet.getTimestamp("booking_date");

            System.out.printf("%d\t%s\t\t%s\t%s\t\t%s\t%s\n", id, passengerName, sourceStation, destinationStation, travelDate, bookingDate);
        }
    }



 

    private static void cancelTicket(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("Enter ticket ID to cancel: ");
        int ticketId = scanner.nextInt();

        String cancelQuery = "DELETE FROM tickets WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(cancelQuery)) {
            preparedStatement.setInt(1, ticketId);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Ticket cancellation successful!");
            } else {
                System.out.println("Ticket cancellation failed. Please check the ticket ID and try again.");
            }
        }
    }

    // (Remaining methods stay the same)

}
