package hotelbooking;


import java.util.HashMap;
import java.util.Map;

// Abstract Room class
abstract class Room {
    protected String type;
    protected double price;

    public Room(String type, double price) {
        this.type = type;
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    public void displayDetails() {
        System.out.println("Room Type: " + type + " | Price: $" + price);
    }
}

// Concrete room classes
class SingleRoom extends Room {
    public SingleRoom() {
        super("Single Room", 100);
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double Room", 180);
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite Room", 300);
    }
}

// Inventory class (state holder)
class RoomInventory {

    private HashMap<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 0); // Example unavailable room
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public Map<String, Integer> getAllInventory() {
        return inventory;
    }
}

// Search Service (read-only access)
class RoomSearchService {

    private RoomInventory inventory;
    private HashMap<String, Room> roomCatalog;

    public RoomSearchService(RoomInventory inventory) {
        this.inventory = inventory;

        roomCatalog = new HashMap<>();
        roomCatalog.put("Single Room", new SingleRoom());
        roomCatalog.put("Double Room", new DoubleRoom());
        roomCatalog.put("Suite Room", new SuiteRoom());
    }

    public void searchAvailableRooms() {
        System.out.println("Available Rooms:");

        for (String roomType : roomCatalog.keySet()) {
            int available = inventory.getAvailability(roomType);

            if (available > 0) { // Filter unavailable rooms
                Room room = roomCatalog.get(roomType);
                room.displayDetails();
                System.out.println("Available: " + available);
                System.out.println();
            }
        }
    }
}

// Main application
public class UseCase4RoomSearch {

    public static void main(String[] args) {

        System.out.println("Welcome to Book My Stay");
        System.out.println("Hotel Booking System v4.0");
        System.out.println();

        RoomInventory inventory = new RoomInventory();
        RoomSearchService searchService = new RoomSearchService(inventory);

        searchService.searchAvailableRooms();
    }
}
