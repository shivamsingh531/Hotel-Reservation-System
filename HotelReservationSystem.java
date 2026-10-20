
import java.util.*;
import java.io.*;

class Room {
    int roomNumber;
    String category;
    double price;

    Room(int roomNumber, String category, double price) {
        this.roomNumber = roomNumber;
        this.category = category;
        this.price = price;
    }
}

class Booking {
    int bookingId;
    String guestName;
    int roomNumber;
    String category;
    int nights;
    double totalAmount;
    String paymentStatus;

    Booking(int bookingId, String guestName, Room room,
            int nights, String paymentStatus) {
        this.bookingId = bookingId;
        this.guestName = guestName;
        this.roomNumber = room.roomNumber;
        this.category = room.category;
        this.nights = nights;
        this.totalAmount = room.price * nights;
        this.paymentStatus = paymentStatus;
    }
}

public class HotelReservationSystem {

    static Scanner sc = new Scanner(System.in);
    static ArrayList<Room> rooms = new ArrayList<>();
    static ArrayList<Booking> bookings = new ArrayList<>();

    static final String FILE_NAME = "bookings.txt";
    static int nextBookingId = 1001;

    public static void main(String[] args) {
        addRooms();
        loadBookings();

        int choice;

        do {
            System.out.println("\n===== HOTEL RESERVATION SYSTEM =====");
            System.out.println("1. Search Available Rooms");
            System.out.println("2. Book a Room");
            System.out.println("3. Cancel Reservation");
            System.out.println("4. View Booking Details");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");

            choice = readInt();

            switch (choice) {
                case 1:
                    searchRooms();
                    break;
                case 2:
                    bookRoom();
                    break;
                case 3:
                    cancelReservation();
                    break;
                case 4:
                    viewBookings();
                    break;
                case 5:
                    saveBookings();
                    System.out.println("Thank you for visiting!");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        } while (choice != 5);

        sc.close();
    }

    static void addRooms() {
        for (int i = 101; i <= 105; i++)
            rooms.add(new Room(i, "Standard", 1500));

        for (int i = 201; i <= 203; i++)
            rooms.add(new Room(i, "Deluxe", 2500));

        for (int i = 301; i <= 302; i++)
            rooms.add(new Room(i, "Suite", 4000));
    }

    static int readInt() {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Enter a valid number: ");
            }
        }
    }

    static boolean isAvailable(Room room) {
        for (Booking b : bookings) {
            if (b.roomNumber == room.roomNumber)
                return false;
        }
        return true;
    }

    static void searchRooms() {
        System.out.println("\nRoom Categories:");
        System.out.println("1. Standard - Rs.1500 per night");
        System.out.println("2. Deluxe   - Rs.2500 per night");
        System.out.println("3. Suite    - Rs.4000 per night");
        System.out.print("Choose category (0 for all): ");

        int choice = readInt();
        String category = "";

        if (choice == 1) category = "Standard";
        else if (choice == 2) category = "Deluxe";
        else if (choice == 3) category = "Suite";
        else if (choice != 0) {
            System.out.println("Invalid category.");
            return;
        }

        boolean found = false;

        System.out.println("\nAvailable Rooms:");

        for (Room room : rooms) {
            if ((category.isEmpty() ||
                    room.category.equals(category))
                    && isAvailable(room)) {
                System.out.println("Room: " + room.roomNumber
                        + " | " + room.category
                        + " | Rs." + room.price + "/night");
                found = true;
            }
        }

        if (!found)
            System.out.println("No rooms available in this category.");
    }

    static void bookRoom() {
        searchRooms();

        System.out.print("\nEnter room number to book: ");
        int roomNumber = readInt();

        Room selectedRoom = null;

        for (Room room : rooms) {
            if (room.roomNumber == roomNumber
                    && isAvailable(room)) {
                selectedRoom = room;
                break;
            }
        }

        if (selectedRoom == null) {
            System.out.println("Room not available or invalid.");
            return;
        }

        System.out.print("Enter guest name: ");
        String name = sc.nextLine().trim();

        if (name.isEmpty()) {
            System.out.println("Guest name cannot be empty.");
            return;
        }

        System.out.print("Enter number of nights: ");
        int nights = readInt();

        if (nights <= 0) {
            System.out.println("Nights must be greater than zero.");
            return;
        }

        double total = selectedRoom.price * nights;

        System.out.println("\n----- BOOKING SUMMARY -----");
        System.out.println("Guest: " + name);
        System.out.println("Room: " + selectedRoom.roomNumber);
        System.out.println("Category: " + selectedRoom.category);
        System.out.println("Nights: " + nights);
        System.out.println("Total: Rs." + total);

        System.out.print("Confirm simulated payment? (Y/N): ");
        String payment = sc.nextLine().trim();

        if (!payment.equalsIgnoreCase("Y")) {
            System.out.println("Booking cancelled before payment.");
            return;
        }

        Booking booking = new Booking(nextBookingId++,
                name, selectedRoom, nights, "Paid");

        bookings.add(booking);
        saveBookings();

        System.out.println("Payment successful (simulation).");
        System.out.println("Booking confirmed!");
        System.out.println("Your Booking ID: " + booking.bookingId);
    }

    static void cancelReservation() {
        System.out.print("Enter booking ID: ");
        int id = readInt();

        for (Booking b : bookings) {
            if (b.bookingId == id) {
                bookings.remove(b);
                saveBookings();
                System.out.println("Reservation cancelled.");
                System.out.println(
                    "Note: This demo does not process actual refunds.");
                return;
            }
        }

        System.out.println("Booking ID not found.");
    }

    static void viewBookings() {
        if (bookings.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }

        System.out.println("\n===== BOOKING DETAILS =====");

        for (Booking b : bookings) {
            System.out.println("----------------------------");
            System.out.println("Booking ID: " + b.bookingId);
            System.out.println("Guest Name: " + b.guestName);
            System.out.println("Room Number: " + b.roomNumber);
            System.out.println("Category: " + b.category);
            System.out.println("Nights: " + b.nights);
            System.out.println("Total Amount: Rs." + b.totalAmount);
            System.out.println("Payment Status: " + b.paymentStatus);
        }
    }

    static void saveBookings() {
        try (PrintWriter writer =
                new PrintWriter(new FileWriter(FILE_NAME))) {

            for (Booking b : bookings) {
                writer.println(b.bookingId + "|" + b.guestName
                        + "|" + b.roomNumber + "|" + b.category
                        + "|" + b.nights + "|" + b.totalAmount
                        + "|" + b.paymentStatus);
            }
        } catch (IOException e) {
            System.out.println("Error saving bookings.");
        }
    }

    static void loadBookings() {
        File file = new File(FILE_NAME);

        if (!file.exists())
            return;

        try (BufferedReader reader =
                new BufferedReader(new FileReader(file))) {

            String line;

            while ((line = reader.readLine()) != null) {
                String[] data = line.split("\\|", -1);

                int id = Integer.parseInt(data[0]);
                String name = data[1];
                int roomNo = Integer.parseInt(data[2]);
                String category = data[3];
                int nights = Integer.parseInt(data[4]);
                double total = Double.parseDouble(data[5]);
                String status = data[6];

                Room room = null;

                for (Room r : rooms) {
                    if (r.roomNumber == roomNo
                            && r.category.equals(category)) {
                        room = r;
                        break;
                    }
                }

                if (room != null) {
                    Booking b = new Booking(id, name, room,
                            nights, status);
                    b.totalAmount = total;
                    bookings.add(b);

                    if (id >= nextBookingId)
                        nextBookingId = id + 1;
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Could not load booking records.");
        }
    }
}
