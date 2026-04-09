import java.io.*;
import java.util.*;

// 🔷 Booking Class
class Booking implements Serializable {
    String bookingId;
    String userName;
    String roomType;

    public Booking(String bookingId, String userName, String roomType) {
        this.bookingId = bookingId;
        this.userName = userName;
        this.roomType = roomType;
    }

    @Override
    public String toString() {
        return bookingId + " | " + userName + " | " + roomType;
    }
}

// 🔷 Inventory Class
class Inventory implements Serializable {
    Map<String, Integer> rooms = new HashMap<>();

    public void addRoom(String type, int count) {
        rooms.put(type, rooms.getOrDefault(type, 0) + count);
    }

    public boolean bookRoom(String type) {
        if (rooms.getOrDefault(type, 0) > 0) {
            rooms.put(type, rooms.get(type) - 1);
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return rooms.toString();
    }
}

// 🔷 Wrapper for Persistence
class SystemState implements Serializable {
    List<Booking> bookings;
    Inventory inventory;

    public SystemState(List<Booking> bookings, Inventory inventory) {
        this.bookings = bookings;
        this.inventory = inventory;
    }
}

// 🔷 Persistence Service
class PersistenceService {

    private static final String FILE_NAME = "hotel_data.ser";

    // Save state to file
    public static void save(SystemState state) {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

            oos.writeObject(state);
            System.out.println("✅ Data saved successfully!");

        } catch (IOException e) {
            System.out.println("❌ Error saving data: " + e.getMessage());
        }
    }

    // Load state from file
    public static SystemState load() {
        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(FILE_NAME))) {

            SystemState state = (SystemState) ois.readObject();
            System.out.println("✅ Data loaded successfully!");
            return state;

        } catch (FileNotFoundException e) {
            System.out.println("⚠ No previous data found. Starting fresh.");
        } catch (Exception e) {
            System.out.println("❌ Corrupted data. Starting fresh.");
        }
        return null;
    }
}

// 🔷 Main System
public class UseCase12DataPersistenceRecovery {

    public static void main(String[] args) {

        // 🔹 Try to load existing state
        SystemState state = PersistenceService.load();

        List<Booking> bookings;
        Inventory inventory;

        if (state != null) {
            bookings = state.bookings;
            inventory = state.inventory;
        } else {
            // Initialize fresh system
            bookings = new ArrayList<>();
            inventory = new Inventory();

            inventory.addRoom("Deluxe", 2);
            inventory.addRoom("Standard", 3);
        }

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== Book My Stay =====");
            System.out.println("1. View Inventory");
            System.out.println("2. Book Room");
            System.out.println("3. View Bookings");
            System.out.println("4. Exit (Save & Shutdown)");
            System.out.print("Choose: ");

            int choice = sc.nextInt();
            sc.nextLine(); // clear buffer

            switch (choice) {

                case 1:
                    System.out.println("Inventory: " + inventory);
                    break;

                case 2:
                    System.out.print("Enter Name: ");
                    String name = sc.nextLine();

                    System.out.print("Enter Room Type (Deluxe/Standard): ");
                    String type = sc.nextLine();

                    if (inventory.bookRoom(type)) {
                        String bookingId = "B" + (bookings.size() + 1);
                        Booking b = new Booking(bookingId, name, type);
                        bookings.add(b);
                        System.out.println("✅ Booking Successful: " + b);
                    } else {
                        System.out.println("❌ Room Not Available!");
                    }
                    break;

                case 3:
                    System.out.println("Bookings:");
                    for (Booking b : bookings) {
                        System.out.println(b);
                    }
                    break;

                case 4:
                    // 🔹 Save state before exit
                    PersistenceService.save(new SystemState(bookings, inventory));
                    System.out.println("💾 System shutting down...");
                    return;

                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}