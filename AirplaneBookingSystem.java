import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class AirplaneBookingSystem {

    static Scanner inputScanner = new Scanner(System.in);

    static String flightName;
    static String destination;
    static String departureTime;
    static String ticketPrice;

    // 4 rows × 5 columns
    static String[][] seats = new String[4][5];

    public static void main(String[] args) {
        selectFlight(); 

        System.out.println("\nFlight Selected:");
        System.out.println("Flight: " + flightName + " | Destination: " + destination +
                " | Time: " + departureTime + " | Price: " + ticketPrice);

        loadFromFile(); // Safe load
        
        int choice;

        do {
            System.out.println("\n========== Airplane Booking System ==========");
            System.out.println("1. View Seat Map");
            System.out.println("2. Book a Seat");
            System.out.println("3. Cancel Booking");
            System.out.println("4. Save Data");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            choice = getIntInput();

            switch (choice) {
                case 1: viewSeats(); break;
                case 2: bookSeat(); break;
                case 3: cancelBooking(); break;
                case 4: saveToFile(); break;
                case 5: System.out.println("Exiting system... Thank you!"); break;
                default: System.out.println("Invalid choice. Try again.");
            }
        } while (choice != 5);

        inputScanner.close();
    }

    static void selectFlight() {
        int choice;
        boolean valid = false;
        System.out.println("Select a flight from the list below:");
        System.out.println("1. PK-001 | Lahore to Karachi | 09:00 AM | Rs. 18,000");
        System.out.println("2. PK-002 | Islamabad to Lahore | 11:30 AM | Rs. 20,000");
        System.out.println("3. PK-003 | Karachi to Peshawar | 03:00 PM | Rs. 22,000");
        System.out.println("4. PK-004 | Multan to Quetta | 07:00 PM | Rs. 24,000");

        do {
            System.out.print("Enter your choice (1-4): ");
            choice = getIntInput();
            switch (choice) {
                case 1: flightName = "PK-001"; destination = "Lahore to Karachi"; departureTime = "09:00 AM"; ticketPrice = "Rs. 18,000"; valid = true; break;
                case 2: flightName = "PK-002"; destination = "Islamabad to Lahore"; departureTime = "11:30 AM"; ticketPrice = "Rs. 20,000"; valid = true; break;
                case 3: flightName = "PK-003"; destination = "Karachi to Peshawar"; departureTime = "03:00 PM"; ticketPrice = "Rs. 22,000"; valid = true; break;
                case 4: flightName = "PK-004"; destination = "Multan to Quetta"; departureTime = "07:00 PM"; ticketPrice = "Rs. 24,000"; valid = true; break;
                default: System.out.println("Invalid input! Please enter 1-4.");
            }
        } while (!valid);
    }

    public static void initializeSeats() {
        for (int i = 0; i < seats.length; i++) {
            for (int j = 0; j < seats[i].length; j++) {
                seats[i][j] = "Empty"; // Ab 'Empty' likha aayega
            }
        }
    }

    public static void viewSeats() {
        System.out.println("\n----- Seat Map -----");
        for (int i = 0; i < seats.length; i++) {
            System.out.print("Row " + (i + 1) + ": ");
            for (int j = 0; j < seats[i].length; j++) {
                System.out.print("[" + seats[i][j] + "] ");
            }
            System.out.println();
        }
    }

    public static void bookSeat() {
        System.out.print("Enter row (1-4): ");
        int row = getIntInput() - 1;
        System.out.print("Enter column (1-5): ");
        int col = getIntInput() - 1;

        if (isValidSeat(row, col)) {
            if (seats[row][col].equals("Empty")) {
                System.out.print("Enter passenger name: ");
                inputScanner.nextLine(); // buffer clear
                seats[row][col] = inputScanner.nextLine();
                System.out.println("Seat booked successfully.");
            } else {
                System.out.println("Seat already booked by: " + seats[row][col]);
            }
        } else {
            System.out.println("Invalid seat selection.");
        }
    }

    public static void cancelBooking() {
        System.out.print("Enter row (1-4): ");
        int row = getIntInput() - 1;
        System.out.print("Enter column (1-5): ");
        int col = getIntInput() - 1;

        if (isValidSeat(row, col)) {
            if (!seats[row][col].equals("Empty")) {
                seats[row][col] = "Empty";
                System.out.println("Booking cancelled.");
            } else {
                System.out.println("Seat is already empty.");
            }
        } else {
            System.out.println("Invalid seat selection.");
        }
    }

    public static boolean isValidSeat(int row, int col) {
        return row >= 0 && row < seats.length && col >= 0 && col < seats[0].length;
    }

    public static void saveToFile() {
        try (PrintWriter pw = new PrintWriter(new FileOutputStream("seats.txt"))) {
            for (int i = 0; i < seats.length; i++) {
                for (int j = 0; j < seats[i].length; j++) {
                    pw.print(seats[i][j]);
                    if (j < seats[i].length - 1) pw.print(",");
                }
                pw.println();
            }
            System.out.println("Data saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }

    public static void loadFromFile() {
        // STEP 1: Pehle sab ko Empty kar do taake null ki gunjayish hi na rahe
        initializeSeats();

        File seatFile = new File("seats.txt");
        if (!seatFile.exists()) {
            System.out.println("No previous data found. Starting fresh.");
            return;
        }

        try (Scanner s = new Scanner(seatFile)) {
            int i = 0;
            while (s.hasNextLine() && i < seats.length) {
                String line = s.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] rowData = line.split(",");
                for (int j = 0; j < Math.min(rowData.length, seats[i].length); j++) {
                    // Agar file mein data hai to wo Empty ko replace kar dega
                    seats[i][j] = rowData[j].trim();
                }
                i++;
            }
            System.out.println("Data loaded successfully.");
        } catch (Exception e) {
            System.out.println("Error loading file: " + e.getMessage());
        }
    }

    public static int getIntInput() {
        while (!inputScanner.hasNextInt()) {
            System.out.print("Enter a valid number: ");
            inputScanner.next(); 
        }
        return inputScanner.nextInt();
    }
}