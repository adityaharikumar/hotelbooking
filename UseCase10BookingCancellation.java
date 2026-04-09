import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;

// Custom Exception for Booking Errors
class BookingException extends Exception {
    public BookingException(String message) {
        super(message);
    }
}

// Booking Class
class Booking {
    String bookingId;
    String roomType;
    int roomCount;
    boolean isCancelled;

    public Booking(String bookingId, String roomType, int roomCount) {
        this.bookingId = bookingId;
        this.roomType = roomType;
        this.roomCount = roomCount;
        this.isCancelled = false;
    }
}

// Inventory Class
class RoomInventory {
    private Map<String, Integer> rooms;

    public RoomInventory() {
        rooms = new HashMap<>();
        rooms.put("DELUXE", 5);
        rooms.put("STANDARD", 10);
        rooms.put("SUITE", 2);
    }

    public boolean isValidRoomType(String type) {
        return rooms.containsKey(type);
    }

    public int getAvailableRooms(String type) {
        return rooms.getOrDefault(type, 0);
    }

    public void bookRoom(String type, int count) throws BookingException {
        int available = getAvailableRooms(type);

        if (available < count) {
            throw new BookingException("Not enough rooms available.");
        }

        rooms.put(type, available - count);
    }

    public void releaseRoom(String type, int count) {
        int available = getAvailableRooms(type);
        rooms.put(type, available + count); // Inventory Restoration
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (String type : rooms.keySet()) {
            System.out.println(type + " : " + rooms.get(type));
        }
    }
}

// Cancellation Service
class CancellationService {
    private Stack<String> rollbackStack;

    public CancellationService() {
        rollbackStack = new Stack<>();
    }

    public void cancelBooking(String bookingId,
                              Map<String, Booking> bookings,
                              RoomInventory inventory) throws BookingException {

        // Validation
        if (!bookings.containsKey(bookingId)) {
            throw new BookingException("Booking does not exist.");
        }

        Booking booking = bookings.get(bookingId);

        if (booking.isCancelled) {
            throw new BookingException("Booking already cancelled.");
        }

        // LIFO Rollback Tracking
        rollbackStack.push(bookingId);

        // State Reversal
        inventory.releaseRoom(booking.roomType, booking.roomCount);

        // Update booking state
        booking.isCancelled = true;

        System.out.println("Cancellation successful for Booking ID: " + bookingId);
    }

    public void showRollbackHistory() {
        System.out.println("\nRollback History (LIFO):");
        while (!rollbackStack.isEmpty()) {
            System.out.println(rollbackStack.pop());
        }
    }
}

// Main Class
public class UseCase10BookingCancellation {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        RoomInventory inventory = new RoomInventory();
        Map<String, Booking> bookings = new HashMap<>();
        CancellationService cancelService = new CancellationService();

        try {
            // Create a sample booking
            System.out.println("Enter Booking ID: ");
            String id = sc.nextLine();

            System.out.println("Enter Room Type (DELUXE/STANDARD/SUITE): ");
            String type = sc.nextLine().toUpperCase();

            System.out.println("Enter number of rooms: ");
            int count = sc.nextInt();

            if (!inventory.isValidRoomType(type)) {
                throw new BookingException("Invalid room type.");
            }

            inventory.bookRoom(type, count);

            Booking booking = new Booking(id, type, count);
            bookings.put(id, booking);

            System.out.println("Booking Confirmed!");

            // Cancellation Flow
            System.out.println("\nEnter Booking ID to cancel: ");
            sc.nextLine(); // clear buffer
            String cancelId = sc.nextLine();

            cancelService.cancelBooking(cancelId, bookings, inventory);

        } catch (BookingException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected Error: " + e.getMessage());
        }

        // Final State
        inventory.displayInventory();
        cancelService.showRollbackHistory();

        sc.close();
    }
}