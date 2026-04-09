import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

// Custom Exception for Invalid Booking
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// Room Inventory Class
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

    public void bookRoom(String type, int count) throws InvalidBookingException {
        // Guard System State
        int available = getAvailableRooms(type);

        if (count <= 0) {
            throw new InvalidBookingException("Booking count must be greater than 0.");
        }

        if (available < count) {
            throw new InvalidBookingException("Not enough rooms available. Only " + available + " left.");
        }

        rooms.put(type, available - count);
    }

    public void displayInventory() {
        System.out.println("\nCurrent Room Availability:");
        for (String type : rooms.keySet()) {
            System.out.println(type + " : " + rooms.get(type));
        }
    }
}

// Validator Class
class BookingValidator {
    public static void validate(String roomType, int count, RoomInventory inventory)
            throws InvalidBookingException {

        // Input Validation
        if (roomType == null || roomType.trim().isEmpty()) {
            throw new InvalidBookingException("Room type cannot be empty.");
        }

        if (!inventory.isValidRoomType(roomType)) {
            throw new InvalidBookingException("Invalid room type: " + roomType);
        }

        if (count <= 0) {
            throw new InvalidBookingException("Number of rooms must be greater than zero.");
        }
    }
}

// Main Class
public class UseCase9ErrorHandlingValidation {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        RoomInventory inventory = new RoomInventory();

        try {
            System.out.println("Enter Room Type (DELUXE/STANDARD/SUITE): ");
            String roomType = sc.nextLine().toUpperCase();

            System.out.println("Enter number of rooms: ");
            int count = sc.nextInt();

            // Fail-Fast Validation
            BookingValidator.validate(roomType, count, inventory);

            // Booking Process
            inventory.bookRoom(roomType, count);

            System.out.println("Booking Successful!");

        } catch (InvalidBookingException e) {
            // Graceful Failure Handling
            System.out.println("Booking Failed: " + e.getMessage());
        } catch (Exception e) {
            // Catch unexpected errors
            System.out.println("Unexpected Error: " + e.getMessage());
        }

        // System continues safely
        inventory.displayInventory();

        sc.close();
    }
}